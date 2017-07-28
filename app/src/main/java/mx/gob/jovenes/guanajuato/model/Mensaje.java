package mx.gob.jovenes.guanajuato.model;

/**
 * Created by codigus on 27/07/2017.
 */

public class Mensaje {
    private int idMensaje;
    private int idChat;
    private String mensaje;
    private int enviaUsuario;
    private int visto;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public Mensaje(String mensaje, int enviaUsuario) {
        this.mensaje = mensaje;
        this.enviaUsuario = enviaUsuario;
    }

    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public int getIdChat() {
        return idChat;
    }

    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int isEnviaUsuario() {
        return enviaUsuario;
    }

    public void setEnviaUsuario(int enviaUsuario) {
        this.enviaUsuario = enviaUsuario;
    }

    public int isVisto() {
        return visto;
    }

    public void setVisto(int visto) {
        this.visto = visto;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
}
