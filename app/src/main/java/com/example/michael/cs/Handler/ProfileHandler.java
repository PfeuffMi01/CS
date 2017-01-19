package com.example.michael.cs.Handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Profile;
import com.example.michael.cs.Data.Room;

import java.util.ArrayList;

import static com.example.michael.cs.Constants.DATA_DIV;

/**
 * Created by Patrick PC on 22.12.2016.
 */


public class ProfileHandler {

    private static final String TAG = "ProfileHandler";

    private static ProfileHandler thisInstance;
    private RoomsAndGroupsHandler roomsAndGroupsHandler;

    private SharedPreferences sharedPreferences;
    private Context context;
    private ArrayList<Profile> profileList;
    private Profile currentProfile;
    private ArrayList<Device> deviceList;
    private ArrayList<Room> roomsList;
    private ArrayList<Group> groupList;

    public ProfileHandler(Context c) {
        this.context = c;
        this.profileList = new ArrayList<>();
        this.roomsAndGroupsHandler = RoomsAndGroupsHandler.getInstance(c);

        initSavedProfiles();
    }

    /**
     * Singleton Initialisierung
     *
     * @param c
     * @return
     */
    public static ProfileHandler getInstance(Context c) {

        if (thisInstance == null) {
            thisInstance = new ProfileHandler(c);
        }

        return thisInstance;
    }

    public void initSavedProfiles() {

        profileList = new ArrayList<>();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        int amountProfilesFromPrefs = sharedPreferences.getInt("pref_amount_profiles", 0);
        Log.i(TAG, "initSavedProfiles: " + amountProfilesFromPrefs + " profiles are in prefs");

        if (amountProfilesFromPrefs > 0) {
            String[] profilesArray = new String[amountProfilesFromPrefs];

            for (int i = 0; i < amountProfilesFromPrefs; i++) {
                profilesArray[i] = sharedPreferences.getString("pref_profiles_" + (i + 1), "");
                createProfilesFromSharedPreferences(profilesArray[i]);
                Log.i(TAG, "initSavedProfiles: " + profilesArray[i]);
            }

        } else {
            Log.i(TAG, "initSavedProfiles: No Profiles yet");
        }

    }

    public ArrayList<Profile> getProfileList() {
        return profileList;
    }

    public void addProfile(Profile p) {
        this.profileList.add(p);
    }

    public void addProfileDataToNewObject(Profile nProfile, String s) {

        Log.i(TAG, "addProfileDataToNewObject: " + s);

        String[] profileDataArray = s.split(DATA_DIV);

        for (String as : profileDataArray) {
            Log.i(TAG, "addProfileDataToNewObject: " + as);
        }

        // Profildaten eingeben
        if (profileDataArray.length == 3) {

            Log.i(TAG, "addProfileDataToNewObject: 3");

            nProfile.setName(profileDataArray[0]);
            nProfile.setServerIP(profileDataArray[1]);
            nProfile.setServerPort(profileDataArray[2]);
        } else {
            Log.i(TAG, "addProfileDataToNewObject: 4");
        }
    }

    public void createProfilesFromSharedPreferences(String s) {

        Log.i(TAG, "createProfilesFromSharedPreferences: " + s);

        Log.i(TAG, "createProfilesFromSharedPreferences: 1");

        Profile newProfile = new Profile(s, roomsAndGroupsHandler, context);
        profileList.add(newProfile);

      /*  if (s.contains(PROFILE_DIV)) {

            Log.i(TAG, "createProfilesFromSharedPreferences: 2");
            Log.i(TAG, "createProfilesFromSharedPreferences: Contains " + PROFILE_DIV);

            // Erst in Arrays für Profil und Geräte aufteilen

            String[] profileAndDevicesSeparated = s.split(PROFILE_DIV);

            addProfileDataToNewObject(newProfile, profileAndDevicesSeparated[0]);

            // Wenn Geräte vorhanden sind...
            if (profileAndDevicesSeparated.length > 1) {

                Log.i(TAG, "createProfilesFromSharedPreferences: 5");

                String devicesData = profileAndDevicesSeparated[1];

                // Gerätedaten in Array splitten
                String[] devicesDataArray = devicesData.split(DEVICES_DIV);

                // Anzahl an Geräteparametern muss passen
                // 3 aufeinanderfolgende Daten gehören zu einem Gerät
                if (devicesDataArray.length % 3 == 0) {

                    Log.i(TAG, "createProfilesFromSharedPreferences: 6");

                    // Dieser Index bezieht sich auf der gesamte Gerätearray
                    int indexToUse = 0;

                    // Für jedes Gerät... ( = Gesamtzahl : 3 )
                    for (int i = 0; i < devicesDataArray.length / 3; i++) {

                        Log.i(TAG, "createProfilesFromSharedPreferences: 7");

                        newProfile.addDeviceToDeviceList(new Device(
                                context,
                                "",
                                false,
                                devicesDataArray[indexToUse++],
                                roomsAndGroupsHandler.getRoomByName(devicesDataArray[indexToUse++]),
                                roomsAndGroupsHandler.getGroupByName(devicesDataArray[indexToUse++]),
                                ""));
                    }

                } else {

                    Log.i(TAG, "createProfilesFromSharedPreferences: 8");

                    // Abbrechen, wenn die Daten nicht stimmen
                    break;
                }
            }

            addNewProfile(newProfile);

            // Wenn keine Geräte im Profil sind
        } else {
            Log.i(TAG, "createProfilesFromSharedPreferences: 11");
            addProfileDataToNewObject(newProfile, str);
            addNewProfile(newProfile);
        }*/
    }

    private void addNewProfile(Profile newProfile) {
        try {

            if (newProfile.getName().equals("") || newProfile.getServerIP().equals("") || newProfile.getServerPort().equals("")) {

                Log.i(TAG, "createProfilesFromSharedPreferences addNewProfile: 9");
                Log.e(TAG, "createProfilesFromSharedPreferences addNewProfile: No Data in Profile Object --> Wont get saved");
            } else {

                Log.i(TAG, "createProfilesFromSharedPreferences addNewProfile: 10");
                Log.i(TAG, "createProfilesFromSharedPreferences addNewProfile: Profile saved:  " + newProfile.getName());

                profileList.add(newProfile);

            }
        } catch (Exception e) {

        }
    }

    public String[] getStringToSaveToPrefs() {

        String[] result = new String[profileList.size()];

        for (int i = 0; i < profileList.size(); i++) {
            result[i] = profileList.get(i).getStringForSharedPreferences();
            Log.i(TAG, "getStringToSaveToPrefs: " + result[i]);
        }

        return result;
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(Profile profile) {
        this.currentProfile = profile;
    }
}

