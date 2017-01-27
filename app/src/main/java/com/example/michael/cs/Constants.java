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
    public static final String GROUP_MOVEMENT_SENSOR = "Bewegungsmelder";
    public static final String GROUP_DOOR_SENSOR = "Türsensoren";
    public static final String GROUP_WINDOW_SENSOR = "Fenstersensoren";
    public static final String GROUP_HUMIDITY = "Feuchtigkeit";

    public static final String ROOM_LIVING = "Wohnzimmer";
    public static final String ROOM_GARAGE = "Garage";
    public static final String ROOM_BED = "Schlafzimmer";
    public static final String ROOM_GARDEN = "Garten";
    public static final String ROOM_HALLWAY = "Flur";
    public static final String ROOM_KITCHEN = "Küche";
    public static final String ROOM_OFFICE = "Büro";

    public static final String[] ROOM_ARRAY = {ROOM_LIVING, ROOM_GARAGE, ROOM_BED,
            ROOM_GARDEN, ROOM_HALLWAY, ROOM_KITCHEN, ROOM_OFFICE};

    public static final String CAT_MOVE = "Bewegungsmelder";
    public static final String CAT_WINDOW = "Fenstersensor";
    public static final String CAT_HUMID = "Feuchtigkeit";
    public static final String CAT_LAMP_RGB = "Licht RGB";
    public static final String CAT_LAMP_W = "Licht Weiß";
    public static final String CAT_PLUG = "Steckdose";
    public static final String CAT_PLUG_C = "Steckdose mit Verbrauch";
    public static final String CAT_TEMP = "Temperatur";
    public static final String CAT_DOOR = "Türsensor";

    public static final String[] CATEGORY_ARRAY = {CAT_MOVE, CAT_WINDOW, CAT_HUMID,
            CAT_LAMP_RGB, CAT_LAMP_W, CAT_PLUG, CAT_PLUG_C, CAT_TEMP, CAT_DOOR};

    public static final String ON = "an";
    public static final String OFF = "aus";
    public static final String NO_PWD = "NO_PASSWORD";
    public static final String NO_USER = "NO_USER";

    //MQTT

    public static final String MQTT_TOPIC_LIVINGROOM = "wohnzimmer";
    public static final String MQTT_TOPIC_OFFICE = "buero";
    public static final String MQTT_TOPIC_KITCHEN = "kueche";
    public static final String MQTT_TOPIC_BEDROOM = "schlafzimmer";
    public static final String MQTT_TOPIC_FLOOR = "flur";
    public static final String MQTT_TOPIC_GARAGE = "garage";
    public static final String MQTT_TOPIC_GARDEN = "garten";

    public static final String[] MQTT_TOPICS_ROOMS = {MQTT_TOPIC_LIVINGROOM, MQTT_TOPIC_OFFICE, MQTT_TOPIC_KITCHEN,
            MQTT_TOPIC_BEDROOM, MQTT_TOPIC_FLOOR, MQTT_TOPIC_GARAGE, MQTT_TOPIC_GARDEN};

    public static final String MQTT_LOG_DIVIDER = "|";

    /**
     * EXAMPLE
     * Profil 1;tcp://schlegel2.ddns.net;1883~Melder1§Büro§Bewegungsmelder
     */
    public static String PROFILE_DEVICES_DIV = "~";
    public static String DATA_DIV = ";";
    public static String DEVICES_DIV = "?";
    public static String DATA_DEVICES_DIV = "§";
    public static String MQTT_TOPIC_DIVIDER = "/";

}
