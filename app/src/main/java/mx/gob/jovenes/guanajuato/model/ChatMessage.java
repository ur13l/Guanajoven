package mx.gob.jovenes.guanajuato.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by codigus on 26/06/2017.
 */
//No se incluira en el json que se envia a firebase el atributo de sendby me
@JsonIgnoreProperties({"sendByMe"})
public class ChatMessage {
    private String message;
    private String sender;
    private boolean sendByMe;

    public ChatMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isSendByMe() {
        return sendByMe;
    }

    public void setSendByMe(boolean sendByMe) {
        this.sendByMe = sendByMe;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;

        if (obj instanceof  ChatMessage) {
            ChatMessage msg = (ChatMessage) obj;
            equal = this.sender.equals(msg.getSender()) && this.message.equals(msg.getMessage()) && this.sendByMe == msg.sendByMe;
        }

        return equal;
    }
}
