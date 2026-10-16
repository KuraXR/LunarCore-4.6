package emu.lunarcore.server.packet.send;

import emu.lunarcore.proto.BattlePassInfoNotifyOuterClass.BattlePassInfoNotify;
import emu.lunarcore.proto.BattlePassInfoNotifyOuterClass.BattlePassInfoNotify.BpTierType;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketBattlePassInfoNotify extends BasePacket {

    public PacketBattlePassInfoNotify() {
        super(CmdId.BattlePassInfoNotify);

        var data = BattlePassInfoNotify.newInstance()
                // 4.6: the taken-reward bitmasks are obfuscated and cannot be told apart
                .setLevel(70)
                .setCurWeekAddExpSum(8000)
                .setExp(800)
                .setCurBpId(18) // doesn't matter
                .setBpTierType(BpTierType.BP_TIER_TYPE_PREMIUM_2);

        this.setData(data);
    }
}
