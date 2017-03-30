package mx.gob.jovenes.guanajuato.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by code on 26/04/16.
 */
public class OKDialog {

    public static void showOKDialog(Context c, String title, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(c);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    public static void showOKDialogAction(Context c, String title, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(c);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("OK", listener);
        alert.show();
    }
}
