package mx.gob.jovenes.guanajuato.api;

import mx.gob.jovenes.guanajuato.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
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
            @Body Object request
    );



    @POST("usuarios/verificarcorreo")
    Call<Response<Boolean>> verificarCorreo(
            @Query("email") String email
    );

    @POST("usuarios/logingoogle")
    Call<Response<Usuario>> loginGoogle(
            @Query("email") String email,
            @Query("id_google") String idGoogle
    );


    @POST("usuarios/loginfacebook")
    Call<Response<Usuario>> loginFacebook(
            @Query("email") String email,
            @Query("id_facebook") String idFacebook
    );


    @POST("password/email")
    Call<Response<Boolean>> recuperarPassword(
            @Query("email") String email
    );
}
