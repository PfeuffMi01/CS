package com.example.michael.cs.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.cs.Constants;
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
import com.example.michael.cs.Fragments.AllFragment;
import com.example.michael.cs.Fragments.DeviceSingleSortListFragment;
import com.example.michael.cs.Fragments.GroupFragment;
import com.example.michael.cs.Fragments.RoomFragment;
import com.example.michael.cs.Handler.MQTTHandler;
import com.example.michael.cs.Handler.ProfileHandler;
import com.example.michael.cs.Handler.RoomsAndGroupsHandler;
import com.example.michael.cs.Interfaces.OnConnectionListener;
import com.example.michael.cs.Interfaces.OnConnectionLostListener;
import com.example.michael.cs.Interfaces.OnMQTTMessageArrived;
import com.example.michael.cs.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.R.attr.mode;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.michael.cs.Constants.CATEGORY_ARRAY;
import static com.example.michael.cs.Constants.CAT_DOOR;
import static com.example.michael.cs.Constants.CAT_HUMID;
import static com.example.michael.cs.Constants.CAT_LAMP_RGB;
import static com.example.michael.cs.Constants.CAT_LAMP_W;
import static com.example.michael.cs.Constants.CAT_MOVE;
import static com.example.michael.cs.Constants.CAT_PLUG;
import static com.example.michael.cs.Constants.CAT_PLUG_C;
import static com.example.michael.cs.Constants.CAT_TEMP;
import static com.example.michael.cs.Constants.CAT_WINDOW;
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
import static com.example.michael.cs.Constants.MQTT_TOPIC_DIVIDER;
import static com.example.michael.cs.Constants.PROFILE_DEVICES_DIV;
import static com.example.michael.cs.Constants.ROOM_ARRAY;

public class MainActivity extends AppCompatActivity implements OnConnectionListener, OnConnectionLostListener, View.OnClickListener, OnMQTTMessageArrived {
    public static final int ALL_FRAGMENT = 0;
    public static final int ROOM_FRAGMENT = 1;
    public static final int GROUP_FRAGMENT = 2;
    public static final int DEVICE_SINGLE_SORT_LIST_FRAGMENT = 3;
    private static final String TAG = "MainActivity";
    public int CURRENT_FRAGMENT;
    public int CURRENT_LIST_CATEGORY;

    Room livingRoom, bedRoom, garageRoom, hallwayRoom, gardenRoom, kitchenRoom, officeRoom;
    Group plugs, movementSens, doorSens, windowSens, humidity, temperature, lamps;

    private AllFragment allFragment;
    private RoomFragment roomFragment;
    private GroupFragment groupFragment;

    private FrameLayout fragContainer;

    private ArrayList<Room> roomsList;
    private ArrayList<Group> groupList;

    private ArrayList<Device> deviceList;
    private DeviceSingleSortListFragment deviceSingleSortListFragment;


    private Menu menu;

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private MQTTHandler mqttHandler;
    private ProfileHandler profileHandler;
    private RoomsAndGroupsHandler roomsAndGroupsHandler;
    private Device deviceToEdit;
    private int deviceToEditAdapterPos;
    private boolean logoutButtonHasBeenClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: Lifecycle");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileHandler = ProfileHandler.getInstance(this);
        mqttHandler = MQTTHandler.getInstance(this);
        roomsAndGroupsHandler = RoomsAndGroupsHandler.getInstance(this);
        logoutButtonHasBeenClicked = false;

        initUI();
        initRoomsAndGroups();
        initFragment();
        initTabs();
        subscribeMQTTTopics();
        getSupportActionBar().setSubtitle(getString(R.string.connected_to_profile) + profileHandler.getCurrentProfile().getName());
    }

    /**
     * Wenn Activity wieder geladen wird mqtt Verbindung aufbauen und listener registrieren
     */
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: Lifecycle");
        loadDevicesFromProfileHandler();
        registerAsMqttListener();

        if (!mqttHandler.isConnected()) {
            mqttHandler.connect();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: Lifecycle");
        mqttHandler.unregisterConnectionListeners();
        logoutButtonHasBeenClicked = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy:  Lifecycle");
        mqttHandler.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: Lifecycle");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Beim MQTTHandler als Listener für diverse events anmelden
     */
    private void registerAsMqttListener() {
        mqttHandler.setOnConnectionListener(this);
        mqttHandler.setOnConnectionLostListener(this);
        mqttHandler.setOnMQTTMessageArrivedListener(this);
    }

    /**
     * Elemente des UI initialisieren
     */
    private void initUI() {

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_main);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setVisibility(VISIBLE);
        appBarLayout.setVisibility(VISIBLE);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);
    }


    /**
     * Vorbereitung aller Fragmente
     */
    private void initFragment() {

        fragContainer = (FrameLayout) findViewById(R.id.fragment_container);
        fragContainer.setVisibility(GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (deviceSingleSortListFragment == null) {
            deviceSingleSortListFragment = new DeviceSingleSortListFragment();
        }
        fragmentTransaction.replace(R.id.fragment_container, deviceSingleSortListFragment, "DeviceSingleSortListFragment");
        fragmentTransaction.commit();

        allFragment = new AllFragment();
        roomFragment = new RoomFragment();
        groupFragment = new GroupFragment();
    }

    /**
     * Initialisierung des TabLayouts und dessen ViewPager
     */
    private void initTabs() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            /**
             * Den {@link FloatingActionButton} fab nur anzeigen, wenn der ViewPager das Fragment "AllFragment" zeigt
             *
             * @param position
             */
            public void onPageSelected(int position) {

                if (position == ALL_FRAGMENT) {

                    fab.show();

                    CURRENT_FRAGMENT = ALL_FRAGMENT;
                    CURRENT_LIST_CATEGORY = 0;
                } else {

                    fab.hide();

                    CURRENT_FRAGMENT = 999;

                    if (position == ROOM_FRAGMENT) {
                        CURRENT_LIST_CATEGORY = ROOM_FRAGMENT;
                    } else if (position == GROUP_FRAGMENT) {
                        CURRENT_LIST_CATEGORY = GROUP_FRAGMENT;
                    }
                }
            }
        });

        CURRENT_FRAGMENT = ALL_FRAGMENT;
    }

    /**
     * Erstellen aller Räume, Gruppen
     * Kontrollieren, mit welchem Server man gerade verbunden ist und nur die entsprechenden Geräte dafür laden
     * TODO Nur die wirklich verfügbaren Geräte anlegen (Status vom Server)
     */
    private void initRoomsAndGroups() {

        roomsList = new ArrayList<>();
        groupList = new ArrayList<>();
        deviceList = new ArrayList<>();

        roomsList = roomsAndGroupsHandler.getRoomList();
        groupList = roomsAndGroupsHandler.getGroupList();

        Collections.sort(roomsList);
        Collections.sort(groupList);

        loadDevicesFromProfileHandler();
    }


    /**
     * Wechselt Fragmente
     * Entscheidet über die Sichtbarkeit des fragContainers und des ViewPagers
     *
     * @param visibilityFragContainer
     * @param visibilityViewPager
     * @param visibilityToolbarBackArrow
     * @param name
     * @param mode
     */
    private void fragChanger(int visibilityFragContainer, int visibilityViewPager, boolean visibilityToolbarBackArrow, String name, int mode) {

        fragContainer.setVisibility(visibilityFragContainer);
        viewPager.setVisibility(visibilityViewPager);
        tabLayout.setVisibility(visibilityViewPager);
        toolbarManager(name, visibilityToolbarBackArrow);

        if (visibilityFragContainer == VISIBLE) {
            deviceSingleSortListFragment.setSortToShow(name);
        }

        CURRENT_FRAGMENT = DEVICE_SINGLE_SORT_LIST_FRAGMENT;
    }

    /**
     * Kümmert sich um dem Titel der Toolbar
     *
     * @param title
     * @param showBackArrow
     */
    public void toolbarManager(String title, boolean showBackArrow) {

        if (title != null) {
            getSupportActionBar().setTitle(title);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBackArrow);
        getSupportActionBar().setDisplayShowHomeEnabled(showBackArrow);
    }

    /**
     * Icons für die Tabs setzen
     */
    private void setupTabIcons() {
        TypedArray imgs = getResources().obtainTypedArray(R.array.tab_icons);

        for (int i = 0; i < imgs.length(); i++) {
            tabLayout.getTabAt(i).setIcon(imgs.getResourceId(i, -1));
        }
        imgs.recycle();
    }

    /**
     * Fragmente in den ViewPager des tabLayout laden
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(allFragment, "Alle");
        adapter.addFragment(roomFragment, "Räume");
        adapter.addFragment(groupFragment, "Gruppen");
        viewPager.setAdapter(adapter);
    }

    /**
     * {@link FloatingActionButton verstecken}
     */
    public void hideFab() {
        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            fab.hide();
        }
    }

    /**
     * {@link FloatingActionButton zeigen}
     */
    public void showFab() {
        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            fab.show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.floatingActionButton:
                showDeviceEditorDialog();
                break;
            default:
                break;
        }

    }

    /**
     * Erstellt den Dialog für den Geräte-Editor.
     * Entweder durch Klick auf den {@link FloatingActionButton} oder auf das "Edit" Symbol eines Geräts
     * Wenn es vom Edit Button kommt, wird noch der "Löschen" Button in den Dialog eingefügt und die
     * Felder mit den Werten des Geräts vorgeladen.
     */
    private void showDeviceEditorDialog() {

        final boolean createNewDevice = deviceToEdit == null;

        final String[] spinnerArrayCategories = CATEGORY_ARRAY;
        final String[] spinnerArrayRooms = ROOM_ARRAY;

        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_device, null);

        final TextInputEditText name = (TextInputEditText) dialogView.findViewById(R.id.devicename_et);
        final TextInputEditText topic = (TextInputEditText) dialogView.findViewById(R.id.deviceid_et);
        final Spinner category = (Spinner) dialogView.findViewById(R.id.category_spin);
        final Spinner room = (Spinner) dialogView.findViewById(R.id.room_spin);

        setupSpinnersForNewDeviceDialog(category, room, spinnerArrayCategories, spinnerArrayRooms);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        if (!createNewDevice) {
            setupDialogForEditingDevice(name, topic, spinnerArrayCategories, spinnerArrayRooms, category, room, dialogBuilder);
        } else {
            dialogBuilder.setTitle(R.string.add_device);
        }

        dialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        boolean errorEmptyName = false;
                        boolean errorEmptyTopic = false;
                        boolean errorExistingName = false;
                        boolean unsupportedCharacter = false;
                        boolean errorExistingTopic = false;

                        String deviceName = String.valueOf(name.getText());

                        if (deviceName.equals("")) {
                            errorEmptyName = true;
                        }

                        String deviceTopic = String.valueOf(topic.getText());

                        if (deviceTopic.equals("")) {
                            errorEmptyTopic = true;
                        }

                        if (createNewDevice && doesTopicExist(deviceTopic)) {
                            errorExistingTopic = true;
                        }

                        if (createNewDevice && doesDeviceExist(deviceName, spinnerArrayRooms[room.getSelectedItemPosition()])) {
                            errorExistingName = true;
                        }

                        // Kontrolle auf unerlaubte Zeichen
                        if (
                                deviceName.contains(PROFILE_DEVICES_DIV)
                                        || deviceName.contains(DATA_DIV)
                                        || deviceName.contains(DEVICES_DIV)
                                        || deviceName.contains(DATA_DEVICES_DIV)
                                        || deviceName.contains(MQTT_TOPIC_DIVIDER)
                                        || deviceTopic.contains(PROFILE_DEVICES_DIV)
                                        || deviceTopic.contains(DATA_DIV)
                                        || deviceTopic.contains(DEVICES_DIV)
                                        || deviceTopic.contains(DATA_DEVICES_DIV)
                                        || deviceTopic.contains(MQTT_TOPIC_DIVIDER)) {
                            unsupportedCharacter = true;
                        }

                        if (unsupportedCharacter) {
                            Toast.makeText(MainActivity.this, getString(R.string.these_symbols_are_not_allowed)
                                    + PROFILE_DEVICES_DIV
                                    + "   " + DATA_DIV
                                    + "   " + DEVICES_DIV
                                    + "   " + DATA_DEVICES_DIV, Toast.LENGTH_LONG).show();
                        } else if (errorEmptyName) {
                            Toast.makeText(MainActivity.this, R.string.enter_device_name, Toast.LENGTH_LONG).show();
                        } else if (errorEmptyTopic) {
                            Toast.makeText(MainActivity.this, R.string.enter_id_topic, Toast.LENGTH_LONG).show();
                        } else if (errorExistingTopic) {
                            Toast.makeText(MainActivity.this, getString(R.string.the_topic) + deviceTopic + getString(R.string.is_already_there), Toast.LENGTH_LONG).show();
                        } else if (errorExistingName) {
                            Toast.makeText(MainActivity.this, R.string.device_already_there, Toast.LENGTH_LONG).show();

                            // Wenn alle Eingaben OK sind
                        } else if (!errorEmptyName && !errorEmptyTopic && !errorExistingName && !errorExistingTopic) {
                            createOrEditDevice(createNewDevice, deviceName, deviceTopic, spinnerArrayCategories[category.getSelectedItemPosition()], spinnerArrayRooms[room.getSelectedItemPosition()]);
                        }
                    }
                }
        );
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        // Wenn der Dialog per "Abbrechen" oder Zurück-Taste abgebrochen wird
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                deviceToEdit = null;
                deviceToEditAdapterPos = -1;
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Button zum Löschen eines Geräts in den Dialog einfügen
     * Textfelder und Spinner initialisieren
     *
     * @param name
     * @param topic
     * @param spinnerArrayCategories
     * @param spinnerArrayRooms
     * @param category
     * @param room
     * @param dialogBuilder
     */
    private void setupDialogForEditingDevice(TextInputEditText name, TextInputEditText topic, String[] spinnerArrayCategories, String[] spinnerArrayRooms, Spinner category, Spinner room, AlertDialog.Builder dialogBuilder) {

        dialogBuilder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Device deletedDevice = null;
                try {
                    deletedDevice = (Device) deviceToEdit.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                unsubscribeMQTTTopic(deviceToEdit);
                deviceList.remove(deviceToEdit);
                notifyOnDataChangedForFragments();

                showUndoSnackbar(deletedDevice, deviceToEditAdapterPos);
            }
        });

        dialogBuilder.setTitle(deviceToEdit.getName() + getString(R.string.edit));

        name.setText(deviceToEdit.getName());
        topic.setText(deviceToEdit.getTopic());

        int type = deviceToEdit.getDeviceType();
        String toLookFor = "";

        switch (type) {
            case LIST_ITEM_TEMP:
                toLookFor = CAT_TEMP;
                break;

            case LIST_ITEM_PLUG:
                toLookFor = CAT_PLUG;
                break;

            case LIST_ITEM_PLUG_CONSUMPTION:
                toLookFor = CAT_PLUG_C;
                break;

            case LIST_ITEM_LAMP_WHITE:
                toLookFor = CAT_LAMP_W;
                break;

            case LIST_ITEM_LAMP_RGB:
                toLookFor = CAT_LAMP_RGB;
                break;

            case LIST_ITEM_DOOR_SENSOR:
                toLookFor = CAT_DOOR;
                break;

            case LIST_ITEM_WINDOW_SENSOR:
                toLookFor = CAT_WINDOW;
                break;

            case LIST_ITEM_MOVEMENT_SENSOR:
                toLookFor = CAT_MOVE;
                break;

            case LIST_ITEM_HUMIDITY:
                toLookFor = CAT_HUMID;
                break;
        }


        for (int i = 0; i < spinnerArrayCategories.length; i++) {
            if (spinnerArrayCategories[i].equals(toLookFor)) {
                category.setSelection(i);
            }
        }

        for (int i = 0; i < spinnerArrayRooms.length; i++) {
            if (spinnerArrayRooms[i].equals(deviceToEdit.getRoom().getName())) {
                room.setSelection(i);
            }
        }

    }

    /**
     * Kontrolle, ob es ein Topic schon gibt
     *
     * @param deviceTopic
     * @return
     */
    private boolean doesTopicExist(String deviceTopic) {

        if (deviceList == null) {
            deviceList = new ArrayList<>();
        }

        for (Device d : deviceList) {
            if (d.getTopic().equals(deviceTopic)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kontrolle ob es in einem Raum schon ein Gerät mit diesem Namen gibt
     *
     * @param deviceName
     * @param room
     * @return
     */
    private boolean doesDeviceExist(String deviceName, String room) {

        if (deviceList == null) {
            deviceList = new ArrayList<>();
        }

        for (Device d : deviceList) {
            if (d.getName().equals(deviceName) && d.getRoom().getName().equals(room)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gepeicherte Geräte werden vom ProfileHandler geladen
     */
    private void loadDevicesFromProfileHandler() {

        this.deviceList = profileHandler.getCurrentProfile().getDeviceList();

        for (Device d : deviceList) {
            Log.i(TAG, "loadDevicesFromProfileHandler: " + d.getName());
        }
    }

    /**
     * Anlegen oder ändern eines neuen Geräts.
     * Anhand der category wird entschieden, um welchen Typ es sich handelt
     *
     * @param createNewDevice
     * @param deviceName
     * @param deviceTopic
     * @param category
     * @param room
     */
    private void createOrEditDevice(boolean createNewDevice, String deviceName, String deviceTopic, String category, String room) {

        final String[] catArray = CATEGORY_ARRAY;
        Room tmpRoom = null;

        for (Room r : roomsList) {
            if (r.getName().equals(room)) {
                tmpRoom = r;
            }
        }

        if (tmpRoom == null) {
            tmpRoom = roomsList.get(0);
        }


        // GERÄTETYP AUSWAHL
        //####################

      /*
      "Bewegungsmelder",
      "Fenstersensor",
      "Feuchtigkeit",
      "Licht RGB",
      "Licht Weiß",
      "Steckdose",
      "Steckdose mit Verbrauch",
      "Temperatur",
      "Türsensor",
      "Wetterstation"};*/

        if (!createNewDevice) {
            deviceList.remove(deviceToEditAdapterPos);
        }

        if (category.equals(catArray[0])) {
            // Bewegungsmelder
            deviceToEdit = new MovementSensor(LIST_ITEM_MOVEMENT_SENSOR, this, "1", false, deviceName, tmpRoom, movementSens, getString(R.string.sensor_last_action), deviceTopic);
        } else if (category.equals(catArray[1])) {
            // Fenstersensor
            deviceToEdit = new WindowSensor(LIST_ITEM_WINDOW_SENSOR, this, "1", false, deviceName, tmpRoom, windowSens, getString(R.string.sensor_last_action), deviceTopic);
        } else if (category.equals(catArray[2])) {
            // Feuchtigkeit
            deviceToEdit = new HumiditySensor(LIST_ITEM_HUMIDITY, this, "1", false, deviceName, tmpRoom, humidity, 80, deviceTopic);
        } else if (category.equals(catArray[3])) {
            // RGB Licht
            deviceToEdit = new RGBLamp(LIST_ITEM_LAMP_RGB, this, "1", false, deviceName, tmpRoom, lamps, 100, getResources().getColor(R.color.white), getString(R.string.off), deviceTopic);
        } else if (category.equals(catArray[4])) {
            // Weißes Licht
            deviceToEdit = new WhiteLamp(LIST_ITEM_LAMP_WHITE, this, "1", false, deviceName, tmpRoom, lamps, 100, getString(R.string.on), deviceTopic);
        } else if (category.equals(catArray[5])) {
            // Steckdose
            deviceToEdit = new Plug(LIST_ITEM_PLUG, this, "1", false, deviceName, tmpRoom, plugs, getString(R.string.off), deviceTopic);
        } else if (category.equals(catArray[6])) {
            // Steckdose mit Verbrauch
            deviceToEdit = new PlugWithConsumption(LIST_ITEM_PLUG_CONSUMPTION, this, "1", false, deviceName, tmpRoom, plugs, getString(R.string.off), "24", deviceTopic);
        } else if (category.equals(catArray[7])) {
            // Temperatur
            deviceToEdit = new Temp(LIST_ITEM_TEMP, this, "1", false, deviceName, tmpRoom, temperature, 24, deviceTopic);
        } else if (category.equals(catArray[8])) {
            // Türsensor
            deviceToEdit = new DoorSensor(LIST_ITEM_DOOR_SENSOR, this, "1", false, deviceName, tmpRoom, doorSens, getString(R.string.sensor_last_action), deviceTopic);
        } else if (category.equals(catArray[9])) {
            // Wetterstation
        } else {
            //
        }


        if (!createNewDevice) {
            deviceList.add(deviceToEditAdapterPos, deviceToEdit);
        } else {
            deviceList.add(deviceToEdit);
        }

        // da sich das topic geändert haben könnte
        unsubscribeMQTTTopic(deviceToEdit);
        subscribeMQTTTopic(deviceToEdit);

        notifyOnDataChangedForFragments();
    }

    /**
     * Die Spinner für den Dialog mit Werten füllen
     *
     * @param category
     * @param room
     * @param spinnerArrayCategories
     * @param spinnerArrayRooms
     */
    private void setupSpinnersForNewDeviceDialog(Spinner category, Spinner room, final String[] spinnerArrayCategories, String[] spinnerArrayRooms) {

        Arrays.sort(spinnerArrayCategories);
        Arrays.sort(spinnerArrayRooms);

        final ArrayAdapter spinnerArrayAdapterCategories = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArrayCategories);

        ArrayAdapter spinnerArrayAdapterRooms = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArrayRooms);

        category.setAdapter(spinnerArrayAdapterCategories);
        room.setAdapter(spinnerArrayAdapterRooms);
    }

    /**
     * Callback wenn im MQTTHandler eine Nachricht eingegangen ist
     * Zerlegung des Topics und Weitergabe an die entsprechende Methode (je nachdem, was für ein Wert ankam)
     *
     * @param topic
     * @param message
     */
    @Override
    public void onMQTTMessageArrived(String topic, String message) {

        String topicPrefix = getTopicPrefix();
        String s = "";


        if (topic.startsWith(topicPrefix)) {
            s = topic.replace(topicPrefix, "");
        }

        String[] roomTopics = Constants.MQTT_TOPICS_ROOMS;
        for (String t : roomTopics) {
            if (s.startsWith(t)) {
                s = s.replace((t + "/"), "");
            }
        }

        String[] colorArray = getResources().getStringArray(R.array.colorMqtt);
        List<String> colors = Arrays.asList(colorArray);

        if (message.equalsIgnoreCase(getString(R.string.on)) || message.equalsIgnoreCase(getString(R.string.off))) {
            handleArrivedOnOffMessage(s, message);
        } else if (colors.contains(message.substring(0, 1).toUpperCase() + message.substring(1))) {
            handleArrivedColorMessage(s, message);
        } else if (isMessageNumber(message)) {
            handleArrivedDimMessage(s, message);
        } else {
            //
        }
    }

    /**
     * Herausfinden ob RGB oder Weiße Lapme, dann den Dim-Wert setzen
     * Durch die vorherige Prüfung in isMessageNumber() können hier nur Werte zwischen 0 und 100 sein
     *
     * @param topic
     * @param message
     */
    private void handleArrivedDimMessage(String topic, String message) {

        for (Device d : deviceList) {
            if (d.getTopic().equals(topic)) {
                if (d instanceof RGBLamp) {
                    ((RGBLamp) d).setDim(Integer.parseInt(message));
                    notifyOnDataChangedForFragments();
                } else if (d instanceof WhiteLamp) {
                    ((WhiteLamp) d).setDim(Integer.parseInt(message));
                    notifyOnDataChangedForFragments();
                }
            }
        }
    }

    /**
     * Nur RGBLampen können die Farbe wechseln, also erst das passende Gerät suchen, dann auf instanceOf prüfen
     *
     * @param topic
     * @param message
     */
    private void handleArrivedColorMessage(String topic, String message) {

        for (Device d : deviceList) {
            if (d.getTopic().equals(topic)) {
                if (d instanceof RGBLamp) {
                    ((RGBLamp) d).setColorByName(message);
                    notifyOnDataChangedForFragments();
                }
            }
        }
    }


    /**
     * Sucht das passende Gerät anhand des topics und setzt es auf an oder aus
     *
     * @param topic
     * @param message
     */
    private void handleArrivedOnOffMessage(String topic, String message) {

        for (Device d : deviceList) {
            if (d.getTopic().equals(topic)) {
                d.setOn(message.equalsIgnoreCase(getString(R.string.on)));
                notifyOnDataChangedForFragments();
            }
        }
    }

    /**
     * Fragmenten bescheid sagen, dass sich etwas geändert hat und sie die View refresehn sollen
     */
    public void notifyOnDataChangedForFragments() {

        if (deviceSingleSortListFragment != null) {
            deviceSingleSortListFragment.onDataHasChanged();
        }

        if (allFragment != null) {
            allFragment.onDataHasChanged();
        }
    }

    /**
     * Kontrolliert ob ein String eine Zahl ist und falls ja, ob diese Zahl im Dim-Bereich von 0-100 liegt
     *
     * @param message zu parsener String
     * @return
     */
    private boolean isMessageNumber(String message) {

        boolean isNumber = true;
        int i = -1;

        // Wenn es sich parsen lässt, ist es eine Zahl
        try {
            i = Integer.parseInt(message);
        } catch (NumberFormatException e) {
            isNumber = false;
        }

        // Erlaubte Dim-Werte
        if (isNumber) {
            if (i < 0 || i > 100) {
                isNumber = false;
            }
        }

        return isNumber;
    }

    public void setSnackbarTextSize(Snackbar s) {
        ViewGroup group = (ViewGroup) s.getView();
        for (int i = 0; i < group.getChildCount(); i++) {
            View v = group.getChildAt(i);
            if (v instanceof TextView) {
                TextView t = (TextView) v;
                t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }
        }
    }

    /**
     * Zeigt einen Dialog mit den Logeinträgen der bisher ein-/ausgegangenen MQTT Befehlen an.
     * Die Einträge dafür werden im MQTTHandler in die Shared Preferences gespeichert
     * Die Option zum löschen des Logs ist ebenfalls vorhanden
     */
    public void showMQTTLogDialog() {
        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_mqtt_log, null);

        ListView listView = (ListView) dialogView.findViewById(R.id.mqtt_log_list);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String log = sharedPreferences.getString("pref_mqtt_log", "");
        String[] listMqttLog = log.split("\\|", -1);

        if (listMqttLog.length == 1 && listMqttLog[0].equals("")) {
            listMqttLog[0] = getString(R.string.no_logs);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, R.id.text_mqtt_log, listMqttLog);
        listView.setAdapter(adapter);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(R.string.mqtt_log);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nix machen
                    }
                }

        );

        dialogBuilder.setNeutralButton(R.string.delete_log, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("pref_mqtt_log");
                editor.apply();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onMQTTConnectionLost() {

        Log.i(TAG, "onMQTTConnectionLost: ");
        try {
            String text = getString(R.string.connection_to_profile) + profileHandler.getCurrentProfile().getName() + getString(R.string.lost);
            getSupportActionBar().setSubtitle(text);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            Log.e(TAG, "onMQTTConnectionLost: ");
        }
    }

    @Override
    public void onMQTTConnection(boolean isConnectionSuccessful, boolean forcedAppEntering, String connectionIP) {
        Log.i(TAG, "onMQTTConnection: " + isConnectionSuccessful + "  " + connectionIP);
    }

    /**
     * Auf das Topic eines Geräts subscriben
     *
     * @param d
     */
    private void subscribeMQTTTopic(Device d) {

        String prefix = getTopicPrefix();
        String topic = prefix + d.getRoom().getTopic() + "/" + d.getTopic();
        mqttHandler.subscribeToTopic(topic);

    }

    /**
     * Auf Topics aller Geräte subscriben
     */
    private void subscribeMQTTTopics() {
        for (Device d : deviceList) {
            subscribeMQTTTopic(d);
        }
    }

    /**
     * Von Topics aller Geräte abmelden
     */
    private void unsubscribeAllMQTTTopics() {
        for (Device d : deviceList) {
            unsubscribeMQTTTopic(d);
        }
    }

    /**
     * Von Topic eines Geräts abmelden
     *
     * @param d
     */
    private void unsubscribeMQTTTopic(Device d) {

        String prefix = getTopicPrefix();
        String topic = prefix + d.getRoom().getTopic() + "/" + d.getTopic();
        mqttHandler.unsubscribeFromTopic(topic);

    }

    /**
     * Holt den Prefix, der evtl bei der Profilkonfiguration eingestellt wurde
     *
     * @return
     */
    public String getTopicPrefix() {
        return profileHandler.getCurrentProfile().getTopicPrefix().equals("") ? "" : (profileHandler.getCurrentProfile().getTopicPrefix() + "/");
    }

    /**
     * Die App per zurück-Button schließen. Wenn eine Kategorie angezeigt wird (Raum, Gruppe) zurück zur Startseite
     * Wenn der ViewPager nicht das AllFragment zeigt, soll erst auf dieses gewechselt werden
     */
    @Override
    public void onBackPressed() {

        if (logoutButtonHasBeenClicked) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            super.onBackPressed();
        } else if (CURRENT_FRAGMENT == DEVICE_SINGLE_SORT_LIST_FRAGMENT) {
            fragChanger(GONE, VISIBLE, false, getResources().getString(R.string.app_name), mode);
            CURRENT_FRAGMENT = 999;
        } else if (CURRENT_FRAGMENT != DEVICE_SINGLE_SORT_LIST_FRAGMENT && viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(0);
        } else {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            super.onBackPressed();
        }
    }


    /**
     * Wenn ein Menü-Element geklickt wurde
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_log:
                showMQTTLogDialog();
                break;

            case R.id.action_logout:
                logout();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * Speichern und zur StartActivity zurück
     */
    private void logout() {

        profileHandler.saveProfilesToPrefs();
        logoutButtonHasBeenClicked = true;
        onBackPressed();
    }


    /**
     * Wenn ein Raum oder Gruppe geklickt wurde
     *
     * @param name Gibt an, welche Sorte gezeigt werden soll
     */
    public void callingMainFromGridClick(String name, int mode) {

        fragChanger(VISIBLE, GONE, true, name, mode);
    }

    /**
     * Wird vom ViewHolder aufgerufen, wenn auf den Switch geklickt wurde
     * Weitergabe der Adapter Position an das derzeit sichtbare Fragment
     *
     * @param adapterPosition
     * @param isChecked
     */
    public void switchInItemHasBeenClicked(int adapterPosition, boolean isChecked) {

        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            allFragment.switchTheSwitch(adapterPosition, isChecked);
        } else {
            deviceSingleSortListFragment.switchTheSwitch(adapterPosition, isChecked);
        }
    }

    /**
     * Wenn statt der Gesamtliste nur ein Raum oder Gruppe angezeigt wird, unterscheiden sich die Adapter Positionen.
     * Wenn der Klick im AllFragment erfolgte, passt die Adapter Position. Wenn er aus der Liste eines Raums oder Gruppe
     * erfolgt, muss erst der (eindeutige) Name des Geräts herausgefunden werden um es dann in der Gesamtliste zu finden und zu ändern.
     *
     * @param adapterPosition
     */
    public void editButtonHasBeenClicked(final int adapterPosition) {

        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            editADeviceByItsName(deviceList.get(adapterPosition).getName());
        } else {
            deviceSingleSortListFragment.editButtonHasBeenClicked(adapterPosition);
        }
    }

    public void editADeviceByItsName(String deviceName) {

        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getName().equals(deviceName)) {
                deviceToEdit = deviceList.get(i);
                deviceToEditAdapterPos = i;
                showDeviceEditorDialog();
            }
        }
    }


    /**
     * Wird vom ViewHolder aufgerufen, wenn darauf geklickt wurde
     * Weitergabe der Adapter Position an das derzeit sichtbare Fragment
     *
     * @param adapterPosition
     * @param listItemType
     * @param view
     */
    public void listItemHasBeenClicked(int adapterPosition, int listItemType, View view) {

        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            allFragment.openDialog(adapterPosition, listItemType);
        } else {
            deviceSingleSortListFragment.openDialog(adapterPosition, listItemType);
        }
    }

    private void showUndoSnackbar(final Device d, final int adapterPosition) {

        Snackbar undoDeleteSnackbar = Snackbar
                .make(coordinatorLayout, d.getName() + getString(R.string.was_deleted), 5000)
                .setActionTextColor(Color.RED)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (deviceToEditAdapterPos != -1) {
                            deviceList.add(adapterPosition, d);
                        } else {
                            deviceList.add(d);
                        }
                        notifyOnDataChangedForFragments();
                    }
                });

        setSnackbarTextSize(undoDeleteSnackbar);
        undoDeleteSnackbar.show();
    }

    public MQTTHandler getMqttHandler() {
        return mqttHandler;
    }

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public ArrayList<Room> getRoomsList() {
        return roomsList;
    }

    public void setCURRENT_LIST_CATEGORY(int CURRENT_LIST_CATEGORY) {
        this.CURRENT_LIST_CATEGORY = CURRENT_LIST_CATEGORY;
    }

    /**
     * Ist für das TabLayout nötig
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final FragmentManager fragmentManager;
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();


        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            fragmentManager = manager;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

    }
}
