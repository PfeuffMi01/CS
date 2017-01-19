package com.example.michael.cs.Handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Activities.StartActivity;
import com.example.michael.cs.Constants;
import com.example.michael.cs.Interfaces.OnConnectionListener;
import com.example.michael.cs.Interfaces.OnConnectionLostListener;
import com.example.michael.cs.Interfaces.OnMQTTMessageArrived;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
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
    private static MQTTHandler thisInstance;
    private String connectionIP, connectionTopic, connectionPort;
    private OnConnectionListener onConnectionListener;
    private Context context;
    private boolean isConnected;
    private MqttAndroidClient mqttClient;
    private SharedPreferences sharedPreferences;
    private boolean mqttConnectionSucceded = false;
    private OnConnectionLostListener onConnectionLostListener;
    private OnMQTTMessageArrived onMqttMessageArrivedListener;
    private char[] connectionPassword;
    private String connectionUsername;
    private boolean hasPasswordAndUsername;
    private String topic;
    private String topicPrefix;

    public MQTTHandler(Context c) {
        this.context = c;
        isConnected = false;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

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


        Log.i(TAG, "connect: ");
        mqttClient = new MqttAndroidClient(context, connectionIP + ":" + connectionPort, "1234");


        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setAutomaticReconnect(true);

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

    /**
     * Eine Nachricht publishen
     *
     * @param t
     * @param message
     */
    public void mqttPublish(final String t, final String message) {

        Log.i(TAG, "mqttPublish: " + t + " " + message);

        ProfileHandler profileHandler = ProfileHandler.getInstance(context);
        topicPrefix = profileHandler.getCurrentProfile().getTopicPrefix().equals("") ? "" : (profileHandler.getCurrentProfile().getTopicPrefix() + "/");

        topic = topicPrefix;
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
                        Toast.makeText(context, "Fehler beim Publish der Nachricht " + topic + " " + message, Toast.LENGTH_LONG).show();
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

    public void setOnMQTTMessageArrivedListener(MainActivity mainActivity) {
        Log.i(TAG, "setOnMQTTMessageArrivedListener: main");
        this.onMqttMessageArrivedListener = mainActivity;
    }

    public void setOnConnectionListener(MainActivity mainActivity) {
        Log.i(TAG, "setOnConnectionListener: main");
        this.onConnectionListener = mainActivity;
    }

    public void setOnConnectionListener(StartActivity startActivity) {
        Log.i(TAG, "setOnConnectionListener: start");
        this.onConnectionListener = startActivity;
    }

    public void setOnConnectionLostListener(MainActivity mainActivity) {
        Log.i(TAG, "setOnConnectionLostListener: main");
        this.onConnectionLostListener = mainActivity;
    }

    public void setOnConnectionLostListener(StartActivity startActivity) {
        Log.i(TAG, "setOnConnectionLostListener: start");
        this.onConnectionLostListener = startActivity;
    }

    public void disconnect(String from) {
        Log.i(TAG, "disconnect: " + from);

        try {
            if (mqttClient.isConnected()) {
                try {
                    mqttClient.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        findDeviceTopicIDFromMessage(topic, message);
    }

    private void findDeviceTopicIDFromMessage(String topic, MqttMessage message) {

        String s = topic;
        String[] roomTopics = Constants.MQTT_TOPICS_ROOMS;
        if (topic.startsWith(topicPrefix)) {
            s = topic.replace(topicPrefix, "");
        }

        for (String t : roomTopics) {
            if (s.startsWith(t)) {
                s = s.replace((t + "/"), "");
            }
        }

        Log.i(TAG, "findDeviceTopicIDFromMessage: " + s);

        if (onMqttMessageArrivedListener != null) {
            onMqttMessageArrivedListener.onMQTTMessageArrived(s, String.valueOf(message));
            Log.i(TAG, "findDeviceTopicIDFromMessage: " + s + " " + message);
        } else {
            Log.e(TAG, "findDeviceTopicIDFromMessage: listener is null");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i(TAG, " Delivery Complete! " + token);
    }


    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.i(TAG, "onSuccess: ");
        isConnected = true;

        mqttClient.setCallback(this);

    /*    try {
            mqttClient.subscribe("CS/buero/l2", 2);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, "onSuccess: :-(");
        }*/

        // Ist für den onConnectionListener der StartActivity gedacht
        if (onConnectionListener != null) {
            Log.i(TAG, "onSuccess: set listener");
            onConnectionListener.onMQTTConnection(true, false, mqttClient.getServerURI());
        }
    }

    public void subscribeToTopic(String t) {

        if (mqttClient != null || mqttClient.isConnected()) {
            try {
                mqttClient.subscribe(t, 0);
                Log.i(TAG, "subscribeToTopic: " + t + " subscribed");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.e(TAG, "onFailure: " + asyncActionToken.toString());
        Log.e(TAG, "onFailure: " + asyncActionToken.getException().getCause());
        Log.e(TAG, "onFailure: " + exception.getCause() + " " + exception.getMessage());
        exception.printStackTrace();

        // Ist für den onConnectionListener der StartActivity gedacht
        if (onConnectionListener != null) {
            onConnectionListener.onMQTTConnection(false, true, mqttClient.getServerURI());
        }

        Toast.makeText(context, "Verbindungsfehler: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void unregisterConnectionListeners() {
        onConnectionListener = null;
        onConnectionLostListener = null;
        onMqttMessageArrivedListener = null;
    }

    public void unsubscribeFromTopic(String topic) {
        try {
            mqttClient.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
