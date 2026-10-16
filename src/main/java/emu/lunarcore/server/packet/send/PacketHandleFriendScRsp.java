package emu.lunarcore.server.packet.send;

import emu.lunarcore.game.player.Player;
import emu.lunarcore.proto.FriendListInfoOuterClass.FriendListInfo;
import emu.lunarcore.proto.HandleFriendScRspOuterClass.HandleFriendScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Retcode;

public class PacketHandleFriendScRsp extends BasePacket {

    public PacketHandleFriendScRsp(Player friend, boolean result) {
        super(CmdId.HandleFriendScRsp);

        var data = HandleFriendScRsp.newInstance()
                .setUid(friend.getUid())
                .setHandleResult(result);

        if (result) {
            data.setHandleFriendInfo(FriendListInfo.newInstance().setSimpleInfo(friend.toSimpleInfo()));
        }

        this.setData(data);
    }

    public PacketHandleFriendScRsp(int uid, Retcode retcode) {
        super(CmdId.HandleFriendScRsp);

        var data = HandleFriendScRsp.newInstance()
                .setUid(uid)
                .setRetcode(retcode.getVal());

        this.setData(data);
    }
}
