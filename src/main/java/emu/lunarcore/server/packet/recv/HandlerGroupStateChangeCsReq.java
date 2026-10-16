package emu.lunarcore.server.packet.recv;

import emu.lunarcore.proto.GroupStateChangeCsReqOuterClass.GroupStateChangeCsReq;
import emu.lunarcore.server.game.GameSession;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Opcodes;
import emu.lunarcore.server.packet.PacketHandler;
import emu.lunarcore.server.packet.send.PacketGroupStateChangeScRsp;

@Opcodes(CmdId.GroupStateChangeCsReq)
public class HandlerGroupStateChangeCsReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] data) throws Exception {
        var req = GroupStateChangeCsReq.parseFrom(data);
        var groupStateInfo = req.getMutableGroupInfo();
        
        // 4.6 has no GroupStateChangeScNotify; the response alone carries the state
        session.send(new PacketGroupStateChangeScRsp(groupStateInfo));
    }

}
