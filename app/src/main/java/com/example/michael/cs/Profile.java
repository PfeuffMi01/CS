package com.example.michael.cs;

import com.example.michael.cs.Data.Devices.Device;

import java.util.ArrayList;

import static com.example.michael.cs.Constants.DATA_DEVICES_DIV;
import static com.example.michael.cs.Constants.DATA_DIV;
import static com.example.michael.cs.Constants.DEVICES_DIV;

/**
 * Enthält die Daten für ein User-Profil, wie IP Adresse, Topic-Prefix, Geräte, ...
 */

public class Profile {

    private String name;
    private String serverIP;
    private String serverPort;
    private ArrayList<Device> deviceList;

    public Profile(String name, String serverIP, String serverPort) {

        this.name = name;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public Profile() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }

    public void addDeviceToDeviceList(Device device) {

        if (this.deviceList == null) {
            deviceList = new ArrayList<>();
        }

        this.deviceList.add(device);
    }

    public void setDeviceList(ArrayList<Device> deviceList) {
        this.deviceList = deviceList;
    }

    /**
     * Baut einen String, der zur Abspeicherung in den Shared Prefernences verwendet wird.
     * Einzelne Elemente werden durch den DATA_DIV getrennt, um den String später wieder in Objekte zerlegen zu können.
     * Name, IP und Port müssen vorhanden sein.
     * <p>
     * BEISPIEL
     * Zuhause*192.168.1.0*1883+Lampe Rgb 1&Küche$Lampe$Lampe weiß$Wohnzimmer$Lampe
     *
     * @return
     */
    public String getStringForSharedPreferences() {

        if (!this.name.equals("") && !this.serverIP.equals("") && !this.serverPort.equals("")) {

            String resultString = "";
            resultString += getName()
                    + DATA_DIV
                    + getServerIP()
                    + DATA_DIV
                    + getServerPort();

            // Wenn Geräte für dieses Profil vorhanden sind, ebenfalls anhängen
            if (this.deviceList != null && this.deviceList.size() > 0) {
                resultString += DATA_DEVICES_DIV;

                for (Device device : deviceList) {
                    resultString += device.getName()
                            + DEVICES_DIV
                            + device.getRoom()
                            + DEVICES_DIV
                            + device.getGroup();
                }
            }

            return resultString;
        } else {
            return "ERROR";
        }
    }
}
