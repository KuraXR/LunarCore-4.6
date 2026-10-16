package emu.lunarcore.server.http.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import emu.lunarcore.LunarCore;
import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Serves the 4.6 client's own cached design data back to it (read only).
 * <p>
 * The 4.6 client only loads its full design data (text maps included) after the gateway gives it
 * a design data url. Without one it stays on the small built-in index and fails on
 * Textmap_en.binary_header.bytes. This answers that url with an archive describing the hotfix
 * already cached in the client's Persistent folder, so the client finds its copy current.
 */
public class DesignDataHandler implements Handler {
    public static final String PATH = "/design_data";

    private static final Pattern FILE_NAME = Pattern.compile("[A-Za-z0-9_-]+(?:/[A-Za-z0-9_-]+)*\\.(?:bytes|ini)");

    public static String getUrl() {
        return LunarCore.getConfig().getHttpServer().getDisplayAddress() + PATH;
    }

    // Only advertise a cached hotfix that is newer than the client's built-in data and whose
    // manifests are intact. Advertising the built-in version can make the client drop its cache.
    public static boolean isAvailable() {
        try {
            return createArchive() != null;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String file = ctx.path().substring(PATH.length());
        if (file.startsWith("/")) file = file.substring(1);
        if (file.startsWith("client/")) file = file.substring("client/".length());
        if (file.startsWith("Windows/")) file = file.substring("Windows/".length());

        if (LunarCore.getConfig().getLogOptions().connections) {
            LunarCore.getLogger().info("Client request: design_data " + ctx.method() + " " + file);
        }

        // Archive listing the cached design and native data versions
        if (file.equals("M_Design_ArchiveV.bytes")) {
            byte[] archive = createArchive();
            if (archive == null) {
                ctx.status(404).result("not found");
                return;
            }
            ctx.contentType("application/octet-stream").result(archive);
            return;
        }

        // Cached files, Persistent first
        String category = "DesignData";
        if (file.startsWith("NativeData/")) {
            category = "NativeData";
            file = file.substring("NativeData/".length());
        } else if (file.startsWith("DesignData/")) {
            file = file.substring("DesignData/".length());
        }

        Path path = resolve(category, file);
        if (path == null) {
            ctx.status(404).result("not found");
            return;
        }

        ctx.contentType("application/octet-stream");
        ctx.header("Content-Length", Long.toString(Files.size(path)));
        ctx.result(Files.newInputStream(path));
    }

    private static Path resolve(String category, String file) throws IOException {
        if (!FILE_NAME.matcher(file).matches()) return null;

        for (String folder : new String[] {"Persistent", "StreamingAssets"}) {
            Path root = getDataFolder(folder, category);
            if (root == null) continue;

            Path candidate = root.resolve(file).normalize();
            if (candidate.startsWith(root) && Files.isRegularFile(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private static Path getDataFolder(String folder, String category) {
        String clientDataDir = LunarCore.getConfig().getClientDataDir();
        if (clientDataDir == null || clientDataDir.isBlank()) return null;
        return Path.of(clientDataDir, folder, category, "Windows").toAbsolutePath().normalize();
    }

    private static byte[] createArchive() throws IOException {
        Descriptor design = Descriptor.read(getDataFolder("Persistent", "DesignData"), "M_DesignV.bytes");
        Descriptor builtIn = Descriptor.read(getDataFolder("StreamingAssets", "DesignData"), "M_DesignV.bytes");
        if (design == null || builtIn == null || design.patch <= builtIn.patch) return null;
        if (!design.matches(getDataFolder("Persistent", "DesignData").resolve("DesignV_" + design.hash + ".bytes"))) return null;

        Descriptor nativeData = Descriptor.read(getDataFolder("Persistent", "NativeData"), "M_NativeDataV.bytes");
        if (nativeData == null || !nativeData.matches(resolve("NativeData", "NativeDataV_" + nativeData.hash + ".bytes"))) return null;

        // Same layout as the client's built-in M_Design_ArchiveV.bytes
        String archive = design.toJson("M_DesignV") + "\r\n" + nativeData.toJson("M_NativeDataV") + "\r\n";
        return archive.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Version file (M_DesignV.bytes / M_NativeDataV.bytes): "SRMI" header, version, md5 of the
     * manifest it points to, manifest size, timestamp, base url and previous patch.
     */
    private static class Descriptor {
        private long major, minor, patch, prevPatch, size, timestamp;
        private String hash, baseUrl;

        private static Descriptor read(Path folder, String name) throws IOException {
            if (folder == null) return null;
            Path file = folder.resolve(name);
            if (!Files.isRegularFile(file) || Files.size(file) < 66 || Files.size(file) > 65_601) return null;

            byte[] data = Files.readAllBytes(file);
            ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            if (buf.getInt(0) != 0x494d5253 || buf.getInt(4) != 0x01000300 || buf.getInt(8) != data.length) return null;

            int urlLength = Short.toUnsignedInt(buf.getShort(60));
            if (data.length != 66 + urlLength) return null;

            Descriptor d = new Descriptor();
            d.major = Integer.toUnsignedLong(buf.getInt(16));
            d.minor = Integer.toUnsignedLong(buf.getInt(20));
            d.patch = Integer.toUnsignedLong(buf.getInt(24));
            d.hash = String.format("%08x%08x%08x%08x", buf.getInt(28), buf.getInt(32), buf.getInt(36), buf.getInt(40));
            d.size = buf.getLong(44);
            d.timestamp = buf.getLong(52);
            d.baseUrl = new String(data, 62, urlLength, StandardCharsets.UTF_8);
            d.prevPatch = Integer.toUnsignedLong(buf.getInt(62 + urlLength));
            return d;
        }

        private boolean matches(Path manifest) throws IOException {
            if (manifest == null || !Files.isRegularFile(manifest) || Files.size(manifest) != this.size) return false;
            try {
                byte[] md5 = MessageDigest.getInstance("MD5").digest(Files.readAllBytes(manifest));
                return HexFormat.of().formatHex(md5).equals(this.hash);
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new IllegalStateException(e);
            }
        }

        private String toJson(String fileName) {
            JsonObject json = new JsonObject();
            json.addProperty("MajorVersion", major);
            json.addProperty("MinorVersion", minor);
            json.addProperty("PatchVersion", patch);
            json.addProperty("PrevPatch", prevPatch);
            json.addProperty("ContentHash", hash);
            json.addProperty("FileSize", size);
            json.addProperty("TimeStamp", timestamp);
            json.addProperty("FileName", fileName);
            json.addProperty("BaseAssetsDownloadUrl", baseUrl);
            return json.toString();
        }
    }
}
