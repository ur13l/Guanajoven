package mx.gob.jovenes.guanajuato.fragments;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import mx.gob.jovenes.guanajuato.model.ChatMessage;
import mx.gob.jovenes.guanajuato.sesion.Sesion;

/**
 * Created by code on 9/02/17.
 */

public class ChatFragment extends CustomFragment {
    private RecyclerView recyclerViewMessages;
    private ImageButton buttonSend;
    private EditText editTextMessage;
    private RVMessagesAdapter adapter;
    private FirebaseHelper helper;
    private PusherAndroid pusher;
    private PushNotificationRegistration nativePusher;
    private String defaultSenderId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);


        pusher = new PusherAndroid("b378afe11ef5100dae7e");
        defaultSenderId = getString(R.string.gcm_defaultSenderId);
        nativePusher = pusher.nativePusher();

        PushNotificationRegistrationListener listener = new PushNotificationRegistrationListener() {
            @Override
            public void onSuccessfulRegistration() {
                Snackbar.make(getView(), "Si conecto", Snackbar.LENGTH_LONG).show();
            }



            @Override
            public void onFailedRegistration(int statusCode, String response) {
                Snackbar.make(getView(), "no conecto", Snackbar.LENGTH_LONG).show();
            }
        };

        try {
            nativePusher.registerGCM(getActivity().getApplicationContext(), defaultSenderId, listener);
        } catch (ManifestValidator.InvalidManifestException e) {
            e.printStackTrace();
        }

        recyclerViewMessages = (RecyclerView) v.findViewById(R.id.rv_messages);
        buttonSend = (ImageButton) v.findViewById(R.id.button_send);
        editTextMessage = (EditText) v.findViewById(R.id.edittext_message);

        setUpAdapter();
        setUpRecyclerView();

        /*buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje(editTextMessage.getText().toString());
            }
        });*/

        return v;
    }

    public void setUpAdapter() {
        ChatMessage msg1 = new ChatMessage();
        ChatMessage msg2 = new ChatMessage();

        msg2.setMessage("como estas");
        msg1.setMessage("hola");

        msg2.setSendByMe(false);
        msg1.setSendByMe(true);

        adapter = new RVMessagesAdapter(this.getContext(),/*new ArrayList<ChatMessage>()*/Arrays.asList(msg1, msg2));
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerViewMessages.setLayoutManager(llm);
        recyclerViewMessages.setAdapter(adapter);
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
