package com.example.michael.cs.Listener;

/**
 * Wird vom {@link com.example.michael.cs.MQTTHandler} aufgerufen, um die Listener über
 * Erfolg oder Misserfolg des Verbindungsaufbaus zu informieren
 */
public interface OnConnectionListener {

public void onMQTTConnection(boolean isConnectionSuccessful);

}
