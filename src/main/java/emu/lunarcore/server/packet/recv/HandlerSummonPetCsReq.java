package emu.lunarcore.server.packet.recv;

import emu.lunarcore.proto.SummonPetCsReqOuterClass.SummonPetCsReq;
import emu.lunarcore.server.game.GameSession;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Opcodes;
import emu.lunarcore.server.packet.PacketHandler;
import emu.lunarcore.server.packet.send.PacketSummonPetScRsp;

@Opcodes(CmdId.SummonPetCsReq)
public class HandlerSummonPetCsReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] data) throws Exception {
        var req = SummonPetCsReq.parseFrom(data);

        // 4.6 summons by a 4-value mode (enum, meaning unverified) instead of a pet id;
        // remember it and echo it back as the summon state
        int mode = req.getMode();
        if (mode < 0 || mode > 3) {
            session.send(new PacketSummonPetScRsp());
            return;
        }

        session.getPlayer().setPetId(mode);
        session.getPlayer().save();

        session.send(new PacketSummonPetScRsp(mode));
    }

}
