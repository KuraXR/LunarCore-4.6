package emu.lunarcore.server.packet.send;

import emu.lunarcore.data.GameData;
import emu.lunarcore.game.player.Player;
import emu.lunarcore.proto.GetPamSkinDataScRspOuterClass.GetPamSkinDataScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketGetPamSkinDataScRsp extends BasePacket {

    public PacketGetPamSkinDataScRsp(Player player) {
        super(CmdId.GetPamSkinDataScRsp);

        var pamSkinsList = GameData.getPomSkinExcelMap().getIds();

        // Fix accounts with no skin or the old default (25200, a typo for 252000). An unknown
        // skin leaves the 4.6 client unable to build Pom-Pom, so the Express never finishes loading
        if (!pamSkinsList.contains(player.getCurrentPamSkinId())) {
            player.setCurrentPamSkinId(252000);
        }

        var data = GetPamSkinDataScRsp.newInstance()
                .addAllUnlockedPamSkins(pamSkinsList.intStream().toArray())
                .setCurrentPamSkinId(player.getCurrentPamSkinId());

        this.setData(data);
    }
}
