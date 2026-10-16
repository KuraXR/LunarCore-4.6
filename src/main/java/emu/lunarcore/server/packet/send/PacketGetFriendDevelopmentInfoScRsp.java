package emu.lunarcore.server.packet.send;

import emu.lunarcore.proto.GetFriendDevelopmentInfoScRspOuterClass.GetFriendDevelopmentInfoScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketGetFriendDevelopmentInfoScRsp extends BasePacket {

    public PacketGetFriendDevelopmentInfoScRsp(int uid) {
        super(CmdId.GetFriendDevelopmentInfoScRsp);

        // TODO 4.6: the development feed was restructured; the 4.2 demo entries are no longer sent
        var data = GetFriendDevelopmentInfoScRsp.newInstance()
                .setUid(uid);

        this.setData(data);
    }
}
