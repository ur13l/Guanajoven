package mx.gob.jovenes.guanajuato.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.pusher.android.PusherAndroid;
import com.pusher.android.notifications.ManifestValidator;
import com.pusher.android.notifications.PushNotificationRegistration;
import com.pusher.android.notifications.tokens.PushNotificationRegistrationListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.gob.jovenes.guanajuato.Firebase.FirebaseHelper;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVMessagesAdapter;
import mx.gob.jovenes.guanajuato.api.ChatAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.ChatMessage;
import mx.gob.jovenes.guanajuato.model.DatosMensajes;
import mx.gob.jovenes.guanajuato.model.Mensaje;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.EditTextValidations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by code on 9/02/17.
 */

public class ChatFragment extends CustomFragment {
    private RecyclerView recyclerViewMessages;
    private ImageButton buttonSend;
    private EditText editTextMessage;
    private RVMessagesAdapter adapter;
    private List<Mensaje> mensajes;
    private ChatAPI chatAPI;
    private Retrofit retrofit;
    private int PAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        chatAPI = retrofit.create(ChatAPI.class);

        IntentFilter intentFilter = new IntentFilter("mx.gob.jovenes.guanajuato.MENSAJE_RECIBIDO");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mensajeRecibido, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerViewMessages = (RecyclerView) v.findViewById(R.id.rv_messages);
        buttonSend = (ImageButton) v.findViewById(R.id.button_send);
        editTextMessage = (EditText) v.findViewById(R.id.edittext_message);
        mensajes = new ArrayList<>();

        //setUpAdapter();
        //setUpRecyclerView();

        obtenerMensajes();

        buttonSend.setOnClickListener((View) -> {
                if (editTextMessage.getText().toString().length() != 0) {
                    mensajes.add(0, new Mensaje(editTextMessage.getText().toString(), 1));
                    adapter.notifyData(mensajes);

                    Call<Response<Boolean>> call = chatAPI.enviarMensaje(Sesion.getUsuario().getApiToken(), editTextMessage.getText().toString());

                    call.enqueue(new Callback<Response<Boolean>>() {
                        @Override
                        public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {

                        }

                        @Override
                        public void onFailure(Call<Response<Boolean>> call, Throwable t) {

                        }
                    });

                    editTextMessage.getText().clear();

                }
        });

        return v;
    }

    public void setUpAdapter() {
       /* Mensaje msg1 = new Mensaje();
        Mensaje msg2 = new Mensaje();

        msg2.setMensaje("como estas");
        msg1.setMensaje("hola");

        msg2.setEnviaUsuario(false);
        msg1.setEnviaUsuario(true);
*/
        //adapter = new RVMessagesAdapter(this.getContext(),/*new ArrayList<ChatMessage>()*/Arrays.asList(msg1, msg2));
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setReverseLayout(true);
        //llm.setStackFromEnd(true);
        llm.setStackFromEnd(false);
        adapter = new RVMessagesAdapter(this.getContext(), Arrays.asList());
        recyclerViewMessages.setLayoutManager(llm);
        recyclerViewMessages.setAdapter(adapter);
    }

    private void obtenerMensajes() {
        Call<Response<DatosMensajes>> call = chatAPI.obtenerMensajes(Sesion.getUsuario().getApiToken(), PAGE);

        call.enqueue(new Callback<Response<DatosMensajes>>() {
            @Override
            public void onResponse(Call<Response<DatosMensajes>> call, retrofit2.Response<Response<DatosMensajes>> response) {
                mensajes = response.body().data.getData();
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setReverseLayout(true);
                //llm.setStackFromEnd(true);
                llm.setStackFromEnd(false);
                adapter = new RVMessagesAdapter(getContext(), mensajes);
                recyclerViewMessages.setLayoutManager(llm);
                recyclerViewMessages.setAdapter(adapter);
                PAGE++;
            }

            @Override
            public void onFailure(Call<Response<DatosMensajes>> call, Throwable t) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setMessage(t.getMessage());
                b.show();

                System.err.println("-------------------");
                System.err.println(PAGE);
                System.err.println(Sesion.getUsuario().getApiToken());
                System.err.println("-------------------");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver mensajeRecibido = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Mensaje mensaje = new Gson().fromJson(intent.getExtras().getString("mensaje"), Mensaje.class);
            mensajes.add(mensaje);
            adapter.notifyDataSetChanged();
        }
    };





    /*
    public void enviarMensaje(String mensaje) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(Sesion.getUsuario().getEmail());
        chatMessage.setMessage(mensaje);

        Firebase chatReference = helper.getChatsReference("juanjoseesva@gmail.com");
        chatReference.push().setValue(chatMessage);
    }

    public void mensajeRecibido(ChatMessage msg) {
        //Se añade el mensaje al adaptador
        adapter.add(msg);
        //El recycler view baja hasta la posicion del ultimo mensaje
        recyclerViewMessages.scrollToPosition(adapter.getItemCount() -1);
    }*/


}
