package code.guanajuato.gob.mx.activatecode.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import code.guanajuato.gob.mx.activatecode.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NuevoUsuarioFragment extends CustomFragment {
    private EditText txt_email, txt_password, txt_confpass, txt_user;
    private Button btnComenzar;
    private NestedScrollView Layout;
    private ImageView imgFacebook, imgTwitter;
    private boolean email, pass, confpass, user;
    private String opcion = "2";
    String validos = " abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWYZ0123456789";

    //valida correo
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    //valida pass
    public boolean soloLetrasYNum(String campo) {
        char letra;
        boolean bien = true;
        for (int i = 0; i < campo.length(); i++) {
            letra = campo.charAt(i);
            if (validos.indexOf(letra) == -1) {
                bien = false;
            }
            ;
        }
        return bien;
    }


    //Si están llenos los campos habilita el botón
    public void botonBloqueo() {
        if (email == true && pass == true  && confpass == true && user == true) {
            btnComenzar.setEnabled(true);
        } else {
            btnComenzar.setEnabled(false);
        }
    }

    public NuevoUsuarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nuevo_usuario, container, false);
        txt_email = (EditText) v.findViewById(R.id.email_inputnuevousuario);
        txt_password = (EditText) v.findViewById(R.id.pass_inputnuevousuario);
        txt_confpass = (EditText) v.findViewById(R.id.confpass_inputnuevousuario);
        btnComenzar = (Button) v.findViewById(R.id.comenzar_btnnuevousuario);
        txt_user = (EditText) v.findViewById(R.id.user_inputnuevousuario);
        imgFacebook = (ImageView) v.findViewById(R.id.imageFacebook);
        imgTwitter = (ImageView) v.findViewById(R.id.imageTwitter);
        AppCompatActivity appcom = (AppCompatActivity) getActivity();
        appcom.getSupportActionBar().setDisplayShowHomeEnabled(true);
        appcom.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appcom.getSupportActionBar().setHomeButtonEnabled(true);
        appcom.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        Layout  = (NestedScrollView) appcom.findViewById(R.id.login_nested);
        Layout.setBackgroundColor(Color.rgb(237,237,237));

/*

        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_email.getText().toString().isEmpty() || txt_password.getText().toString().isEmpty() ||  txt_confpass.getText().toString().isEmpty() || txt_user.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Verifica que no existan campos vacios", Toast.LENGTH_LONG).show();
                } else {
                    new GetData().execute(txt_user.getText().toString(),txt_email.getText().toString(), txt_password.getText().toString(),opcion);
                }

            }
        });
*/

        txt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txt_email.setHint("you@email.com");
                } else {
                    txt_email.setHint("");
                    if (txt_email.getText().toString().equalsIgnoreCase("")) {
                        txt_email.setHintTextColor(Color.RED);
                        email = false;
                        botonBloqueo();
                    } else {
                        if (isValidEmail(txt_email.getText().toString())) {
                            email = true;
                            botonBloqueo();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Correo no valido", Toast.LENGTH_LONG).show();
                            email = false;
                            botonBloqueo();
                        }

                    }
                }
            }
        });





        txt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txt_password.setHint("Mínimo 6 caracteres");
                } else {
                    txt_password.setHint("");
                    if (txt_password.getText().toString().equalsIgnoreCase("")) {
                        txt_password.setHintTextColor(Color.RED);
                        pass = false;
                        botonBloqueo();
                    } else {
                        if (soloLetrasYNum(txt_password.getText().toString()) && txt_password.getText().length() >= 6) {
                            pass = true;
                            botonBloqueo();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Contraseña no valida, evita caracteres especiales", Toast.LENGTH_LONG).show();
                            pass = false;
                            botonBloqueo();
                        }

                    }
                }
            }
        });


        txt_confpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txt_confpass.setHint("Repite la contraseña");
                } else {
                    txt_confpass.setHint("");
                    if(txt_confpass.getText().toString().equalsIgnoreCase(txt_password.getText().toString())){
                        confpass = true;
                        botonBloqueo();
                    }else{
                        confpass = false;
                        botonBloqueo();
                        Toast.makeText(getActivity().getApplicationContext(), "La contraseña no coincide", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        txt_user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txt_user.setHint("Mínimo 6 caracteres");
                } else {
                    txt_user.setHint("");
                    if (txt_user.getText().toString().equalsIgnoreCase("")) {
                        txt_user.setHintTextColor(Color.RED);
                        user = false;
                        botonBloqueo();
                    } else {
                        if (soloLetrasYNum(txt_user.getText().toString()) && txt_user.getText().length() >= 6) {
                            user = true;
                            botonBloqueo();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Usuario no valido, evita caracteres especiales", Toast.LENGTH_LONG).show();
                            user = false;
                            botonBloqueo();
                        }

                    }
                }
            }
        });


        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/pekesaludcom-256207354579322/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.addCategory(Intent.CATEGORY_BROWSABLE);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });


        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/PekeSalud";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.addCategory(Intent.CATEGORY_BROWSABLE);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });






        return v;
    }


/*

    //Conexión Servlet-Base de datos
    private class GetData extends AsyncTask<String, Void, Login> {


        @Override
        protected Login doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
            params.put("nickname", args[0].toString());
            params.put("email", args[1].toString());
            params.put("password", args[2].toString());
            params.put("opcion", args[3].toString());
            String url = "http://10.0.3.2:8080/PekeSalud/ServletEjemplo";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, params);
            Gson gson = new Gson();
            Log.d("Ramon", result + "");
            return gson.fromJson(result, Login.class);
        }

        @Override
        public void onPostExecute(Login result) {
            if (result == null || result.getEmail() == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Email o Contraseña Incorrectos, intenta nuevamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Prueba funciona", Toast.LENGTH_LONG).show();
                //Entra al sistema SERVLET
            }
        }
    }
    */


}



