package mx.gob.jovenes.guanajuato.model;

/**
 * Created by codigus on 27/07/2017.
 */

public class Mensaje {
    private int idMensaje;
    private int idChat;
    private String mensaje;
    private boolean enviaUsuario;
    private boolean visto;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public Mensaje(String mensaje, boolean enviaUsuario) {
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

    public boolean isEnviaUsuario() {
        return enviaUsuario;
    }

    public void setEnviaUsuario(boolean enviaUsuario) {
        this.enviaUsuario = enviaUsuario;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
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
