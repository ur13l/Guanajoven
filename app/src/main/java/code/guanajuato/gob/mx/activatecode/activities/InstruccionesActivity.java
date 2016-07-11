package code.guanajuato.gob.mx.activatecode.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import code.guanajuato.gob.mx.activatecode.R;

/**
 * Created by code on 11/07/16.
 */
public class InstruccionesActivity extends Activity implements View.OnClickListener{
    private View background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);

        background = findViewById(R.id.background);

        background.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.finish();
    }
}
