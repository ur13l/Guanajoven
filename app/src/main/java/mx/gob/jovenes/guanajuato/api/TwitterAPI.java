package mx.gob.jovenes.guanajuato.api;

import mx.gob.jovenes.guanajuato.model.TwitterResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by codigus on 26/05/2017.
 */

public interface TwitterAPI {

    @GET("tweets")
    Call<TwitterResponse> get (
            @Query("screen_name") String screenName
    );

}
