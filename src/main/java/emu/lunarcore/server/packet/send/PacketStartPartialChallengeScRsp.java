package emu.lunarcore.server.packet.send;

import emu.lunarcore.game.challenge.ChallengeInstance;
import emu.lunarcore.game.player.Player;
import emu.lunarcore.proto.StartPartialChallengeScRspOuterClass.StartPartialChallengeScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Retcode;

public class PacketStartPartialChallengeScRsp extends BasePacket {

    public PacketStartPartialChallengeScRsp(Retcode retcode) {
        super(CmdId.StartPartialChallengeScRsp);

        var data = StartPartialChallengeScRsp.newInstance()
                .setRetcode(retcode.getVal());

        this.setData(data);
    }

    public PacketStartPartialChallengeScRsp(Player player) {
        super(CmdId.StartPartialChallengeScRsp);

        var data = StartPartialChallengeScRsp.newInstance();
        var challenge = player.getInstance(ChallengeInstance.class);

        if (challenge != null) {
            data.setScene(player.getScene().toProto());
            data.setChallengeInfo(challenge.toProto());
            data.setLineup(player.getCurrentLineup().toProto());
        } else {
            data.setRetcode(1);
        }

        this.setData(data);
    }
}
