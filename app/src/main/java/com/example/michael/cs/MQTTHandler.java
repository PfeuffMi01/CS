package com.example.michael.cs;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Interfaces.OnConnectionListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.michael.cs.Constants.MQTT_CONNECTION_ERROR_NOTI_ID;
import static com.example.michael.cs.Constants.MQTT_LOG_DIVIDER;

/**
 * Kümmert sich um alles, was mit MQTT zu tun hat
 * Ist eine Singleton Klasse
 */
public class MQTTHandler {

    private static final String TAG = "MQTTHandler";
    private static final String MQTT_TOPIC = "CS/+/+/state";
    private static final String MQTT_IP = "tcp://schlegel2.ddns.net:1883";

    public static int instanceCounter = 0;

    private OnConnectionListener onConnectionListener;
    private Context context;
    private MqttAndroidClient mqttAndroidClient;
    private SharedPreferences sharedPreferences;
    private boolean mqttConnectionSucceded = false;
    private static MQTTHandler thisInstance;

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

    public MQTTHandler(Context c) {
        this.context = c;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Log.i(TAG, "MQTTHandler: " + ++instanceCounter);
    }

    /**
     * Erst connecten
     *
     * Wenn erfolgreich:
     * - Callbacks setzen und subscriben
     *
     * Wenn fehlerhaft:
     * - Benachrichtigung posten
     *
     * Immer:
     * - Listener informieren
     */
    public void connect() {

        mqttAndroidClient = new MqttAndroidClient(context, MQTT_IP, "androidClient");

        try {
            mqttAndroidClient.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "onSuccess: Connection Success!" + " " + asyncActionToken);
                    mqttConnectionSucceded = true;
                    setMQTTCallbacks();
                    onConnectionListener.onMQTTConnection(true, false);

                    try {
                        mqttAndroidClient.subscribe(MQTT_TOPIC, 0);
                    } catch (MqttException ex) {
                        Log.e(TAG, "onSuccess: Couldn't subscribe " + ex.toString());
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "onFailure: Connection Failure! " + exception.toString() + " " + asyncActionToken);
                    onConnectionListener.onMQTTConnection(false, false);
                    mqttConnectionSucceded = false;

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                            .setContentTitle("MQTT-Fehler").setSmallIcon(R.drawable.ic_stat_launcher)
                            .setContentText("Fehler beim Verbinden zum MQTT-Server");

                    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                    bigText.bigText("Verbindung zum MQTT Server " + MQTT_IP + " fehlgeschlagen");
                    bigText.setBigContentTitle("MQTT-Verbindungsfehler");
                    notificationBuilder.setStyle(bigText);
                    notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(MQTT_CONNECTION_ERROR_NOTI_ID, notificationBuilder.build());
                }
            });
        } catch (MqttException ex) {
        }
    }

    /**
     * Um auf MQTT Ereignisse reagieren zu können
     */
    private void setMQTTCallbacks() {

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection was lost!");

            }

            /*
            Bei einer Empfangenen Nachrichti die SharedPReferences für den Log ergänzen
             */
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Message Arrived!: " + topic + ": " + new String(message.getPayload()));
                Log.i(TAG, "messageArrived: " + topic + " " + message);

                mqttLogCreator(topic, message.toString(), "RECEIVED");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Delivery Complete! " + token.toString());
            }
        });
    }

    /**
     * Eine Nachricht publishen
     * @param topic
     * @param message
     */
    public void mqttPublish(final String topic, final String message) {

        if (mqttAndroidClient.isConnected() && mqttConnectionSucceded) {

            try {

                Log.i(TAG, "mqttPublish: " + topic + " " + message);

                mqttAndroidClient.publish(topic, new MqttMessage(message.getBytes()), this, new IMqttActionListener() {

                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i(TAG, "onSuccess: publish success");
                        mqttLogCreator(topic, message, "SENT");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i(TAG, "onFailure: publish failure");
                        Toast.makeText(context, "Fehler beim Publish der Nachricht " + topic + "/" + message, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "mqttPublish: not connected. " + mqttAndroidClient.isConnected() + " " + mqttConnectionSucceded);
        }
    }

    /**
    * Erstellt einen Logeintrag in den SharedPreferences, der dann im Menü unter "MQTT Log" angezeigt wird.
    * Mit Datum und ob es Empfangen oder gesendet wurde
    */
    private void mqttLogCreator(String topic, String message, String sentOrReceived) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentLog = sharedPreferences.getString("pref_mqtt_log", "");
        currentLog += getDateTime() + " " + sentOrReceived + ": " + topic + message + MQTT_LOG_DIVIDER;
        editor.putString("pref_mqtt_log", currentLog);
        editor.apply();
    }

    /**
     * Erzeugen eines Strings mit Datum und Zeit für den Logeintrag
     * @return
     */
    public String getDateTime() {

        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH.mm");
        String dateTime = formatter.format(now);

        return dateTime;
    }

    public boolean isMqttConnectionSucceded() {
        return mqttConnectionSucceded;
    }

    public void setOnConnectionListener(MainActivity mainActivity) {
        this.onConnectionListener = mainActivity;
    }
}
