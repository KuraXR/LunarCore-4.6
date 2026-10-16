package emu.lunarcore.server.packet.send;

import emu.lunarcore.data.GameData;
import emu.lunarcore.proto.GetChallengeGroupStatisticsScRspOuterClass.GetChallengeGroupStatisticsScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketGetChallengeGroupStatisticsScRsp extends BasePacket {

    public PacketGetChallengeGroupStatisticsScRsp(int groupId) {
        super(CmdId.GetChallengeGroupStatisticsScRsp);

        var data = GetChallengeGroupStatisticsScRsp.newInstance()
                .setGroupId(groupId);

        // The 4.6 client reads the statistics for the group's mode and throws if they are missing.
        // Empty statistics are enough; best records are not sent yet
        var group = GameData.getChallengeGroupExcelMap().get(groupId);
        String type = group != null && group.getChallengeGroupType() != null ? group.getChallengeGroupType() : "";

        switch (type) {
            case "Story" -> data.getMutableChallengeStory();
            case "Boss" -> data.getMutableChallengeBoss();
            default -> data.getMutableChallengeMemory();
        }

        this.setData(data);
    }
}
