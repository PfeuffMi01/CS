package com.example.michael.cs;

/**
 * Konstante Strings oder Werte
 * Dient quasi als Konfigurationsdatei für die App
 */

public class Constants {

    public static final int LIST_ITEM_TEMP = 0;
    public static final int LIST_ITEM_PLUG = 1;
    public static final int LIST_ITEM_PLUG_CONSUMPTION = 2;
    public static final int LIST_ITEM_LAMP_WHITE = 3;
    public static final int LIST_ITEM_LAMP_RGB = 4;
    public static final int LIST_ITEM_DOOR_SENSOR = 6;
    public static final int LIST_ITEM_WINDOW_SENSOR = 7;
    public static final int LIST_ITEM_MOVEMENT_SENSOR = 8;
    public static final int LIST_ITEM_HUMIDITY = 9;


    public static final int GROUPS = 1;
    public static final int ROOMS = 2;

    public static final String GROUP_LAMPS = "Lichter";
    public static final String GROUP_PLUGS = "Steckdosen";
    public static final String GROUP_TEMP = "Temperatur";
    public static final String GROUP_MOVEMENT_SENSOR = "Bewegungsm...";
    public static final String GROUP_DOOR_SENSOR = "Türsensoren";
    public static final String GROUP_WINDOW_SENSOR = "Fenstersensoren";
    public static final String GROUP_HUMIDITY = "Feuchtigkeit";
    public static final String GROUP_WEATHER_STATION = "Wetterstation";

    public static final String ROOM_LIVING = "Wohnzimmer";
    public static final String ROOM_GARAGE = "Garage";
    public static final String ROOM_BED = "Schlafzimmer";
    public static final String ROOM_GARDEN = "Garten";
    public static final String ROOM_HALLWAY = "Flur";
    public static final String ROOM_KITCHEN = "Küche";
    public static final String ROOM_OFFICE = "Büro";
    public static final String ROOM_OPENHAB ="Openhab";
    public static final String STATUS_OK = "OK";

    public static final boolean isDebugEnabled = true;
    public static final int MQTT_CONNECTION_TIMEOUT = 6000;
    public static final String ON = "an";
    public static final String OFF = "aus";
    public static final String OPENHAB_COLOR_TOPIC_ADDITION = "color";

    public static String FHEM_IP = "http://192.168.178.21";
    public static String FHEM_PORT = "8083";

    public static final int MAX_TEMP = 30;
    public static final int MIN_TEMP = 10;

    public static long DIALOG_LISTENER_DELAY = 200;


    //MQTT

    public static final String MQTT_IP = "tcp://schlegel2.ddns.net:1883";

    public static final String MQTT_TOPIC_MAIN = "CS";
    public static final String MQTT_TOPIC_LIVINGROOM = "/livingroom";
    public static final String MQTT_TOPIC_OFFICE = "/office";
    public static final String MQTT_TOPIC_KITCHEN = "/kitchen";
    public static final String MQTT_TOPIC_BEDROOM = "/bedroom";
    public static final String MQTT_TOPIC_FLOOR = "/floor";
    public static final String MQTT_TOPIC_GARAGE = "/garage";
    public static final String MQTT_TOPIC_GARDEN = "/garden";

    public static final String[] MQTT_TOPICS_ROOMS = {MQTT_TOPIC_LIVINGROOM, MQTT_TOPIC_OFFICE, MQTT_TOPIC_KITCHEN,
            MQTT_TOPIC_BEDROOM, MQTT_TOPIC_FLOOR, MQTT_TOPIC_GARAGE, MQTT_TOPIC_GARDEN};

    public static final String MQTT_TOPIC_LIGHT = "/light";
    public static final String MQTT_TOPIC_SOCKET = "/socket";
    public static final String MQTT_TOPIC_MOTION = "/motion";
    public static final String MQTT_TOPIC_DOOR = "/door";
    public static final String MQTT_TOPIC_WINDOW = "/window";
    public static final String MQTT_TOPIC_TEMPERATURE = "/temperatur";
    public static final String MQTT_TOPIC_HUMIDITY = "/humidity";

    public static final String MQTT_LOG_DIVIDER = "|";

    public static final int MQTT_CONNECTION_ERROR_NOTI_ID = 666;


    public static final String MQTT_TOPIC_OPENHAB = "patrick/#";
    public static final String MQTT_IP_OPENHAB = "tcp://192.168.178.20:1883";

    public static final String MQTT_TOPIC_JOHANN = "CS/+/+/status";
    public static final String MQTT_IP_JOHANN = "tcp://schlegel2.ddns.net:1883";

}
