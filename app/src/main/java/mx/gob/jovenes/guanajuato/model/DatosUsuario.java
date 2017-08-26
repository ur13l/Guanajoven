package mx.gob.jovenes.guanajuato.model;

/**
 * Created by Uriel on 22/05/2017.
 */

public class DatosUsuario {
    private int idDatosUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Genero genero;
    private String fechaNacimiento;
    private Estado estadoNacimiento;
    private Estado estado;
    private int codigoPostal;
    private String telefono;
    private String curp;
    private Municipio municipio;
    private String rutaImagen;
    private NivelEstudios nivelEstudios;
    private PuebloIndigena puebloIndigena;
    private CapacidadDiferente capacidadDiferente;
    private String premios;
    private String proyectosSociales;
    private int apoyoProyectosSociales;

    public int getIdDatosUsuario() {
        return idDatosUsuario;
    }

    public void setIdDatosUsuario(int idDatosUsuario) {
        this.idDatosUsuario = idDatosUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Estado getEstadoNacimiento() {
        return estadoNacimiento;
    }

    public void setEstadoNacimiento(Estado estadoNacimiento) {
        this.estadoNacimiento = estadoNacimiento;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public NivelEstudios getNivelEstudios() {
        return nivelEstudios;
    }

    public void setNivelEstudios(NivelEstudios nivelEstudios) {
        this.nivelEstudios = nivelEstudios;
    }

    public PuebloIndigena getPuebloIndigena() {
        return puebloIndigena;
    }

    public void setPuebloIndigena(PuebloIndigena puebloIndigena) {
        this.puebloIndigena = puebloIndigena;
    }

    public CapacidadDiferente getCapacidadDiferente() {
        return capacidadDiferente;
    }

    public void setCapacidadDiferente(CapacidadDiferente capacidadDiferente) {
        this.capacidadDiferente = capacidadDiferente;
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

    public int isApoyoProyectosSociales() {
        return apoyoProyectosSociales;
    }

    public void setApoyoProyectosSociales(int apoyoProyectosSociales) {
        this.apoyoProyectosSociales = apoyoProyectosSociales;
    }
}
