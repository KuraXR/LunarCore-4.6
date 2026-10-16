package emu.lunarcore.server.packet.send;

import emu.lunarcore.proto.BlackInfoOuterClass.BlackInfo;
import emu.lunarcore.proto.PlayerGetTokenScRspOuterClass.PlayerGetTokenScRsp;
import emu.lunarcore.server.game.GameSession;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Retcode;

public class PacketPlayerGetTokenScRsp extends BasePacket {

    public PacketPlayerGetTokenScRsp(GameSession session) {
        super(CmdId.PlayerGetTokenScRsp);

        var data = PlayerGetTokenScRsp.newInstance()
                .setUid(session.getUid())
                .setBlackInfo(BlackInfo.newInstance());

        this.setData(data);
    }

    public PacketPlayerGetTokenScRsp(Retcode retcode) {
        super(CmdId.PlayerGetTokenScRsp);

        var data = PlayerGetTokenScRsp.newInstance()
                .setRetcode(retcode.getVal());

        this.setData(data);
    }
}
