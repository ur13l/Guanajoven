package mx.gob.jovenes.guanajuato.notifications;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by code on 7/06/16.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    public static final String TOKEN = "FirebaseInstanceIdService_TOKEN";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString(TOKEN, token).commit();
    }

}
