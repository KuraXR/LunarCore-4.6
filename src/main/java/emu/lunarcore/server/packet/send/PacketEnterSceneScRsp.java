package emu.lunarcore.server.packet.send;

import emu.lunarcore.game.player.Player;
import emu.lunarcore.proto.EnterSceneScRspOuterClass.EnterSceneScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketEnterSceneScRsp extends BasePacket {

    public PacketEnterSceneScRsp(Player player, boolean success) {
        super(CmdId.EnterSceneScRsp);

        var data = EnterSceneScRsp.newInstance();

        if (success && player.getScene() != null) {
            // The reply carries the floor and flag 6; flag 11 is added when a full scene
            // (EnterSceneByServerScNotify) follows, which LunarCore always sends
            data.getMutableSceneIdentifier().setFloorId(player.getScene().getFloorId());
            data.setIsEntered(true);
            data.setIsFullScene(true);
        } else {
            data.setRetcode(1);
        }

        this.setData(data);
    }
}
