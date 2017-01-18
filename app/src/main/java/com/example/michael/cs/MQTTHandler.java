package com.example.michael.cs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Activities.StartActivity;
import com.example.michael.cs.Interfaces.OnConnectionListener;
import com.example.michael.cs.Interfaces.OnConnectionLostListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.michael.cs.Constants.MQTT_LOG_DIVIDER;
import static com.example.michael.cs.Constants.NO_PWD;

/**
 * Kümmert sich um alles, was mit MQTT zu tun hat
 * Ist eine Singleton Klasse
 */
public class MQTTHandler implements MqttCallback, IMqttActionListener {

    private static final String TAG = "MQTTHandler";

    public static int instanceCounter = 0;

    private String connectionIP, connectionTopic, connectionPort;
    private OnConnectionListener onConnectionListener;
    private Context context;
    private boolean isConnected;
    private MqttAndroidClient mqttClient;
    private SharedPreferences sharedPreferences;
    private boolean mqttConnectionSucceded = false;
    private static MQTTHandler thisInstance;
    private OnConnectionLostListener onConnectionLostListener;
    private char[] connectionPassword;
    private String connectionUsername;
    private boolean hasPasswordAndUsername;
    private MqttClient client;
    private String topic;

    /**
     * Singleton Initialisierung
     *
     * @param c
     * @return
     */
    public static MQTTHandler getInstance(Context c) {

        if (thisInstance == null) {
            thisInstance = new MQTTHandler(c);
        }

        return thisInstance;
    }

    public void setConnetionDetails(String ip, String port, String topicToSubscribe, String username, char[] password) {

        connectionIP = ip;
        connectionPort = port;
        connectionTopic = topicToSubscribe;

        String pwd = new String(password);

        Log.i(TAG, "setConnetionDetails: " + ip + " " + port + " " + username + " " + pwd);


        if (String.copyValueOf(password).equals(NO_PWD)) {
            hasPasswordAndUsername = false;
        } else {
            hasPasswordAndUsername = true;
            connectionPassword = password;
            connectionUsername = username;
        }
    }


    public MQTTHandler(Context c) {
        this.context = c;
        isConnected = false;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Erst connecten
     * <p>
     * Wenn erfolgreich:
     * - Callbacks setzen und subscriben
     * <p>
     * Wenn fehlerhaft:
     * - Benachrichtigung posten
     * <p>
     * Immer:
     * - Listener informieren
     */
    public void connect() {
      /*
        - MQTT-Broker-Adresse: 82.165.207.248
        - MQTT-Port: 1883 (Standard Port)
        - MQTT-Login: MqttCsUser
        - MQTT-Password: $tudent17
       */

   /*     try {
             client = new MqttClient(connectionIP + ":" + connectionPort, "1234");

            MqttConnectOptions options = new MqttConnectOptions();
            options.setPassword(connectionPassword);
            options.setUserName(connectionUsername);
            client.connect();

        } catch (MqttException e) {
            e.printStackTrace();
        }
*/


        Log.i(TAG, "connect: ");
        mqttClient = new MqttAndroidClient(context, connectionIP + ":" + connectionPort, "1234");

        MqttConnectOptions connectOptions = new MqttConnectOptions();


        Log.i(TAG, "connect: " + mqttClient.getServerURI() + " " + mqttClient.getClientId());

        if (hasPasswordAndUsername) {
            Log.i(TAG, "connect: setting username:  " + connectionUsername + " and password: " + String.copyValueOf(connectionPassword));
            connectOptions.setPassword(connectionPassword);
            connectOptions.setUserName(connectionUsername);
        }

        try {
            mqttClient.connect(connectOptions, this);
        } catch (MqttException e) {
            Log.e(TAG, "connect: " + e.getCause() + " " + e.getReasonCode() + " " + e.getMessage());
        }
    }

   /* *//**
     * Um auf MQTT Ereignisse reagieren zu können
     *//*
    private void setMQTTCallbacks() {

        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println(TAG + " Connection was lost!");
                onConnectionLostListener.onMQTTConnectionLost();
                isConnected = false;
            }

            // Bei einer Empfangenen Nachricht die SharedPReferences für den Log ergänzen
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                Log.i(TAG, "messageArrived: " + topic + " " + message);
                mqttLogCreator(topic, message.toString(), "RECEIVED");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println(TAG + " Delivery Complete! " + token);
            }
        });
    }*/


    /**
     * Eine Nachricht publishen
     *
     * @param t
     * @param message
     */
    public void mqttPublish(final String t, final String message) {

        Log.i(TAG, "mqttPublish: " + t + " " + message);

        ProfileHandler profileHandler = ProfileHandler.getInstance(context);

        topic = profileHandler.getCurrentProfile().getTopicPrefix().equals("") ? "" : (profileHandler.getCurrentProfile().getTopicPrefix() + "/");
        topic += t;

        if (mqttClient.isConnected()) {

            try {

                Log.i(TAG, "mqttPublish: " + topic + " " + message);

                mqttClient.publish(topic, new MqttMessage(message.getBytes()), this, new IMqttActionListener() {

                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i(TAG, "onSuccess: publish success");
                        mqttLogCreator(topic, message, "SENT");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i(TAG, "onFailure: publish failure");
                        Toast.makeText(context, "Fehler beim Publish der Nachricht "  + topic + " " + message, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }


        } else {
            Log.i(TAG, "mqttPublish: not connected. " + mqttClient.isConnected() + " " + mqttConnectionSucceded);
        }
    }

    /**
     * Erstellt einen Logeintrag in den SharedPreferences, der dann im Menü unter "MQTT Log" angezeigt wird.
     * Mit Datum und ob es Empfangen oder gesendet wurde
     */
    private void mqttLogCreator(String topic, String message, String sentOrReceived) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentLog = sharedPreferences.getString("pref_mqtt_log", "");
        currentLog += sentOrReceived
                + ": "
                + getDateTime()
                + "\n" + topic
                + "/" + message
                + MQTT_LOG_DIVIDER;
        editor.putString("pref_mqtt_log", currentLog);
        editor.apply();
    }

    /**
     * Erzeugen eines Strings mit Datum und Zeit für den Logeintrag
     *
     * @return
     */
    public String getDateTime() {

        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String dateTime = formatter.format(now) + " Uhr";

        return dateTime;
    }

    public boolean isMqttConnectionSucceded() {
        return mqttConnectionSucceded;
    }

    public void setOnConnectionListener(MainActivity mainActivity) {
        this.onConnectionListener = mainActivity;
    }

    public void setOnConnectionListener(StartActivity startActivity) {
        this.onConnectionListener = startActivity;
    }

    public void setOnConnectionLostListener(MainActivity mainActivity) {
        this.onConnectionLostListener = mainActivity;
    }

    public void setOnConnectionLostListener(StartActivity startActivity) {
        this.onConnectionLostListener = startActivity;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void disconnect(String from) {
        Log.i(TAG, "disconnect: " + from);

        if(mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }


/*
        if (mqttClient != null) {
            Log.i(TAG, "disconnect: client not null");
            mqttClient.unregisterResources();
            mqttClient.close();
        }*/
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.i(TAG, "Connection was lost!");
        if (onConnectionLostListener != null) {
            onConnectionLostListener.onMQTTConnectionLost();
            isConnected = false;
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.i(TAG, "messageArrived: " + topic + " " + message);
        mqttLogCreator(topic, message.toString(), "RECEIVED");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i(TAG, " Delivery Complete! " + token);
    }


    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.i(TAG, "onSuccess: ");

        isConnected = true;

        if (onConnectionListener != null) {
            onConnectionListener.onMQTTConnection(true, false, mqttClient.getServerURI());
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.e(TAG, "onFailure: " + asyncActionToken.toString());
        Log.e(TAG, "onFailure: " + asyncActionToken.getException().getCause());
        Log.e(TAG, "onFailure: " + exception.getCause() + " " + exception.getMessage());
        exception.printStackTrace();

        if (onConnectionListener != null) {
            onConnectionListener.onMQTTConnection(false, true, mqttClient.getServerURI());
        }

        Toast.makeText(context, "Verbindungsfehler: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void unregisterConnectionListeners() {
        onConnectionListener = null;
        onConnectionLostListener = null;
    }
}
