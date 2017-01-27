package com.example.michael.cs.Handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Activities.StartActivity;
import com.example.michael.cs.Interfaces.OnConnectionListener;
import com.example.michael.cs.Interfaces.OnConnectionLostListener;
import com.example.michael.cs.Interfaces.OnMQTTMessageArrived;
import com.example.michael.cs.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private MqttAndroidClient mqttClient;
    private SharedPreferences sharedPreferences;
    private boolean mqttConnectionSucceded = false;
    private OnConnectionLostListener onConnectionLostListener;
    private OnMQTTMessageArrived onMqttMessageArrivedListener;
    private char[] connectionPassword;
    private String connectionUsername;
    private ArrayList<String> subscribedTopics;
    private boolean hasPasswordAndUsername;
    private String topic;
    private String topicPrefix;

    private MQTTHandler(Context c) {
        this.context = c;
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

        subscribedTopics = new ArrayList<>();

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

        mqttClient = new MqttAndroidClient(context, connectionIP + ":" + connectionPort, "androidClient");

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setAutomaticReconnect(true);

        if (hasPasswordAndUsername) {
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

        ProfileHandler profileHandler = ProfileHandler.getInstance(context);
        topicPrefix = profileHandler.getCurrentProfile().getTopicPrefix().equals("") ? "" : (profileHandler.getCurrentProfile().getTopicPrefix() + "/");

        topic = topicPrefix;
        topic += t;

        if (mqttClient.isConnected()) {

            try {

                MqttMessage msg = new MqttMessage(message.getBytes());
                msg.setQos(2);
                mqttClient.publish(topic, msg, this, new IMqttActionListener() {

                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        mqttLogCreator(topic, message, "SENT");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Toast.makeText(context, context.getString(R.string.error_at_publishing_message) + topic + " " + message, Toast.LENGTH_LONG).show();
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
    private String getDateTime() {

        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return formatter.format(now) + " Uhr";
    }

    public void setOnMQTTMessageArrivedListener(MainActivity mainActivity) {
        this.onMqttMessageArrivedListener = mainActivity;
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

    public void disconnect() {

        try {
            if (mqttClient.isConnected()) {
                try {
                    mqttClient.unregisterResources();
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
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.i(TAG, "messageArrived: " + topic + " " + message);

        mqttLogCreator(topic, message.toString(), "RECEIVED");
        findDeviceTopicIDFromMessage(topic, message);
    }

    private void findDeviceTopicIDFromMessage(String topic, MqttMessage message) {

        if (onMqttMessageArrivedListener != null) {
            onMqttMessageArrivedListener.onMQTTMessageArrived(topic, message.toString());
        } else {
            Log.i(TAG, "findDeviceTopicIDFromMessage: No Device");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i(TAG, " Delivery Complete! " + token);
    }


    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        mqttClient.setCallback(this);
        String message = "init";

        // eigentlich nicht nötig
        try {
            MqttMessage msg = new MqttMessage(message.getBytes());
            msg.setQos(2);
            mqttClient.publish("CS/init", msg);
        } catch (MqttException e) {
            e.printStackTrace();
        }


        // Ist für den onConnectionListener der StartActivity gedacht
        if (onConnectionListener != null) {
            onConnectionListener.onMQTTConnection(true, false, mqttClient.getServerURI());
        }
    }

    public void subscribeToTopic(String t) {

        if (!subscribedTopics.contains(t)) {
            try {
                mqttClient.subscribe(t, 2);
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

        Toast.makeText(context, context.getString(R.string.conn_failure) + exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void unregisterConnectionListeners() {
        onConnectionListener = null;
        onConnectionLostListener = null;
        onMqttMessageArrivedListener = null;
    }

    public void unsubscribeFromTopic(String t) {

        if (subscribedTopics.contains(t)) {
            try {
                mqttClient.unsubscribe(t);
                subscribedTopics.remove(t);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }
}
