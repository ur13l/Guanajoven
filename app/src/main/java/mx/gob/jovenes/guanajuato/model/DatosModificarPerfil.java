package mx.gob.jovenes.guanajuato.model;

import java.util.List;

/**
 * Created by codigus on 04/07/2017.
 */

public class DatosModificarPerfil {
    private String apiToken;
    private int idNivelEstudios;
    private int idProgramaBeneficiario;
    private int trabajo;
    private int idPuebloIndigena;
    private int idCapacidadDiferente;
    private String premios;
    private String proyectosSociales;
    private int apoyoProyectosSociales;
    private List<DatosUsuarioIdioma> idiomas;

    public DatosModificarPerfil(String apiToken, int idNivelEstudios, int idPuebloIndigena, int idCapacidadDiferente, String premios, String proyectosSociales, int apoyoProyectosSociales, List<DatosUsuarioIdioma> idiomas) {
        this.apiToken = apiToken;
        this.idNivelEstudios = idNivelEstudios;
        this.idPuebloIndigena = idPuebloIndigena;
        this.idCapacidadDiferente = idCapacidadDiferente;
        this.premios = premios;
        this.proyectosSociales = proyectosSociales;
        this.apoyoProyectosSociales = apoyoProyectosSociales;
        this.idiomas = idiomas;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public int getIdNivelEstudios() {
        return idNivelEstudios;
    }

    public void setIdNivelEstudios(int idNivelEstudios) {
        this.idNivelEstudios = idNivelEstudios;
    }

    public int getIdProgramaGobierno() {
        return idProgramaBeneficiario;
    }

    public void setIdProgramaGobierno(int idProgramaGobierno) {
        this.idProgramaBeneficiario = idProgramaGobierno;
    }

    public int getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(int trabajo) {
        this.trabajo = trabajo;
    }

    public int getIdPuebloIndigena() {
        return idPuebloIndigena;
    }

    public void setIdPuebloIndigena(int idPuebloIndigena) {
        this.idPuebloIndigena = idPuebloIndigena;
    }

    public int getIdCapacidadDiferente() {
        return idCapacidadDiferente;
    }

    public void setIdCapacidadDiferente(int idCapacidadDiferente) {
        this.idCapacidadDiferente = idCapacidadDiferente;
    }

    public String getPremios() {
        return premios;
    }

    public void setPremios(String premios) {
        this.premios = premios;
    }

    public String getProyectosSociales() {
        return proyectosSociales;
    }

    public void setProyectosSociales(String proyectosSociales) {
        this.proyectosSociales = proyectosSociales;
    }

    public int getApoyoProyectosSociales() {
        return apoyoProyectosSociales;
    }

    public void setApoyoProyectosSociales(int apoyoProyectosSociales) {
        this.apoyoProyectosSociales = apoyoProyectosSociales;
    }

    public List<DatosUsuarioIdioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<DatosUsuarioIdioma> idiomas) {
        this.idiomas = idiomas;
    }
}
