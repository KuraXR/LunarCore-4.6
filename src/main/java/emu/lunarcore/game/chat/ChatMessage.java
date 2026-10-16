package emu.lunarcore.game.chat;

import java.util.concurrent.atomic.AtomicLong;

import emu.lunarcore.LunarCore;
import emu.lunarcore.proto.ChatContentInfoOuterClass.ChatContentInfo;
import emu.lunarcore.proto.ChatInfoOuterClass.ChatInfo;
import emu.lunarcore.proto.MsgTypeOuterClass.MsgType;
import lombok.Getter;

@Getter
public class ChatMessage {
    // The 4.6 client keeps one history message per timestamp, so every message gets its own
    private static final AtomicLong lastTime = new AtomicLong();

    private int fromUid;
    private int toUid;
    private String text;
    private int emote;
    private long time;

    public ChatMessage(int fromUid, int toUid) {
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.time = lastTime.updateAndGet(last -> Math.max(last + 1, System.currentTimeMillis()));
    }
    
    public ChatMessage(int fromUid, int toUid, String text) {
        this(fromUid, toUid);
        this.text = text;
    }
    
    public ChatMessage(int fromUid, int toUid, int emote) {
        this(fromUid, toUid);
        this.emote = emote;
    }

    public MsgType getType() {
        return this.getText() != null ? MsgType.MSG_TYPE_CUSTOM_TEXT : MsgType.MSG_TYPE_EMOJI;
    }

    public ChatInfo toProto() {
        // 4.6 sends the time in milliseconds; history drops messages with times in seconds
        var proto = ChatInfo.newInstance()
                .setSentTime(LunarCore.convertToServerTime(this.getTime()));

        // 4.6 replaced the sender header with sender/receiver sides; players are kind 1 with their
        // uid as role_id (verified in game)
        proto.getMutableSender()
                .setKind(1)
                .setRoleId(this.getFromUid());
        proto.getMutableReceiver()
                .setKind(1)
                .setRoleId(this.getToUid());

        var content = ChatContentInfo.newInstance()
                .setMsgType(this.getType());

        if (this.getText() != null) {
            content.getMutableChatMsg().setChatText(this.getText());
        } else {
            content.getMutableChatMsg().setEmoteId(this.getEmote());
        }

        proto.addChatContentList(content);
        
        return proto;
    }
}
