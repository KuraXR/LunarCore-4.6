package emu.lunarcore.server.packet.recv;

import emu.lunarcore.proto.PlayBackGroundMusicCsReqOuterClass.PlayBackGroundMusicCsReq;
import emu.lunarcore.server.game.GameSession;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Opcodes;
import emu.lunarcore.server.packet.PacketHandler;
import emu.lunarcore.server.packet.send.PacketPlayBackGroundMusicScRsp;

@Opcodes(CmdId.PlayBackGroundMusicCsReq)
public class HandlerPlayBackGroundMusicCsReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] data) throws Exception {
        var req = PlayBackGroundMusicCsReq.parseFrom(data);

        // 4.6 sends a playing state; its current entry is the selected music
        int musicId = req.getPlayState().getCurrent().getId();
        if (musicId != 0) {
            session.getPlayer().setCurrentBgm(musicId);
        }

        session.send(new PacketPlayBackGroundMusicScRsp(session.getPlayer().getCurrentBgm()));
    }

}
