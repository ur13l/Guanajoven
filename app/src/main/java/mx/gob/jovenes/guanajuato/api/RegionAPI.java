package mx.gob.jovenes.guanajuato.api;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Region;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by esva on 25/04/17.
 */

public interface RegionAPI {

    @GET("regiones")

    Call<Response<ArrayList<Region>>> get(
            @Query("timestamp") long timestamp
    );
}
