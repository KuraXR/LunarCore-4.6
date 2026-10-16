package emu.lunarcore.server.packet.recv;

import java.util.ArrayList;
import java.util.List;

import emu.lunarcore.data.common.ItemParam;
import emu.lunarcore.proto.EquipmentQuickUpCsReqOuterClass.EquipmentQuickUpCsReq;
import emu.lunarcore.proto.ItemCostOuterClass.ItemCost;
import emu.lunarcore.server.game.GameSession;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Opcodes;
import emu.lunarcore.server.packet.PacketHandler;
import emu.lunarcore.server.packet.send.PacketEquipmentQuickUpScRsp;

// 4.6 one-step light cone level up and ascension
@Opcodes(CmdId.EquipmentQuickUpCsReq)
public class HandlerEquipmentQuickUpCsReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] data) throws Exception {
        var req = EquipmentQuickUpCsReq.parseFrom(data);

        // Ascension costs are worked out on the server, so only the exp items are read
        List<ItemParam> items = new ArrayList<>(req.getExpItemCostList().getItemList().length());
        for (ItemCost cost : req.getExpItemCostList().getItemList()) {
            items.add(new ItemParam(cost));
        }

        var returnItems = session.getServer().getInventoryService().quickLevelUpEquipment(
                session.getPlayer(),
                req.getEquipmentUniqueId(),
                req.getTargetLevel(),
                req.getTargetPromotion(),
                items
        );

        session.send(new PacketEquipmentQuickUpScRsp(returnItems));
    }

}
