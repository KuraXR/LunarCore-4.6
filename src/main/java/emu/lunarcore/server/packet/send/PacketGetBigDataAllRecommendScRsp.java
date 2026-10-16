package emu.lunarcore.server.packet.send;

import emu.lunarcore.proto.BigDataRecommendTypeOuterClass.BigDataRecommendType;
import emu.lunarcore.proto.GetBigDataAllRecommendScRspOuterClass.GetBigDataAllRecommendScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketGetBigDataAllRecommendScRsp extends BasePacket {

    public PacketGetBigDataAllRecommendScRsp(BigDataRecommendType type) {
        super(CmdId.GetBigDataAllRecommendScRsp);

        var data = GetBigDataAllRecommendScRsp.newInstance()
                .setRecommendType(type);
        
        // 4.6 has no recommend-data sub messages in this reply
        
        this.setData(data);
    }
}
