package com.example.michael.cs.Interfaces;

import com.example.michael.cs.Handler.MQTTHandler;

/**
 * Wird vom {@link MQTTHandler} aufgerufen, um die Listener über
 * Erfolg oder Misserfolg des Verbindungsaufbaus zu informieren
 */
public interface OnConnectionListener {

    void onMQTTConnection(boolean isConnectionSuccessful, boolean forcedAppEntering, String connectionIP);

}
