package emu.lunarcore.server.http.handlers;

import org.jetbrains.annotations.NotNull;

import emu.lunarcore.LunarCore;
import emu.lunarcore.proto.GateserverOuterClass.Gateserver;
import emu.lunarcore.util.Utils;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class QueryGatewayHandler implements Handler {

    public QueryGatewayHandler() {

    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        // Get streaming data from config
        var data = LunarCore.getHotfixData();
        
        // Build gateserver proto. The 4.6 gateway has 88 obfuscated fields; only these are known:
        // region, ip, port and the design-data url/flag.
        Gateserver gateserver = Gateserver.newInstance()
                .setRegionName(LunarCore.getConfig().getGameServer().getId())
                .setIp(LunarCore.getConfig().getGameServer().getPublicAddress())
                .setPort(LunarCore.getConfig().getGameServer().getPublicPort());

        // Design data hotfix (asset bundle, lua and ifix urls have no known 4.6 field)
        if (data.exResourceUrl != null && !data.exResourceUrl.isBlank()) {
            gateserver.setExResourceUrl(data.exResourceUrl)
                .setEnableDesignDataVersionUpdate(true);
        } else if (DesignDataHandler.isAvailable()) {
            // Without a design data url the client never loads its full design data (text maps)
            gateserver.setExResourceUrl(DesignDataHandler.getUrl())
                .setEnableDesignDataVersionUpdate(true);
        }
        
        // Log
        if (LunarCore.getConfig().getLogOptions().connections) {
            LunarCore.getLogger().info("Client request: query_gateway");
        }

        // Encode to base64 and send to client
        ctx.result(Utils.base64Encode(gateserver.toByteArray()));
    }

}
