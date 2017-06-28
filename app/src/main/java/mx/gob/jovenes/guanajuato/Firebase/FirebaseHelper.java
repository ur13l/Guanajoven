package mx.gob.jovenes.guanajuato.Firebase;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codigus on 26/06/2017.
 */

public class FirebaseHelper {
    private Firebase dataReference;

    //Variables estaticas que tienen las url
    private final static String SEPARATOR = "___";
    private final static String USERS_PATH = "users";
    private final static String CONTACTS_PATH = "contacts";
    private final static String CHATS_PATH = "chats";
    private final static String FIREBASE_URL = "https://guanajoven-161821.firebaseio.com";

    private static class SingletonHolder {
        private static final FirebaseHelper INSTANCE = new FirebaseHelper();
    }

    public static FirebaseHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //Singleton (Instancia unica)
    public FirebaseHelper() {
        //Asignamos a la variable datareference un nuevo objeto con la url de nuestro proyecto
        this.dataReference = new Firebase(FIREBASE_URL);
    }

    public Firebase getDataReference() {
        return dataReference;
    }

    public String getAuthUserEmail() {
        AuthData authData = dataReference.getAuth();
        String email = null;
        if (authData != null) {
            Map<String, Object> providerData = authData.getProviderData();
            email = providerData.get("email").toString();
        }
        return email;
    }

    public Firebase getUserReference(String email) {
        Firebase userReference = null;
        if (email != null) {
            //Reemplaza los puntos por guión bajo, politica de firebase
            String emailKey = email.replace(".","_");
            //obtiene al usuario apartir de la url de firebase
            userReference = dataReference.getRoot().child(USERS_PATH).child(emailKey);
        }
        return userReference;
    }

    public Firebase getMyUserReference() {
        //retorna al usuario apatir del email autorizado
        return getUserReference(getAuthUserEmail());
    }

    public Firebase getContactsReference(String email) {
        return getUserReference(email).child(CONTACTS_PATH);
    }

    //Obtiene la lista de contactos a partir del usuario autorizado
    public Firebase getMyContactsReference() {
        return getContactsReference(getAuthUserEmail());
    }

    //Retorna un contacto a partir del receptor y emisor
    public Firebase getOneContactReference(String mainEmail, String childEmail) {
        String childKey = childEmail.replace(".", "_");
        return getUserReference(mainEmail).child(CONTACTS_PATH).child(childEmail);
    }

    //Retorna una chat apartir de su llave
    public Firebase getChatsReference(String receiver) {
        String keySender = getAuthUserEmail().replace(".", "_");
        String keyReceiver = receiver.replace(".", "_");

        String keyChat = keySender + SEPARATOR + keyReceiver;
        if (keySender.compareTo(keyChat) > 0) {
            keyChat = keyReceiver + SEPARATOR + keySender;
        }
        return dataReference.getRoot().child(CHATS_PATH).child(keyChat);
    }

    //Cambia el estado de conexion
    public void changeUserConnectionStatus(boolean online) {
        if (getMyUserReference() != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("online", online);
            getMyUserReference().updateChildren(updates);
            notifyContacsOfConnectionChange(online);
        }
    }

    //Notificamos a los usuarios si estamos online o offline
    public void notifyContacsOfConnectionChange(boolean online) {
        notifyContacsOfConnectionChange(online, false);
    }

    public void notifyContacsOfConnectionChange(final boolean online, final boolean signOff) {
        final String myEmail = getAuthUserEmail();
        getMyContactsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String email = child.getKey();
                    Firebase reference = getOneContactReference(email, myEmail);
                    reference.setValue(online);
                }
                if (signOff) {
                    dataReference.unauth();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //Método para cerrar la sesión
    public void signOff() {
        notifyContacsOfConnectionChange(false, true);
    }
}
