package mx.gob.jovenes.guanajuato.utils;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import mx.gob.jovenes.guanajuato.Funcion;

/**
 * Created by Uriel on 24/04/2017.
 */

public class SlideHandler {
    private static float startPointX;
    private static float startPointY;
    private static int actionType;
    private static final int ACTION_TYPE_DEFAULT = 0;
    private static final int ACTION_TYPE_UP = 1;
    private static final int ACTION_TYPE_RIGHT = 2;
    private static final int ACTION_TYPE_DOWN = 3;
    private static final int ACTION_TYPE_LEFT = 4;
    private static final int SLIDE_RANGE = 100;

    public static void initSlider(View view, final String direccion, final Funcion f) {
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startPointX = event.getRawX();
                        startPointY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (startPointX - x > SLIDE_RANGE) {
                            actionType = ACTION_TYPE_LEFT;
                        } else if (x - startPointX > SLIDE_RANGE) {
                            actionType = ACTION_TYPE_RIGHT;
                        } else if (startPointY - y > SLIDE_RANGE) {
                            actionType = ACTION_TYPE_UP;
                        } else if (y - startPointY > SLIDE_RANGE) {
                            actionType = ACTION_TYPE_DOWN;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (actionType == ACTION_TYPE_UP) {
                            //Acción arriba
                        } else if (actionType == ACTION_TYPE_RIGHT  && direccion.equals("right")) {
                            f.exec();
                        } else if (actionType == ACTION_TYPE_DOWN) {
                            //Izquierda
                        } else if (actionType == ACTION_TYPE_LEFT && direccion.equals("left")) {
                           f.exec();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
