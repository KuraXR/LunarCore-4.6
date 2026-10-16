package emu.lunarcore.server.packet.send;

import emu.lunarcore.game.battle.Battle;
import emu.lunarcore.proto.QuickStartCocoonStageScRspOuterClass.QuickStartCocoonStageScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketQuickStartCocoonStageScRsp extends BasePacket {

    public PacketQuickStartCocoonStageScRsp(Battle battle) {
        super(CmdId.QuickStartCocoonStageScRsp);

        var data = QuickStartCocoonStageScRsp.newInstance();

        if (battle != null) {
            data.setBattleInfo(battle.toProto())
            .setCocoonId(battle.getMappingInfoId())
            .setWave(battle.getCocoonWave()); // 4.6 removed wave_count from this reply
        } else {
            data.setRetcode(1);
        }

        this.setData(data);
    }
}
