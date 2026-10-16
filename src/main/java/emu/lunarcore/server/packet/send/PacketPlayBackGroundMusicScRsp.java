package emu.lunarcore.server.packet.send;

import emu.lunarcore.proto.PlayBackGroundMusicScRspOuterClass.PlayBackGroundMusicScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketPlayBackGroundMusicScRsp extends BasePacket {

    public PacketPlayBackGroundMusicScRsp(int musicId) {
        super(CmdId.PlayBackGroundMusicScRsp);

        var data = PlayBackGroundMusicScRsp.newInstance();
        data.getMutablePlayState().getMutableCurrent().setId(musicId);

        this.setData(data);
    }
}
