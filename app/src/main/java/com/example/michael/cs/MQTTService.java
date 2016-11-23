package com.example.michael.cs;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Patrick PC on 23.11.2016.
 */

public class MQTTService extends Service {

    private static final String TAG = "MQTTService";

    public static final String TOPIC = "/fhem/flur/klingel/";
    public static final String MQTT_IP = "tcp://192.168.178.21:1883";

    public static final String MQTT_TOPIC_JOHANN = "CS/#";
    public static final String MQTT_IP_JOHANN = "tcp://schlegel2.ddns.net:1883";
    NotificationCompat.Builder mBuilder;

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

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_format_list_bulleted_black_18dp);
        mBuilder.setContentTitle("Service gestartet");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(99999, mBuilder.build());

//        final MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(this.getApplicationContext(), MQTT_IP, "androidClient");
        final MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(this.getApplicationContext(), MQTT_IP_JOHANN, "androidClient");

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection was lost!");

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Message Arrived!: " + topic + ": " + new String(message.getPayload()));
                Log.i(TAG, "messageArrived: " + topic + " " + message);

                mBuilder.setSmallIcon(R.drawable.ic_format_list_bulleted_black_18dp);
                mBuilder.setContentTitle("MQTT-Nachricht");
                mBuilder.setContentText(message.toString());
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(99999, mBuilder.build());

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Delivery Complete! " + token.toString());
            }
        });

        try {
            mqttAndroidClient.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "onSuccess: Connection Success!");
                    try {
                        mqttAndroidClient.subscribe(MQTT_TOPIC_JOHANN, 0);

//                        mqttAndroidClient.publish(TOPIC, new MqttMessage("TEST!".getBytes()), this, new IMqttActionListener() {
//                        mqttAndroidClient.publish(MQTT_TOPIC_JOHANN, new MqttMessage("TEST!".getBytes()), this, new IMqttActionListener() {
//
//                            @Override
//                            public void onSuccess(IMqttToken asyncActionToken) {
//                                Log.i(TAG, "onSuccess: publish success");
//                            }
//
//                            @Override
//                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                                Log.i(TAG, "onFailure: publish failure");
//
//                            }
//                        });

                    } catch (MqttException ex) {
                        Log.e(TAG, "onSuccess: ohh :-(");
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "onFailure: Connection Failure!");
                    Log.i(TAG, "onFailure " + exception.toString());
                }
            });
        } catch (MqttException ex) {

        }


        return super.onStartCommand(intent, flags, startId);
    }
}
