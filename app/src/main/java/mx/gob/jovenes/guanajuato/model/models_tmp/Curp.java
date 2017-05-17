package mx.gob.jovenes.guanajuato.model.models_tmp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Uriel on 16/05/2017.
 */

public class Curp {

    @SerializedName("statusOper")
    private String statusOper;
    private String message;
    private String nombres;
    private String sexo;

    @SerializedName("fechNac")
    private String fechNac;
    private String nacionalidad;

    @SerializedName("anioReg")
    private String anioReg;

    @SerializedName("cveEntidadNac")
    private String cveEntidadNac;


    @SerializedName("cveMunicipioReg")
    private String cveMunicipioReg;


    @SerializedName("statusCurp")
    private String statusCurp;


    @SerializedName("CurpRenapo")
    private String CurpRenapo;


    @SerializedName("PrimerApellido")
    private String PrimerApellido;


    @SerializedName("SegundoApellido")
    private String SegundoApellido;

    public String getStatusOper() {
        return statusOper;
    }

    public void setStatusOper(String statusOper) {
        this.statusOper = statusOper;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechNac() {
        return fechNac;
    }

    public void setFechNac(String fechNac) {
        this.fechNac = fechNac;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getAnioReg() {
        return anioReg;
    }

    public void setAnioReg(String anioReg) {
        this.anioReg = anioReg;
    }

    public String getCveEntidadNac() {
        return cveEntidadNac;
    }

    public void setCveEntidadNac(String cveEntidadNac) {
        this.cveEntidadNac = cveEntidadNac;
    }

    public String getCveMunicipioReg() {
        return cveMunicipioReg;
    }

    public void setCveMunicipioReg(String cveMunicipioReg) {
        this.cveMunicipioReg = cveMunicipioReg;
    }

    public String getStatusCurp() {
        return statusCurp;
    }

    public void setStatusCurp(String statusCurp) {
        this.statusCurp = statusCurp;
    }

    public String getCurpRenapo() {
        return CurpRenapo;
    }

    public void setCurpRenapo(String curpRenapo) {
        CurpRenapo = curpRenapo;
    }

    public String getPrimerApellido() {
        return PrimerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        PrimerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return SegundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        SegundoApellido = segundoApellido;
    }
}
