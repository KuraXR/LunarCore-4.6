package emu.lunarcore.server.packet.send;

import emu.lunarcore.game.player.Player;
import emu.lunarcore.proto.GetPetDataScRspOuterClass.GetPetDataScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketGetPetDataScRsp extends BasePacket {

    public PacketGetPetDataScRsp(Player player) {
        super(CmdId.GetPetDataScRsp);

        // 4.6 reports the summon state instead of cur_pet_id (petId holds the 4.6 summon mode;
        // older saves may hold a 4.2 pet id, which is not a mode)
        var data = GetPetDataScRsp.newInstance();
        int mode = player.getPetId();
        data.getMutableState().setMode(mode >= 0 && mode <= 3 ? mode : 0);

        for (int id : player.getUnlocks().getPets()) {
            data.addPetIdList(id);
        }

        this.setData(data);
    }
}
