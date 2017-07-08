package mx.gob.jovenes.guanajuato.api;

import mx.gob.jovenes.guanajuato.model.DatosModificarPerfil;
import mx.gob.jovenes.guanajuato.model.DatosUsuarioIdioma;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by codigus on 04/07/2017.
 */

public interface RegistroModificarPerfil {

    @POST("profile/update")
    Call<Response<Boolean>> postModificarPerfil (
            @Query("api_token") String apiToken,
            @Query("id_nivel_estudios") int idNivelEstudios,
            @Query("id_programa_beneficiario") int idProgramaBeneficiario,
            @Query("trabajo") int trabajo,
            @Query("id_pueblo_indigena") int idPuebloIndigena,
            @Query("id_capacidad_diferente") int idCapacidadDiferente,
            @Query("premios") String premios,
            @Query("proyectos_sociales") String proyectosSociales,
            @Query("apoyo_proyectos_sociales") int apoyoProyectosSociales,
            @Query("idiomas") DatosUsuarioIdioma[] idiomas);

    @POST("profile/get")
    Call<Response<DatosModificarPerfil>> getModificarPerfil (
            @Query("api_token") String apiToken
    );

}
