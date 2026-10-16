package emu.lunarcore.server.packet.recv;

import emu.lunarcore.server.game.GameSession;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Opcodes;
import emu.lunarcore.server.packet.PacketHandler;
import emu.lunarcore.server.packet.send.PacketRecallPetScRsp;

@Opcodes(CmdId.RecallPetCsReq)
public class HandlerRecallPetCsReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] data) throws Exception {
        session.getPlayer().setPetId(0);
        session.getPlayer().save();

        // 4.6 has no CurPetChangedScNotify; the reply carries the cleared summon state
        session.send(new PacketRecallPetScRsp());
    }

}
