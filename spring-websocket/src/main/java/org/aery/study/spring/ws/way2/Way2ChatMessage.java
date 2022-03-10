package org.aery.study.spring.ws.way2;

public class Way2ChatMessage {

    public enum ChatType {
        CHAT, JOIN, LEAVE
    }

    /**
     * 訊息種類
     */
    private ChatType type;

    /**
     * 訊息發送者的名稱
     */
    private String sender;

    /**
     * 訊息內容
     */
    private String content;

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
