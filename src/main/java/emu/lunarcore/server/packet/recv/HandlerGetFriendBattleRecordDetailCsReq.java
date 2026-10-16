package emu.lunarcore.server.packet.recv;

import emu.lunarcore.proto.GetFriendBattleRecordDetailCsReqOuterClass.GetFriendBattleRecordDetailCsReq;
import emu.lunarcore.server.game.GameSession;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Opcodes;
import emu.lunarcore.server.packet.PacketHandler;
import emu.lunarcore.server.packet.send.PacketGetFriendBattleRecordDetailScRsp;


@Opcodes(CmdId.GetFriendBattleRecordDetailCsReq)
public class HandlerGetFriendBattleRecordDetailCsReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] data) throws Exception {
        var req = GetFriendBattleRecordDetailCsReq.parseFrom(data);

        session.send(new PacketGetFriendBattleRecordDetailScRsp(req.getUid()));
    }

}
