package code.guanajuato.gob.mx.activatecode.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by code on 27/06/16.
 */
public class ConnectionUtilities {

    public static boolean hasWIFIConnection(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network.getType() == ConnectivityManager.TYPE_WIFI){
            return true;
        }
        return false;

    }
}
