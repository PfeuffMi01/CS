package com.example.michael.cs;

/**
 * Created by Patrick PC on 09.11.2016.
 */

public class Constants {

    public static final int LIST_ITEM_TEMP = 0;
    public static final int LIST_ITEM_PLUG = 1;
    public static final int LIST_ITEM_CIRCUIT = 2;
    public static final int LIST_ITEM_LAMP_WHITE = 3;
    public static final int LIST_ITEM_LAMP_RGB = 4;
    public static final int LIST_ITEM_GENERIC_DEVICE = 5;

    public static final int GROUPS = 1;
    public static final int ROOMS = 2;

    public static final String GROUP_CIRCUITS = "Schalter";
    public static final String GROUP_LAMPS = "Lichter";
    public static final String GROUP_PLUGS = "Steckdosen";
    public static final String GROUP_TEMP = "Temperatur";
    public static final String GROUP_GENERIC_DEVICES = "Unbekannt";
    public static final String GROUP_MOVEMENT_SENSORT = "Bewegungsm...";
    public static final String GROUP_DOOR_SENSOR = "Türsensoren";
    public static final String GROUP_WINDOW_SENSOR = "Fenstersensoren";
    public static final String GROUP_WEATHER_STATION = "Wetterstationen";

    public static final String ROOM_LIVING = "Wohnzimmer";
    public static final String ROOM_BATH = "Badezimmer";
    public static final String ROOM_GARAGE = "Garage";
    public static final String ROOM_BED = "Schlafzimmer";
    public static final String ROOM_DINING = "Esszimmer";
    public static final String ROOM_GARDEN = "Garten";
    public static final String ROOM_HALLWAY = "Flur";
    public static final String ROOM_KITCHEN = "Küche";


    public static String FHEM_IP ="http://192.168.178.21";
    public static String FHEM_PORT ="8083";

    public static final int MAX_TEMP = 30;
    public static final int MIN_TEMP = 10;

    public static long DIALOG_LISTENER_DELAY = 200;
}
