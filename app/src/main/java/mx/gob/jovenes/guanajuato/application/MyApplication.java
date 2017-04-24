package mx.gob.jovenes.guanajuato.application;

import android.app.Application;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by code on 6/03/17.
 */

public class MyApplication extends MultiDexApplication {
    Retrofit retrofit;
    public static String LAST_UPDATE_CONVOCATORIAS = "last_update_convocatorias";

    //dirección publica
    //public static final String BASE_URL = "http://200.23.39.11/GuanajovenWeb/public/api/";

    //uriel publica
    //public static final String BASE_URL = "http://192.168.0.93/GuanajovenWeb/public/api/";

    //dirección uriel
    public static final String BASE_URL = "http://10.0.7.121/GuanajovenWeb/public/api/";

    //dirección local
    //public static final String BASE_URL = "http://10.0.7.40/GuanajovenWeb/public/api/";

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

        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().name("guanajoven").directory(new File(rootPath + "/realm/")).build();
        Realm.setDefaultConfiguration(realmConfig);


    }

    public Retrofit getRetrofitInstance(){
        return retrofit;
    }
}
