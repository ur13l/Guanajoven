package mx.gob.jovenes.guanajuato.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by codigus on 26/05/2017.
 */

public class Tweet  {
    @SerializedName("id_str")
    private String idStr;
    @SerializedName("text")
    private String text;
    @SerializedName("user")
    private UsuarioTweet user;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UsuarioTweet getUser() {
        return user;
    }

    public void setUser(UsuarioTweet user) {
        this.user = user;
    }
}
