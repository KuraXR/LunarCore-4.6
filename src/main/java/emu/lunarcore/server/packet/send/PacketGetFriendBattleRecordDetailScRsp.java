package emu.lunarcore.server.packet.send;

import emu.lunarcore.proto.GetFriendBattleRecordDetailScRspOuterClass.GetFriendBattleRecordDetailScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketGetFriendBattleRecordDetailScRsp extends BasePacket {

    public PacketGetFriendBattleRecordDetailScRsp(int uid) {
        super(CmdId.GetFriendBattleRecordDetailScRsp);

        // TODO 4.6: friend battle records were restructured; the 4.2 demo records are no longer sent
        var data = GetFriendBattleRecordDetailScRsp.newInstance()
                .setUid(uid);

        this.setData(data);
    }
}
