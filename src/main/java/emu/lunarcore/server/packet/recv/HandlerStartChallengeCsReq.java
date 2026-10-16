package emu.lunarcore.server.packet.recv;

import emu.lunarcore.proto.ChallengeLineupAvatarOuterClass.ChallengeLineupAvatar;
import emu.lunarcore.proto.StartChallengeCsReqOuterClass.StartChallengeCsReq;
import emu.lunarcore.server.game.GameSession;
import emu.lunarcore.server.packet.CmdId;
import emu.lunarcore.server.packet.Opcodes;
import emu.lunarcore.server.packet.PacketHandler;
import us.hebi.quickbuf.RepeatedInt;
import us.hebi.quickbuf.RepeatedMessage;

@Opcodes(CmdId.StartChallengeCsReq)
public class HandlerStartChallengeCsReq extends PacketHandler {

    @Override
    public void handle(GameSession session, byte[] data) throws Exception {
        var req = StartChallengeCsReq.parseFrom(data);

        // Parse buff ids
        int firstHalfBuff = 0;
        int secondHalfBuff = 0;

        if (req.getMutableExtInfo().hasNewStoryBuffInfo()) {
            var storyBuffs = req.getMutableExtInfo().getMutableNewStoryBuffInfo();
            firstHalfBuff = storyBuffs.getFirstHalf();
            secondHalfBuff = storyBuffs.getSecondHalf();
        } else if (req.getMutableExtInfo().hasStoryBuffInfo()) {
            var storyBuffs = req.getMutableExtInfo().getMutableStoryBuffInfo();
            firstHalfBuff = storyBuffs.getBuffOne();
            secondHalfBuff = storyBuffs.getBuffTwo();
        } else if (req.getMutableExtInfo().hasBossBuffInfo()) {
            var bossBuffs = req.getMutableExtInfo().getMutableBossBuffInfo();
            firstHalfBuff = bossBuffs.getBuffOne();
            secondHalfBuff = bossBuffs.getBuffTwo();
        }

        // 4.6 sends the teams as avatar lists; the old id lists stay empty
        var lineup1 = toIds(req.getFirstHalfLineup(), req.getAvatarLineupFirst());
        var lineup2 = toIds(req.getSecondHalfLineup(), req.getAvatarLineupSecond());

        // Start challenge
        session.getPlayer().getChallengeManager().startChallenge(req.getChallengeId(), lineup1, lineup2, firstHalfBuff, secondHalfBuff);
    }

    private static RepeatedInt toIds(RepeatedInt ids, RepeatedMessage<ChallengeLineupAvatar> avatars) {
        if (ids.length() > 0 || avatars.length() == 0) {
            return ids;
        }

        var result = RepeatedInt.newEmptyInstance();
        for (int i = 0; i < avatars.length(); i++) {
            result.add(avatars.get(i).getId());
        }
        return result;
    }

}
