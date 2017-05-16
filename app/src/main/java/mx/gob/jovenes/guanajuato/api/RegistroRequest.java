package mx.gob.jovenes.guanajuato.api;

/**
 * Created by Uriel on 16/03/2017.
 */

public class RegistroRequest {

    private String curp;
    private String email;
    private String password;
    private String confirmarPassword;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombre;
    private String genero;
    private String fechaNacimiento;
    private String codigoPostal;
    private String estadoNacimiento;
    private String rutaImagen;
    private String idGoogle;
    private String idFacebook;


    public RegistroRequest(String curp, String email, String password, String confirmarPassword,
                           String apellidoPaterno, String apellidoMaterno, String nombre,
                           String genero, String fechaNacimiento, String codigoPostal,
                           String estadoNacimiento, String rutaImagen, String idGoogle,
                           String idFacebook) {
        this.curp = curp;
        this.email = email;
        this.password = password;
        this.confirmarPassword = confirmarPassword;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombre = nombre;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.codigoPostal = codigoPostal;
        this.estadoNacimiento = estadoNacimiento;
        this.rutaImagen = rutaImagen;
        this.idGoogle = idGoogle;
        this.idFacebook = idFacebook;
    }
}
