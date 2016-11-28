package com.example.michael.cs.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

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

import static com.example.michael.cs.Constants.MQTT_LOG_DIVIDER;
import static com.example.michael.cs.Constants.MQTT_SERVICE_INTENT_MESSAGE;
import static com.example.michael.cs.Constants.MQTT_SERVICE_INTENT_TOPIC;


public class MQTTService extends Service {

    public static int counter = 0;

    private static final String TAG = "MQTTService";
    public static final String MQTT_TOPIC = "CS/#";
    public static final String MQTT_TOPIC_2 = "CS/#/+/status";
    public static final String MQTT_IP = "tcp://schlegel2.ddns.net:1883";

    public MqttAndroidClient mqttAndroidClient;

    public SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        // Code to execute when the service is first created
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");

        counter++;
        Log.i(TAG, "onStartCommand: " + counter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        for (int i = 0; i < 10; i++) {
            mqttLogCreator("CS/livingroom/light", "Testnachricht", "SENT");
        }

        // Erst verbinden, Callback setzen und subscriben
        connectMQTT();
        setMQTTCallbacks();


        // dann schauen, ob eine message im Intent dabei ist
        if (intent != null && intent.getExtras() != null) {

            if (!intent.getStringExtra(MQTT_SERVICE_INTENT_TOPIC).equals("") && !intent.getStringExtra(MQTT_SERVICE_INTENT_MESSAGE).equals("")) {

                String topic = intent.getStringExtra(MQTT_SERVICE_INTENT_TOPIC);
                String message = intent.getStringExtra(MQTT_SERVICE_INTENT_MESSAGE);
                Log.i(TAG, "onStartCommand:  " + topic + " " + message);
//                mqttPublish(topic, message);

            } else {

            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public String getDateTime() {

        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm");
        String dateTime = formatter.format(now);

        return dateTime;
    }

    private void connectMQTT() {

        mqttAndroidClient = new MqttAndroidClient(this.getApplicationContext(), MQTT_IP, "androidClient");

        try {
            mqttAndroidClient.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "onSuccess: Connection Success!");
                    try {
                        mqttAndroidClient.subscribe(MQTT_TOPIC, 0);
                    } catch (MqttException ex) {
                        Log.e(TAG, "onSuccess: Couldn't subscribe " + ex.toString());
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "onFailure: Connection Failure! " + exception.toString());
                }
            });
        } catch (MqttException ex) {

        }

    }

    private void setMQTTCallbacks() {

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection was lost!");

            }

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

    private void mqttPublish(final String topic, final String message) {

        if (mqttAndroidClient.isConnected()) {

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

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "mqttPublish: not connected");
        }
    }

    /*
     * Erstellt einen Logeintrag in den SharedPreferences, der dann im Menü unter "MQTT Log" angezeigt wird.
     * Mit Datum und ob es Empfangen oder gesendet wurde
     */
    private void mqttLogCreator(String topic, String message, String sentOrReceived) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentLog = sharedPreferences.getString("pref_mqtt_log", "");
        currentLog += getDateTime() + " " + sentOrReceived + ": " + topic + "/" + message + MQTT_LOG_DIVIDER;
        editor.putString("pref_mqtt_log", currentLog);
        editor.apply();

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind: ");
        super.onRebind(intent);
    }
}
