package mx.gob.jovenes.guanajuato.application;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by code on 6/03/17.
 */

public class MyApplication extends MultiDexApplication {
    Retrofit retrofit;
    public static final String BASE_URL = "http://10.0.7.121/GuanajovenWeb/public/api/";

    @Override
    public void onCreate() {
        super.onCreate();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("d/M/yyyy")
                .create();
         retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


    }

    public Retrofit getRetrofitInstance(){
        return retrofit;
    }
}
