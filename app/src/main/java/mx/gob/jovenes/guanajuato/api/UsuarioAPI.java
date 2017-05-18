package mx.gob.jovenes.guanajuato.api;

import org.json.JSONObject;

import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.model.models_tmp.Curp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by code on 7/03/17.
 */

public interface UsuarioAPI {

    /**
     * Login de usuario común
     * @param email
     * @param password
     * @return
     */
    @POST("usuarios/login")
    Call<Response<Usuario>> login(
            @Query("email") String email,
            @Query("password") String password
    );

    /**
     * Registro de usuarios
     * @param request
     * @return
     */
    @POST("usuarios/registrar")
    Call<Response<Usuario>> registrar(
            @Body Object request
    );

    /**
     * Verifica si el correo ya está registrado en la BD
     * @param email
     * @return
     */
    @POST("usuarios/verificarcorreo")
    Call<Response<Boolean>> verificarCorreo(
            @Query("email") String email
    );

    /**
     * Funcionalidad para logueo utilizando cuenta de Google.
     * @param email
     * @param idGoogle
     * @return
     */
    @POST("usuarios/logingoogle")
    Call<Response<Usuario>> loginGoogle(
            @Query("email") String email,
            @Query("id_google") String idGoogle
    );


    /**
     * Funcionalidad para logueo utilizando cuenta de Facebook
     * @param email
     * @param idFacebook
     * @return
     */
    @POST("usuarios/loginfacebook")
    Call<Response<Usuario>> loginFacebook(
            @Query("email") String email,
            @Query("id_facebook") String idFacebook
    );


    /**
     * Método para recuperación de password a partir de un email.
     * @param email
     * @return
     */
    @POST("password/email")
    Call<Response<Boolean>> recuperarPassword(
            @Query("email") String email
    );

    /**
     * Método para consultar datos de una persona con su CURP
     * @param curp
     * @return
     */
    @POST("usuarios/curp")
    Call<Response<Curp>> consultarCurp(
            @Query("curp") String curp
    );


    /**
     * Método para actualizar el token Guanajoven del código QR
     * @param apiToken
     * @return
     */
    @POST("usuarios/actualizar-token-guanajoven")
    Call<Response<String>> actualizarTokenGuanajoven(
            @Query("api_token") String apiToken
    );
}
