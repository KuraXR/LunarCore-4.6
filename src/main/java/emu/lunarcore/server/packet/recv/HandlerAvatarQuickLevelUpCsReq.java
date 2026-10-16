package emu.lunarcore.server.packet.recv;

import java.util.ArrayList;
import java.util.List;

import emu.lunarcore.data.common.ItemParam;
import emu.lunarcore.proto.AvatarQuickLevelUpCsReqOuterClass.AvatarQuickLevelUpCsReq;
import emu.lunarcore.proto.ItemCostOuterClass.ItemCost;
import emu.lunarcore.server.game.GameSession;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Opcodes;
import emu.lunarcore.server.packet.PacketHandler;
import emu.lunarcore.server.packet.send.PacketAvatarQuickLevelUpScRsp;

// 4.6 one-step level up and ascension; the client names are obfuscated
@Opcodes(CmdId.EKLIDIDKFHC)
public class HandlerAvatarQuickLevelUpCsReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] data) throws Exception {
        var req = AvatarQuickLevelUpCsReq.parseFrom(data);

        // Ascension costs are worked out on the server, so only the exp items are read
        List<ItemParam> items = new ArrayList<>(req.getExpItemCostList().getItemList().length());
        for (ItemCost cost : req.getExpItemCostList().getItemList()) {
            items.add(new ItemParam(cost));
        }

        var returnItems = session.getServer().getInventoryService().quickLevelUpAvatar(
                session.getPlayer(),
                req.getBaseAvatarId(),
                req.getTargetLevel(),
                req.getTargetPromotion(),
                items
        );

        session.send(new PacketAvatarQuickLevelUpScRsp(returnItems));
    }

}
