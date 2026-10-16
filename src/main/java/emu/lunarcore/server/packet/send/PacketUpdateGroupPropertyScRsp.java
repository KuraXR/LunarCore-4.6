package emu.lunarcore.server.packet.send;

import emu.lunarcore.command.commands.PortTestCommand;
import emu.lunarcore.proto.UpdateGroupPropertyScRspOuterClass.UpdateGroupPropertyScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketUpdateGroupPropertyScRsp extends BasePacket {

    public PacketUpdateGroupPropertyScRsp(int floorId, int groupId, String name, int value) {
        super(CmdId.UpdateGroupPropertyScRsp);

        var data = UpdateGroupPropertyScRsp.newInstance()
                .setFloorId(floorId)
                .setGroupId(groupId)
                .setPropertyName(name);

        // 4.6: which of the two unnamed ints is the value is UNVERIFIED (see /porttest groupprop)
        if (PortTestCommand.groupPropAlt) {
            data.setCurPropertyValueAlt(value);
        } else {
            data.setCurPropertyValue(value);
        }

        this.setData(data);
    }
}
