package com.example.michael.cs.Interfaces;

public interface OnMQTTMessageArrived {

    void onMQTTMessageArrived(String topic, String message);

}
