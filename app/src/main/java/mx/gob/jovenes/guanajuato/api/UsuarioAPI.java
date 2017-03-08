package mx.gob.jovenes.guanajuato.api;

import mx.gob.jovenes.guanajuato.model.Usuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by code on 7/03/17.
 */

public interface UsuarioAPI {

    @POST("usuarios/login")
    Call<Response<Usuario>> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("usuarios/registrar")
    Call<Response<Usuario>> registrar(
            @Query("email") String email,
            @Query("password") String password,
            @Query("confirmar_password") String confirmarPassword,
            @Query("nombre") String nombre,
            @Query("id_genero") String idGenero,
            @Query("id_ocupacion") String idOcupacion,
            @Query("id_estado") String idEstado,
            @Query("id_municipio") String idMunicipio,
            @Query("fecha_nacimiento") String fechaNacimiento,
            @Query("codigo_postal") String codigoPostal,
            @Query("telefono") String telefono,
            @Query("curp") String curp
    );
}
