package emu.lunarcore.server.packet.send;

import java.util.Collection;

import emu.lunarcore.game.inventory.GameItem;
import emu.lunarcore.proto.AvatarQuickLevelUpScRspOuterClass.AvatarQuickLevelUpScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketAvatarQuickLevelUpScRsp extends BasePacket {

    public PacketAvatarQuickLevelUpScRsp(Collection<GameItem> returnItems) {
        super(CmdId.PMEMIDKDAFG);

        var data = AvatarQuickLevelUpScRsp.newInstance();

        if (returnItems != null) {
            for (GameItem item : returnItems) {
                data.addReturnItemList(item.toPileProto());
            }
        } else {
            data.setRetcode(1);
        }

        this.setData(data);
    }
}
