package com.example.michael.cs;

import android.content.Context;
import android.util.Log;

import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Devices.DoorSensor;
import com.example.michael.cs.Data.Devices.HumiditySensor;
import com.example.michael.cs.Data.Devices.MovementSensor;
import com.example.michael.cs.Data.Devices.Plug;
import com.example.michael.cs.Data.Devices.PlugWithConsumption;
import com.example.michael.cs.Data.Devices.RGBLamp;
import com.example.michael.cs.Data.Devices.Temp;
import com.example.michael.cs.Data.Devices.WhiteLamp;
import com.example.michael.cs.Data.Devices.WindowSensor;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

import java.util.ArrayList;

import static com.example.michael.cs.Constants.DATA_DEVICES_DIV;
import static com.example.michael.cs.Constants.DATA_DIV;
import static com.example.michael.cs.Constants.DEVICES_DIV;
import static com.example.michael.cs.Constants.LIST_ITEM_DOOR_SENSOR;
import static com.example.michael.cs.Constants.LIST_ITEM_HUMIDITY;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP_RGB;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP_WHITE;
import static com.example.michael.cs.Constants.LIST_ITEM_MOVEMENT_SENSOR;
import static com.example.michael.cs.Constants.LIST_ITEM_PLUG;
import static com.example.michael.cs.Constants.LIST_ITEM_PLUG_CONSUMPTION;
import static com.example.michael.cs.Constants.LIST_ITEM_TEMP;
import static com.example.michael.cs.Constants.LIST_ITEM_WINDOW_SENSOR;
import static com.example.michael.cs.Constants.NO_PWD;
import static com.example.michael.cs.Constants.NO_USER;
import static com.example.michael.cs.Constants.PROFILE_DEVICES_DIV;


/**
 * Enthält die Daten für ein User-Profil, wie IP Adresse, Topic-Prefix, Geräte, ...
 */


public class Profile {

    private static final String TAG = "Profile_tag";
    private String name;
    private String serverIP;
    private String serverPort;
    private String username;
    private String password;
    private String topicPrefix;
    public ArrayList<Device> deviceList;

    public Profile(String name, String serverIP, String serverPort, String tPre, String username, String password) {

        this.name = name;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.topicPrefix = tPre;
        this.username = username.equals("") ? NO_USER : username;
        this.password = password.equals("") ? NO_PWD : password;

    }

    public Profile(String n, String ip_, String p, String username, String pass) {
        this.deviceList = new ArrayList<>();
    }

    public Profile(String s, RoomsAndGroupsHandler rgh, Context c) {


        this.deviceList = new ArrayList<>();

        if (!s.equals("")) {

            String[] profile_devices_separated = s.split("\\~", 2);
            String[] profile = profile_devices_separated[0].split("\\;", 6);

            if (profile_devices_separated.length > 1) {
                Log.i(TAG, "Profile: PROFILDATEN: " + profile_devices_separated[0]);
                Log.i(TAG, "Profile: GERÄTEDATEN: " + profile_devices_separated[1]);
            }

            Log.i(TAG, "Profile: PROFILATTRIBUT 1 Name: " + profile[0]);
            Log.i(TAG, "Profile: PROFILATTRIBUT 2 URL: " + profile[1]);
            Log.i(TAG, "Profile: PROFILATTRIBUT 3 Port: " + profile[2]);
            Log.i(TAG, "Profile: PROFILATTRIBUT 4 Topic Prefix: " + profile[3]);

            try {
                Log.i(TAG, "Profile: PROFILATTRIBUT 5 User: " + profile[4]);
                Log.i(TAG, "Profile: PROFILATTRIBUT 6 Passwort: " + profile[5]);
            } catch (Exception e) {
                e.printStackTrace();
            }


            this.name = profile[0];
            this.serverIP = profile[1];
            this.serverPort = profile[2];
            this.topicPrefix = profile[3];

            try {
                this.username = profile[4];
                this.password = profile[5];
            } catch (Exception e) {
                e.printStackTrace();
            }


            if(profile_devices_separated.length > 1 && profile_devices_separated[1] != null) {
                String[] devices = profile_devices_separated[1].split("\\?");
                String a = profile_devices_separated[1];
                Log.i(TAG, "Profile: String to split: " + a);
                int count = a.length() - a.replace("\\?", "").length();
                Log.i(TAG, "Profile: COUNT: " + count);

                Log.i(TAG, "Profile: " + devices.length + " Geräte sind gesplittet worden");

                if (devices.length > 0) {

                    for (String str : devices) {

                        Log.i(TAG, "Profile GERÄTELISTE: " + str);

                        String[] device = str.split("\\§");

                        if (device.length == 5) {

                            Log.i(TAG, "Profile: GERÄT ATTRIBUT 1: " + device[0]);
                            Log.i(TAG, "Profile: GERÄT ATTRIBUT 2: " + device[1]);
                            Log.i(TAG, "Profile: GERÄT ATTRIBUT 3: " + device[2]);
                            Log.i(TAG, "Profile: GERÄT ATTRIBUT 3: " + device[3]);

                            int deviceType = Integer.parseInt(device[0]);
                            String name = device[1];
                            Room r = rgh.getRoomByName(device[2]);
                            Group g = rgh.getGroupByName(device[3]);
                            String topic = device[4];

                            createCorrectDevice(deviceType, c, "", false, name, r, g, topic);

                        } else {
                            Log.e(TAG, "Profile: Device Array ist not 3");
                        }
                    }
                }
            }
        }

        for (Device d : deviceList) {
            Log.i(TAG, "Profile: In LIST: " + d.getName() + " " + d.getGroup().getName());
        }
    }

    private void createCorrectDevice(int deviceType, Context c, String s, boolean b, String name, Room r, Group g, String topic) {

        switch (deviceType) {

            case LIST_ITEM_TEMP:
                deviceList.add((new Temp(LIST_ITEM_TEMP, c, "", b, name, r, g, 20, topic)));
                break;
            case LIST_ITEM_PLUG:
                deviceList.add((new Plug(LIST_ITEM_PLUG, c, "", b, name, r, g, "An", topic)));
                break;
            case LIST_ITEM_PLUG_CONSUMPTION:
                deviceList.add((new PlugWithConsumption(LIST_ITEM_PLUG_CONSUMPTION, c, "", b, name, r, g, "An", "20kw", topic)));
                break;
            case LIST_ITEM_LAMP_WHITE:
                deviceList.add((new WhiteLamp(LIST_ITEM_LAMP_WHITE, c, "", b, name, r, g, 10, "", topic)));
                break;
            case LIST_ITEM_LAMP_RGB:
                deviceList.add((new RGBLamp(LIST_ITEM_LAMP_RGB, c, "", b, name, r, g, 10, -1, "", topic)));
                break;
            case LIST_ITEM_DOOR_SENSOR:
                deviceList.add((new DoorSensor(LIST_ITEM_DOOR_SENSOR, c, "", b, name, r, g, "Vorhin", topic)));
                break;
            case LIST_ITEM_WINDOW_SENSOR:
                deviceList.add((new WindowSensor(LIST_ITEM_WINDOW_SENSOR, c, "", b, name, r, g, "Vorhin", topic)));
                break;
            case LIST_ITEM_MOVEMENT_SENSOR:
                deviceList.add((new MovementSensor(LIST_ITEM_MOVEMENT_SENSOR, c, "", b, name, r, g, "Vorhin", topic)));
                break;
            case LIST_ITEM_HUMIDITY:
                deviceList.add((new HumiditySensor(LIST_ITEM_HUMIDITY, c, "", b, name, r, g, 76, topic)));
                break;


        }


    }

    public String getTopicPrefix() {


        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

        if (deviceList == null) {
            deviceList = new ArrayList<>();
        }

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
     * Zuhause;192.168.1.0;1883~Lampe Rgb 1+Küche+Lampe$Lampe weiß+Wohnzimmer+Lampe+topicLampe
     *
     * @return
     */
    public String getStringForSharedPreferences() {

        if (!this.name.equals("") && !this.serverIP.equals("") && !this.serverPort.equals("")) {

            String resultString = "";

            String pwd = getPassword().equals("") ? NO_PWD : getPassword();
            String user = getUsername().equals("") ? NO_USER : getUsername();

            resultString += getName()
                    + DATA_DIV
                    + getServerIP()
                    + DATA_DIV
                    + getServerPort()
                    + DATA_DIV
                    + getTopicPrefix()
                    + DATA_DIV
                    + user
                    + DATA_DIV
                    + pwd;

            // Wenn Geräte für dieses Profil vorhanden sind, ebenfalls anhängen
            if (this.deviceList != null && this.deviceList.size() > 0) {
                resultString += PROFILE_DEVICES_DIV;

                for (int i = 0; i < deviceList.size(); i++) {
                    resultString
                            += deviceList.get(i).getDeviceType()
                            + DATA_DEVICES_DIV
                            + deviceList.get(i).getName()
                            + DATA_DEVICES_DIV
                            + deviceList.get(i).getRoom().getName()
                            + DATA_DEVICES_DIV
                            + deviceList.get(i).getGroup().getName()
                            + DATA_DEVICES_DIV
                            + deviceList.get(i).getTopic();

                    // Wenn noch ein Gerät danach kommt, den Divider anhängen
                    if ((deviceList.size() - 1) - i > 0) {
                        resultString += DEVICES_DIV;
                    }
                }
            }

            return resultString;
        } else {
            return "ERROR";
        }
    }


}
