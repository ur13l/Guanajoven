package mx.gob.jovenes.guanajuato.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVIdiomasSeleccionadosAdapter;
import mx.gob.jovenes.guanajuato.api.RegistroModificarPerfil;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.UsuarioAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.DatosModificarPerfil;
import mx.gob.jovenes.guanajuato.model.DatosUsuarioIdioma;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.EditTextValidations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.R.id.list;

/**
 * Autor: Uriel Infante
 * Fragment de datos complementarios del usuario, esta interfaz solicita información obligatoria
 * al usuario, y es llamada cuando se va a crear un nuevo usuario, sin importar su forma de logueo.
 * Fecha: 02/05/2016
 */
public class EditarDatosFragment extends CustomFragment {
    private static final String EMAIL = "email";
    private static final String NOMBRE = "nombre";
    private static final String AP_PATERNO = "ap_paterno";
    private static final String RUTA_IMAGEN = "ruta_imagen";

    private Button btnContinuar;
    private CircleImageView imgPerfil;

    private MaterialSpinner spnNivelEstudios;
    private MaterialSpinner spnConfirmProgramaGobierno;
    private MaterialSpinner spnProgramaGobierno;
    private MaterialSpinner spnTrabaja;
    private MaterialSpinner spnConfirmPuebloIndigena;
    private MaterialSpinner spnPuebloIndigena;
    private MaterialSpinner spnConfirmCapacidadDiferente;
    private MaterialSpinner spnCapacidadDiferente;
    private MaterialSpinner spnConfirmPremios;
    private EditText etPremios;
    private MaterialSpinner spnConfirmProyectosSociales;
    private EditText etProyectosSociales;
    private MaterialSpinner spnSueldoProyectosSociales;
    private ProgressDialog progressDialog;

    private Button btnSeleccionarIdiomas;

    private RegistroModificarPerfil registroModificarPerfilAPI;

    private String[] siNo = {"Sí", "No"};
    private String[] nivelesEstudio = {"Primaria", "Secundaria", "Preparatoria", "TSU", "Universidad", "Maestría", "Doctorado", "Otro"};
    private String[] programasGobierno = {"Municipal", "Estatal", "Federal", "Internacional"};
    private String[] pueblosIndigenas = {"Otomí", "Chichimeca-Jonaz", "Náhuatl", "Mazahua", "Otra"};
    private String[] capacidadesDiferentes = {"Física", "Sensorial", "Auditiva", "Visual", "Psíquica", "Intelectual", "Mental"};

    public static TextView textViewTituloIdiomasSeleccionados;
    public static LinearLayout layoutTablas;
    public static RecyclerView recyclerViewIdiomasSeleccionados;
    public static Activity thisActivity;
    public static RVIdiomasSeleccionadosAdapter adapter;

    private AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        registroModificarPerfilAPI = retrofit.create(RegistroModificarPerfil.class);
        thisActivity = getActivity();

        builder = new AlertDialog.Builder(getContext());
        builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editar_datos, parent, false);

        btnContinuar = (Button) v.findViewById(R.id.btn_continuar);
        spnNivelEstudios = (MaterialSpinner) v.findViewById(R.id.spn_nivel_estudios);
        spnConfirmProgramaGobierno = (MaterialSpinner) v.findViewById(R.id.spn_confirm_programa_gobierno);
        spnProgramaGobierno = (MaterialSpinner) v.findViewById(R.id.spn_programa_gobierno);
        spnTrabaja = (MaterialSpinner) v.findViewById(R.id.spn_trabaja);
        spnConfirmPuebloIndigena = (MaterialSpinner) v.findViewById(R.id.spn_confirm_pueblo_indigena);
        spnPuebloIndigena = (MaterialSpinner) v.findViewById(R.id.spn_pueblo_indigena);
        spnConfirmCapacidadDiferente = (MaterialSpinner) v.findViewById(R.id.spn_confirm_capacidad_diferente);
        spnCapacidadDiferente = (MaterialSpinner) v.findViewById(R.id.spn_capacidad_diferente);
        spnConfirmPremios = (MaterialSpinner) v.findViewById(R.id.spn_confirm_premios);
        spnConfirmProyectosSociales = (MaterialSpinner) v.findViewById(R.id.spn_confirm_proyectos_sociales);
        spnSueldoProyectosSociales = (MaterialSpinner) v.findViewById(R.id.spn_sueldo_proyectos_sociales);
        etPremios = (EditText) v.findViewById(R.id.et_premios);
        etProyectosSociales = (EditText) v.findViewById(R.id.et_proyecto_social);
        btnSeleccionarIdiomas = (Button) v.findViewById(R.id.btn_seleccionar_idiomas);

        imgPerfil = (CircleImageView) v.findViewById(R.id.imagen_usuario);
        ArrayAdapter<String> siNoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, siNo);
        ArrayAdapter<String> nivelEstudioAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nivelesEstudio);
        ArrayAdapter<String> programaGobiernoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, programasGobierno);
        ArrayAdapter<String> pueblosIndigenasAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, pueblosIndigenas);
        ArrayAdapter<String> capacidadesDiferentesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, capacidadesDiferentes);

        siNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nivelEstudioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        programaGobiernoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pueblosIndigenasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        capacidadesDiferentesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNivelEstudios.setAdapter(nivelEstudioAdapter);
        spnProgramaGobierno.setAdapter(programaGobiernoAdapter);
        spnPuebloIndigena.setAdapter(pueblosIndigenasAdapter);
        spnCapacidadDiferente.setAdapter(capacidadesDiferentesAdapter);
        spnConfirmProgramaGobierno.setAdapter(siNoAdapter);
        spnTrabaja.setAdapter(siNoAdapter);
        spnConfirmPuebloIndigena.setAdapter(siNoAdapter);
        spnConfirmCapacidadDiferente.setAdapter(siNoAdapter);
        spnConfirmPremios.setAdapter(siNoAdapter);
        spnConfirmProyectosSociales.setAdapter(siNoAdapter);
        spnSueldoProyectosSociales.setAdapter(siNoAdapter);

        EditTextValidations.dependencySpinners(spnConfirmProgramaGobierno, new View[]{spnProgramaGobierno});
        EditTextValidations.dependencySpinners(spnConfirmPuebloIndigena, new View[]{spnPuebloIndigena});
        EditTextValidations.dependencySpinners(spnConfirmCapacidadDiferente, new View[]{spnCapacidadDiferente});
        EditTextValidations.dependencySpinners(spnConfirmPremios, new View[]{etPremios});
        EditTextValidations.dependencySpinners(spnConfirmProyectosSociales, new View[]{etProyectosSociales, spnSueldoProyectosSociales});

        textViewTituloIdiomasSeleccionados = (TextView) v.findViewById(R.id.textview_titulo_idiomas_seleccionados);
        layoutTablas = (LinearLayout) v.findViewById(R.id.layout_tabla);
        recyclerViewIdiomasSeleccionados = (RecyclerView) v.findViewById(R.id.rv_idiomas_seleccionados);

        btnSeleccionarIdiomas.setOnClickListener((View) -> {
            /*if (IdiomasAdicionalesDialogFragment.numeroDeIdiomas() > 0) {
                //En caso de que ya halla puesto idiomas le saldra una alerta
                AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                alerta.setMessage("Tienes idiomas seleccionados, en caso de aceptar se eliminaran, ¿Estás de acuerdo?");

                alerta.setPositiveButton("Aceptar", (dialog, which) -> {
                    IdiomasAdicionalesDialogFragment.datosIdiomas.clear();
                    adapter.notifyDataSetChanged();

                    textViewTituloIdiomasSeleccionados.setVisibility(android.view.View.GONE);
                    layoutTablas.setVisibility(android.view.View.GONE);
                    recyclerViewIdiomasSeleccionados.setVisibility(android.view.View.GONE);

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    IdiomasAdicionalesDialogFragment idiomasAdicionalesDialogFragment = new IdiomasAdicionalesDialogFragment();
                    idiomasAdicionalesDialogFragment.show(fragmentManager, null);
                });

                alerta.show();

            } else {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                IdiomasAdicionalesDialogFragment idiomasAdicionalesDialogFragment = new IdiomasAdicionalesDialogFragment();
                idiomasAdicionalesDialogFragment.show(fragmentManager, null);
            }*/
            if (adapter.getListaIdiomas() != null) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                IdiomasAdicionalesDialogFragment idiomasAdicionalesDialogFragment = new IdiomasAdicionalesDialogFragment();
                idiomasAdicionalesDialogFragment.show(fragmentManager, null);
                IdiomasAdicionalesDialogFragment.quitarElementosDeAdapter(adapter.getListaIdiomas());
            } else {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                IdiomasAdicionalesDialogFragment idiomasAdicionalesDialogFragment = new IdiomasAdicionalesDialogFragment();
                idiomasAdicionalesDialogFragment.show(fragmentManager, null);
            }
        });

        btnContinuar.setOnClickListener((View -> {
            botonGuardar();
        }));

        Picasso.with(getActivity()).load(Sesion.getUsuario().getDatosUsuario().getRutaImagen()).into(imgPerfil);

        cargarDatos();

        return v;
    }

    //Limpia el arreglo estatico de idiomas
    @Override
    public void onStop() {
        super.onStop();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        //Limpia el arreglo de idiomas para que cuando vuelva a abrir el fragment se encuentre vacio
        if (IdiomasAdicionalesDialogFragment.datosIdiomas != null) {
            IdiomasAdicionalesDialogFragment.datosIdiomas.clear();
        }

    }

    //Cuando el usuario comience a ingresar idiomas o cuando los quite, actualiza la vista
    public static void validarIdiomas() {
        if (IdiomasAdicionalesDialogFragment.numeroDeIdiomas() > 0) {
            textViewTituloIdiomasSeleccionados.setVisibility(View.VISIBLE);
            layoutTablas.setVisibility(View.VISIBLE);
            recyclerViewIdiomasSeleccionados.setVisibility(View.VISIBLE);

            LinearLayoutManager llm = new LinearLayoutManager(thisActivity);
            adapter = new RVIdiomasSeleccionadosAdapter(thisActivity, IdiomasAdicionalesDialogFragment.datosIdiomas);
            recyclerViewIdiomasSeleccionados.setLayoutManager(llm);
            recyclerViewIdiomasSeleccionados.setAdapter(adapter);
            textViewTituloIdiomasSeleccionados.setVisibility(View.VISIBLE);
            recyclerViewIdiomasSeleccionados.setVisibility(View.VISIBLE);
        } else {
            textViewTituloIdiomasSeleccionados.setVisibility(View.GONE);
            layoutTablas.setVisibility(View.GONE);
            recyclerViewIdiomasSeleccionados.setVisibility(View.GONE);
        }
    }

    //Método para el botón de guardar
    public void botonGuardar() {
        if (datosCompletos()) {
            registrarDatos();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Ingresa todas las opciones");
            dialog.create();
            dialog.show();
        }
    }

    //Registra los datos en BD
    private void registrarDatos() {
        //Datos a registrar
        String apiToken = Sesion.getUsuario().getApiToken();
        int nivelEstudios = spnNivelEstudios.getSelectedItemPosition();
        int beneficiarioPrograma = spnProgramaGobierno.getSelectedItemPosition();
        int trabajo;

        if (spnTrabaja.getSelectedItemPosition() == 1) {
            trabajo = 1;
        } else {
            trabajo = 0;
        }

        int idPuebloIndigena = spnPuebloIndigena.getSelectedItemPosition();
        int idCapacidadDiferente = spnCapacidadDiferente.getSelectedItemPosition();
        String premios = etPremios.getText().toString();
        String proyectos = etProyectosSociales.getText().toString();
        int apoyoProyecto;

        if (spnSueldoProyectosSociales.getSelectedItemPosition() == 1) {
            apoyoProyecto = 1;
        } else {
            apoyoProyecto = 0;
        }

        DatosModificarPerfil datosModificarPerfil = new DatosModificarPerfil(apiToken, nivelEstudios, beneficiarioPrograma, trabajo, idPuebloIndigena, idCapacidadDiferente, premios, proyectos, apoyoProyecto, IdiomasAdicionalesDialogFragment.datosIdiomas);

        Call<Response<Boolean>> callRegistrar = registroModificarPerfilAPI.postModificarPerfil(datosModificarPerfil);

        callRegistrar.enqueue(new Callback<Response<Boolean>>() {
            @Override
            public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                builder.setMessage("Datos registrados");
                builder.show();
            }

            @Override
            public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                builder.setMessage("Error en registrar");
                builder.show();
            }
        });
    }

    //Cuando abre el fragment se ejecuta
    private void cargarDatos() {
        String apiToken = Sesion.getUsuario().getApiToken();
        Call<Response<DatosModificarPerfil>> cargarDatos = registroModificarPerfilAPI.getModificarPerfil(apiToken);

        cargarDatos.enqueue(new Callback<Response<DatosModificarPerfil>>() {
            @Override
            public void onResponse(Call<Response<DatosModificarPerfil>> call, retrofit2.Response<Response<DatosModificarPerfil>> response) {
                DatosModificarPerfil datosModificarPerfil = response.body().data;
                asignarDatos(datosModificarPerfil);
            }

            @Override
            public void onFailure(Call<Response<DatosModificarPerfil>> call, Throwable t) {

            }
        });

    }

    //Métodos para verificar que ingrese todos los datos
    private boolean datosCompletos() {
        if (confirmoNivelEstudios()) {
            if (confirmoBeneficiario()) {
                if (confirmoTrabajo()) {
                    if (confirmoPuebloIndigena()) {
                        if (confirmoCapacidadDiferente()) {
                            if (confirmoPremios()) {
                                if (confirmoProyectos()) {
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean confirmoNivelEstudios() {
        if (spnNivelEstudios.getSelectedItemPosition() != 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean confirmoBeneficiario() {
        if (spnConfirmProgramaGobierno.getSelectedItemPosition() != 0) {
            if (spnConfirmProgramaGobierno.getSelectedItemPosition() == 1) {
                if (seleccionoTipoBeneficiario()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean confirmoTrabajo() {
        if (spnTrabaja.getSelectedItemPosition() != 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean confirmoPuebloIndigena() {
        if (spnConfirmPuebloIndigena.getSelectedItemPosition() != 0) {
            if (spnConfirmPuebloIndigena.getSelectedItemPosition() == 1) {
                if (seleccionoPuebloIndigena()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean confirmoCapacidadDiferente() {
        if (spnConfirmCapacidadDiferente.getSelectedItemPosition() != 0) {
            if (spnConfirmCapacidadDiferente.getSelectedItemPosition() == 1) {
                if (seleccionoCapacidadDiferente()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean confirmoPremios() {
        if (spnConfirmPremios.getSelectedItemPosition() != 0) {
            if (spnConfirmPremios.getSelectedItemPosition() == 1) {
                if (ingresoPremios()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean confirmoProyectos() {
        if (spnConfirmProyectosSociales.getSelectedItemPosition() != 0) {
            if (spnConfirmProyectosSociales.getSelectedItemPosition() == 1) {
                if (ingresoProyectos()) {
                    if (seleccionoApoyo()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean seleccionoTipoBeneficiario() {
        return spnProgramaGobierno.getSelectedItemPosition() != 0;
    }

    private boolean seleccionoPuebloIndigena() {
        return spnPuebloIndigena.getSelectedItemPosition() != 0;
    }

    private boolean seleccionoCapacidadDiferente() {
        return spnCapacidadDiferente.getSelectedItemPosition() != 0;
    }

    private boolean ingresoPremios() {
        return !(etPremios.getText().toString().equals(""));
    }

    private boolean ingresoProyectos() {
        return !(etProyectosSociales.getText().toString().equals(""));
    }

    private boolean seleccionoApoyo() {
        return spnSueldoProyectosSociales.getSelectedItemPosition() != 0;
    }

    //método para establecer los datos en los spinners y los textview
    private void asignarDatos(DatosModificarPerfil datosModificarPerfil) {
        if (datosModificarPerfil.getIdProgramaGobierno() != 0) { spnConfirmProgramaGobierno.setSelection(1); } else spnConfirmProgramaGobierno.setSelection(2);
        if (datosModificarPerfil.getTrabajo() == 0) spnTrabaja.setSelection(2); else spnTrabaja.setSelection(datosModificarPerfil.getTrabajo());
        if (datosModificarPerfil.getIdPuebloIndigena() != 0) spnConfirmPuebloIndigena.setSelection(1); else spnConfirmPuebloIndigena.setSelection(2);
        if (datosModificarPerfil.getIdCapacidadDiferente() != 0) spnConfirmCapacidadDiferente.setSelection(1); else spnConfirmCapacidadDiferente.setSelection(2);
        if (datosModificarPerfil.getApoyoProyectosSociales() == 0) spnSueldoProyectosSociales.setSelection(2); else spnSueldoProyectosSociales.setSelection(1);
        if (datosModificarPerfil.getPremios() != null) { spnConfirmPremios.setSelection(1); etPremios.setText(datosModificarPerfil.getPremios()); } else spnConfirmPremios.setSelection(2);
        if (datosModificarPerfil.getProyectosSociales() != null) { spnConfirmProyectosSociales.setSelection(1); etProyectosSociales.setText(datosModificarPerfil.getProyectosSociales()); } else spnConfirmProyectosSociales.setSelection(2);

        if (datosModificarPerfil.getIdiomas().size() > 0) {
            IdiomasAdicionalesDialogFragment.datosIdiomas = new ArrayList<>();

            textViewTituloIdiomasSeleccionados.setVisibility(View.VISIBLE);
            layoutTablas.setVisibility(View.VISIBLE);
            recyclerViewIdiomasSeleccionados.setVisibility(View.VISIBLE);

            LinearLayoutManager llm = new LinearLayoutManager(thisActivity);
            adapter = new RVIdiomasSeleccionadosAdapter(thisActivity, IdiomasAdicionalesDialogFragment.datosIdiomas);
            recyclerViewIdiomasSeleccionados.setLayoutManager(llm);
            recyclerViewIdiomasSeleccionados.setAdapter(adapter);
            textViewTituloIdiomasSeleccionados.setVisibility(View.VISIBLE);
            recyclerViewIdiomasSeleccionados.setVisibility(View.VISIBLE);

            IdiomasAdicionalesDialogFragment.datosIdiomas.addAll(datosModificarPerfil.getIdiomas());
            adapter.notifyDataSetChanged();
        }

        spnNivelEstudios.setSelection(datosModificarPerfil.getIdNivelEstudios());
        spnPuebloIndigena.setSelection(datosModificarPerfil.getIdPuebloIndigena());
        spnCapacidadDiferente.setSelection(datosModificarPerfil.getIdCapacidadDiferente());
        spnProgramaGobierno.setSelection(datosModificarPerfil.getIdProgramaGobierno());

    }

}
