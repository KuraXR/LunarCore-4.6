package emu.lunarcore.server.packet.send;

import java.util.Collection;

import emu.lunarcore.game.inventory.GameItem;
import emu.lunarcore.proto.EquipmentQuickUpScRspOuterClass.EquipmentQuickUpScRsp;
import emu.lunarcore.server.packet.BasePacket;
import emu.lunarcore.server.packet.CmdId;

public class PacketEquipmentQuickUpScRsp extends BasePacket {

    public PacketEquipmentQuickUpScRsp(Collection<GameItem> returnItems) {
        super(CmdId.EquipmentQuickUpScRsp);

        var data = EquipmentQuickUpScRsp.newInstance();

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
