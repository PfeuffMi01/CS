package com.example.michael.cs.Activities;

import android.animation.Animator;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
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
import com.example.michael.cs.Interfaces.OnConnectionListener;
import com.example.michael.cs.Interfaces.OnConnectionLostListener;
import com.example.michael.cs.Interfaces.OnDataChangedListener;
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
import static com.example.michael.cs.Constants.GROUP_DOOR_SENSOR;
import static com.example.michael.cs.Constants.GROUP_HUMIDITY;
import static com.example.michael.cs.Constants.GROUP_LAMPS;
import static com.example.michael.cs.Constants.GROUP_MOVEMENT_SENSOR;
import static com.example.michael.cs.Constants.GROUP_PLUGS;
import static com.example.michael.cs.Constants.GROUP_TEMP;
import static com.example.michael.cs.Constants.GROUP_WINDOW_SENSOR;
import static com.example.michael.cs.Constants.LIST_ITEM_DOOR_SENSOR;
import static com.example.michael.cs.Constants.LIST_ITEM_HUMIDITY;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP_RGB;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP_WHITE;
import static com.example.michael.cs.Constants.LIST_ITEM_MOVEMENT_SENSOR;
import static com.example.michael.cs.Constants.LIST_ITEM_PLUG;
import static com.example.michael.cs.Constants.LIST_ITEM_PLUG_CONSUMPTION;
import static com.example.michael.cs.Constants.LIST_ITEM_TEMP;
import static com.example.michael.cs.Constants.LIST_ITEM_WINDOW_SENSOR;
import static com.example.michael.cs.Constants.MQTT_CONNECTION_ERROR_NOTI_ID;
import static com.example.michael.cs.Constants.MQTT_TOPIC_BEDROOM;
import static com.example.michael.cs.Constants.MQTT_TOPIC_DIVIDER;
import static com.example.michael.cs.Constants.MQTT_TOPIC_FLOOR;
import static com.example.michael.cs.Constants.MQTT_TOPIC_GARAGE;
import static com.example.michael.cs.Constants.MQTT_TOPIC_GARDEN;
import static com.example.michael.cs.Constants.MQTT_TOPIC_KITCHEN;
import static com.example.michael.cs.Constants.MQTT_TOPIC_LIVINGROOM;
import static com.example.michael.cs.Constants.MQTT_TOPIC_OFFICE;
import static com.example.michael.cs.Constants.PROFILE_DEVICES_DIV;
import static com.example.michael.cs.Constants.ROOM_ARRAY;
import static com.example.michael.cs.Constants.ROOM_BED;
import static com.example.michael.cs.Constants.ROOM_GARAGE;
import static com.example.michael.cs.Constants.ROOM_GARDEN;
import static com.example.michael.cs.Constants.ROOM_HALLWAY;
import static com.example.michael.cs.Constants.ROOM_KITCHEN;
import static com.example.michael.cs.Constants.ROOM_LIVING;
import static com.example.michael.cs.Constants.ROOM_OFFICE;

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

    private Snackbar snackbar;

    private Menu menu;

    private String currentlyConnectedServer;

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private MQTTHandler mqttHandler;
    private boolean appWasEnteredByLongClickOnNoConnectionIcon;
    private ProfileHandler profileHandler;
    private OnDataChangedListener onDataChangedListener;
    private Device deviceToEdit;
    private int deviceToEditAdapterPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileHandler = ProfileHandler.getInstance(this);
        mqttHandler = MQTTHandler.getInstance(this);

        setThisAsMqttListeners();
        initUI();
        initRoomsAndGroups();
        initFragment();
        initTabs();
        subscribeMQTTTopics();
        getSupportActionBar().setSubtitle("Verbunden zu Profil " + profileHandler.getCurrentProfile().getName());
    }

    private void setThisAsMqttListeners() {
        mqttHandler.setOnConnectionListener(this);
        mqttHandler.setOnConnectionLostListener(this);
        mqttHandler.setOnMQTTMessageArrivedListener(this);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "??? onResume: ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        mqttHandler.unregisterConnectionListeners();
        super.onPause();
    }

    /**
     * Lifecycle Aufruf bevor die App beendet wird.
     * Benachrichtigung der fehlerhaften MQTT Verbindung löschen
     */
    @Override
    protected void onDestroy() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(MQTT_CONNECTION_ERROR_NOTI_ID);

        mqttHandler.disconnect(TAG + " onDestroy");

     /*   if (mqttHandler.isConnected()) {
            Log.i(TAG, "onDestroy: ");
            mqttHandler.disconnect();
        }*/

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Menü anlegen
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    private void initUI() {

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_main);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setVisibility(VISIBLE);
        appBarLayout.setVisibility(VISIBLE);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);

    }

    private void noInternetConnectionHandler() {

        try {
            viewPager.setVisibility(GONE);
            tabLayout.setVisibility(GONE);
        } catch (Exception e) {
            Log.e(TAG, "noInternetConnectionHandler: Layout was null");
        }
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

            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected: " + position);

                if (position == ALL_FRAGMENT) {

                    fab.show();

                    CURRENT_FRAGMENT = ALL_FRAGMENT;
                    CURRENT_LIST_CATEGORY = 0;
                } else {

                    fab.hide();

                    CURRENT_FRAGMENT = DEVICE_SINGLE_SORT_LIST_FRAGMENT;

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
     * Erstellen aller Räume, Gruppen und Geräte
     * Kontrollieren, mit welchem Server man gerade verbunden ist und nur die entsprechenden Geräte dafür laden
     * TODO Nur die wirklich verfügbaren Geräte anlegen (Status vom Server)
     */
    private void initRoomsAndGroups() {

        roomsList = new ArrayList<>();
        groupList = new ArrayList<>();
        deviceList = new ArrayList<>();

        livingRoom = new Room(R.drawable.living_room, ROOM_LIVING, MQTT_TOPIC_LIVINGROOM);
        bedRoom = new Room(R.drawable.bed_room, ROOM_BED, MQTT_TOPIC_BEDROOM);
        garageRoom = new Room(R.drawable.garage_room, ROOM_GARAGE, MQTT_TOPIC_GARAGE);
        hallwayRoom = new Room(R.drawable.hallway, ROOM_HALLWAY, MQTT_TOPIC_FLOOR);
        gardenRoom = new Room(R.drawable.garden_room_, ROOM_GARDEN, MQTT_TOPIC_GARDEN);
        kitchenRoom = new Room(R.drawable.kitchen_room, ROOM_KITCHEN, MQTT_TOPIC_KITCHEN);
        officeRoom = new Room(R.drawable.office_room, ROOM_OFFICE, MQTT_TOPIC_OFFICE);

        plugs = new Group(GROUP_PLUGS, R.drawable.plug_group);
        movementSens = new Group(GROUP_MOVEMENT_SENSOR, R.drawable.movement_sens);
        doorSens = new Group(GROUP_DOOR_SENSOR, R.drawable.door_sens);
        windowSens = new Group(GROUP_WINDOW_SENSOR, R.drawable.window_sens);
        humidity = new Group(GROUP_HUMIDITY, R.drawable.humidity);
        temperature = new Group(GROUP_TEMP, R.drawable.temp);
        lamps = new Group(GROUP_LAMPS, R.drawable.lamps_group);

        roomsList.add(livingRoom);
        roomsList.add(bedRoom);
        roomsList.add(garageRoom);
        roomsList.add(hallwayRoom);
        roomsList.add(gardenRoom);
        roomsList.add(kitchenRoom);
        roomsList.add(officeRoom);

        groupList.add(plugs);
        groupList.add(movementSens);
        groupList.add(doorSens);
        groupList.add(windowSens);
        groupList.add(humidity);
        groupList.add(temperature);
        groupList.add(lamps);

      /*  deviceList.add(new RGBLamp(this, "rgblamp1", false, "Philips LED RGB Lampe", livingRoom, lamps, 0, getResources().getColor(R.color.white), STATUS_OK, MQTT_TOPIC_LIGHT));
        deviceList.add(new WhiteLamp(this, "whitelamp1", false, "Philips LED weiss ", officeRoom, lamps, 12, STATUS_OK, MQTT_TOPIC_LIGHT));
        deviceList.add(new RGBLamp(this, "rgblamp2", false, "Osram LED RGB Lampe ", kitchenRoom, lamps, 50, getResources().getColor(R.color.white), STATUS_OK, MQTT_TOPIC_LIGHT));
        deviceList.add(new Plug(this, "plug", false, "Homematic Steckdose", bedRoom, plugs, STATUS_OK, MQTT_TOPIC_SOCKET));
        deviceList.add(new MovementSensor(this, "move1", true, "Philips Bewegungssensor", hallwayRoom, movementSens, "17.11.16 12:45 Uhr", MQTT_TOPIC_MOTION));
        deviceList.add(new DoorSensor(this, "door1", true, "Türsensor ", hallwayRoom, doorSens, STATUS_OK, MQTT_TOPIC_DOOR));
        deviceList.add(new WindowSensor(this, "window1", true, "Fenstersensor ", livingRoom, windowSens, STATUS_OK, MQTT_TOPIC_WINDOW));
        //deviceList.add(new MovementSensor(this, "move2", true, "Homematic Bewegungssensor", garageRoom, movementSens, "16.11.16 13:32 Uhr", MQTT_TOPIC_MOTION));
        deviceList.add(new Temp(this, "temp", true, "Homematic Wetterstation", gardenRoom, temperature, 5, MQTT_TOPIC_TEMPERATURE));
        deviceList.add(new HumiditySensor(this, "humidity", true, "Homematic Wetterstation", gardenRoom, humidity, 75, MQTT_TOPIC_HUMIDITY));
        deviceList.add(new PlugWithConsumption(this, "plug2", false, "Elgato Steckdose", bedRoom, plugs, STATUS_OK, "5000", MQTT_TOPIC_SOCKET));
        deviceList.add(new RGBLamp(this, "ledBand", false, "LED-Band RGB", bedRoom, lamps, 12, getResources().getColor(R.color.white), STATUS_OK, MQTT_TOPIC_LIGHT));*/


        Collections.sort(roomsList);
        Collections.sort(groupList);

        loadDevicesFromProfileHandler();


     /*   for (Device device : deviceList) {
            Log.i(TAG, "initExampleData: " + device.getName() + " " + device.getTopic());
        }*/
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Lässt das Ladelayout in einer kreisförmigen Animation verschwinden
     * Nur für OS Versionen ab Lollipop
     */
    private void animateLoadingLayout() {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Log.i(TAG, "animateLoadingLayout: ");
            View myView = findViewById(R.id.mqtt_loading_layout);

            // Animationszentrum bestimmen (Mitte des Screens)
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = (myView.getTop() + myView.getBottom()) / 2;

            // den finalen Radius bestimmen
            int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

            Animator animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius, 0);

            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(600);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    letLoadingScreenDisappear();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.start();
        } else {
            letLoadingScreenDisappear();
        }
    }

    public void letLoadingScreenDisappear() {
        if (snackbar != null) {
            snackbar.dismiss();
        }

        appBarLayout.setVisibility(VISIBLE);
        viewPager.setVisibility(VISIBLE);

     /*   if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("Verbunden mit tcp://schlegel2.ddns.net:1883");
        }*/
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

        getSupportActionBar().setTitle(title);
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
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(allFragment, "Alle");
        adapter.addFragment(roomFragment, "Räume");
        adapter.addFragment(groupFragment, "Gruppen");
        viewPager.setAdapter(adapter);
    }

    public void hideFab() {
        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            fab.hide();
        }
    }

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
            dialogBuilder.setTitle("Gerät hinzufügen");
        }

        dialogBuilder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
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
                            Toast.makeText(MainActivity.this, "Folgende Zeichen sind nicht erlaubt:\n"
                                    + PROFILE_DEVICES_DIV
                                    + "   " + DATA_DIV
                                    + "   " + DEVICES_DIV
                                    + "   " + DATA_DEVICES_DIV, Toast.LENGTH_LONG).show();
                        } else if (errorEmptyName) {
                            Toast.makeText(MainActivity.this, "Bitte Gerätenamen eingeben", Toast.LENGTH_LONG).show();
                        } else if (errorEmptyTopic) {
                            Toast.makeText(MainActivity.this, "Bitte ID / Topic eingeben", Toast.LENGTH_LONG).show();
                        } else if (errorExistingTopic) {
                            Toast.makeText(MainActivity.this, "Das Topic " + deviceTopic + " gibt es bereits", Toast.LENGTH_LONG).show();
                        } else if (errorExistingName) {
                            Toast.makeText(MainActivity.this, "Dieses Gerät gibt es bereits", Toast.LENGTH_LONG).show();

                            // Wenn alle Eingaben OK sind
                        } else if (!errorEmptyName && !errorEmptyTopic && !errorExistingName && !errorExistingTopic) {
                            createOrEditDevice(createNewDevice, deviceName, deviceTopic, spinnerArrayCategories[category.getSelectedItemPosition()], spinnerArrayRooms[room.getSelectedItemPosition()]);
                        }
                    }
                }
        );
        dialogBuilder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.i(TAG, "onDismiss: ");
                deviceToEdit = null;
                deviceToEditAdapterPos = -1;
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void setupDialogForEditingDevice(TextInputEditText name, TextInputEditText topic, String[] spinnerArrayCategories, String[] spinnerArrayRooms, Spinner category, Spinner room, AlertDialog.Builder dialogBuilder) {

        dialogBuilder.setNeutralButton("Löschen", new DialogInterface.OnClickListener() {
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
                onDataChangedListener.onDataHasChanged();

                showUndoSnackbar(deletedDevice, deviceToEditAdapterPos);
            }
        });

        dialogBuilder.setTitle(deviceToEdit.getName() + " editieren");

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

    private boolean doesDeviceExist(String deviceName, String room) {

        if (deviceList == null) {
            deviceList = new ArrayList<>();
        }

        for (Device d : deviceList) {
            if (d.getName().equals(deviceName) && d.getRoom().getName().equals(room)) {
                Log.i(TAG, "doesDeviceExist: found exsiting device");
                return true;
            }
        }
        return false;
    }

    private void loadDevicesFromProfileHandler() {
        Log.i(TAG, "loadDevicesFromProfileHandler: " + profileHandler.getCurrentProfile().getStringForSharedPreferences());

        this.deviceList = profileHandler.getCurrentProfile().getDeviceList();

        for (Device d : deviceList) {
            Log.i(TAG, "loadDevicesFromProfileHandler: " + d.getName());
        }
    }

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
            deviceToEdit = new MovementSensor(LIST_ITEM_MOVEMENT_SENSOR, this, "1", false, deviceName, tmpRoom, movementSens, "Vorhin", deviceTopic);
        } else if (category.equals(catArray[1])) {
            // Fenstersensor
            deviceToEdit = new WindowSensor(LIST_ITEM_WINDOW_SENSOR, this, "1", false, deviceName, tmpRoom, windowSens, "Vorhin", deviceTopic);
        } else if (category.equals(catArray[2])) {
            // Feuchtigkeit
            deviceToEdit = new HumiditySensor(LIST_ITEM_HUMIDITY, this, "1", false, deviceName, tmpRoom, humidity, 80, deviceTopic);
        } else if (category.equals(catArray[3])) {
            // RGB Licht
            deviceToEdit = new RGBLamp(LIST_ITEM_LAMP_RGB, this, "1", false, deviceName, tmpRoom, lamps, 100, getResources().getColor(R.color.white), "aus", deviceTopic);
        } else if (category.equals(catArray[4])) {
            // Weißes Licht
            deviceToEdit = new WhiteLamp(LIST_ITEM_LAMP_WHITE, this, "1", false, deviceName, tmpRoom, lamps, 100, "aus", deviceTopic);
        } else if (category.equals(catArray[5])) {
            // Steckdose
            deviceToEdit = new Plug(LIST_ITEM_PLUG, this, "1", false, deviceName, tmpRoom, plugs, "aus", deviceTopic);
        } else if (category.equals(catArray[6])) {
            // Steckdose mit Verbrauch
            deviceToEdit = new PlugWithConsumption(LIST_ITEM_PLUG_CONSUMPTION, this, "1", false, deviceName, tmpRoom, plugs, "aus", "24", deviceTopic);
        } else if (category.equals(catArray[7])) {
            // Temperatur
            deviceToEdit = new Temp(LIST_ITEM_TEMP, this, "1", false, deviceName, tmpRoom, temperature, 24, deviceTopic);
        } else if (category.equals(catArray[8])) {
            // Türsensor
            deviceToEdit = new DoorSensor(LIST_ITEM_DOOR_SENSOR, this, "1", false, deviceName, tmpRoom, doorSens, "Vorhin", deviceTopic);
        } else if (category.equals(catArray[9])) {
            // Wetterstation
        }


        if (!createNewDevice) {
            deviceList.add(deviceToEditAdapterPos, deviceToEdit);
        } else {
            deviceList.add(deviceToEdit);
        }
        onDataChangedListener.onDataHasChanged();
        subscribeMQTTTopics();
    }

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

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Nur bei "Lichtern" anzeigen

                int rgbIndex = -1;
                int whiteIndex = -1;

                for (int i1 = 0; i1 < spinnerArrayCategories.length; i1++) {
                    if (spinnerArrayCategories[i1].equals("Licht RGB")) {
                        rgbIndex = i1;
                    }

                    if (spinnerArrayCategories[i1].equals("Licht Weiß")) {
                        whiteIndex = i1;
                    }
                }

                //dim.setVisibility(i == rgbIndex || i == whiteIndex ? VISIBLE : GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void setOnDataChangedListener(AllFragment allFragment) {
        onDataChangedListener = allFragment;
    }

    @Override
    public void onMQTTMessageArrived(String topic, String message) {

        String topicPrefix = getTopicPrefix();
        String s = "";

        Log.i(TAG, "onMQTTMessageArrived: ");

        if (topic.startsWith(topicPrefix)) {
            s = topic.replace(topicPrefix, "");
            Log.i(TAG, "onMQTTMessageArrived: topic starts with topicPrefix " + topicPrefix);
        }

        String[] roomTopics = Constants.MQTT_TOPICS_ROOMS;
        for (String t : roomTopics) {
            if (s.startsWith(t)) {
                Log.i(TAG, "onMQTTMessageArrived: room found: " + t);
                s = s.replace((t + "/"), "");
            }
        }

        Log.i(TAG, "onMQTTMessageArrived: " + s);

//        Toast.makeText(this, topic + " " + message, Toast.LENGTH_SHORT).show();

        String[] colorArray = getResources().getStringArray(R.array.colorMqtt);
        List<String> colors = Arrays.asList(colorArray);

        if (message.equalsIgnoreCase("an") || message.equalsIgnoreCase("aus")) {
            handleArrivedOnOffMessage(s, message);
        } else if (colors.contains(message.substring(0, 1).toUpperCase() + message.substring(1))) {
            handleArrivedColorMessage(s, message);
        } else if (isMessageNumber(message)) {
            handleArrivedDimMessage(s, message);
        } else if (false) {
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
        Log.i(TAG, "handleArrivedDimMessage: ");

        for (Device d : deviceList) {
            if (d.getTopic().equals(topic)) {
                if (d instanceof RGBLamp) {
                    ((RGBLamp) d).setDim(Integer.parseInt(message));
                    onDataChangedListener.onDataHasChanged();
                } else if (d instanceof WhiteLamp) {
                    ((WhiteLamp) d).setDim(Integer.parseInt(message));
                    onDataChangedListener.onDataHasChanged();
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
        Log.i(TAG, "handleArrivedColorMessage: ");

        for (Device d : deviceList) {
            if (d.getTopic().equals(topic)) {
                Log.i(TAG, "handleArrivedColorMessage: 1");
                if (d instanceof RGBLamp) {
                    Log.i(TAG, "handleArrivedColorMessage: 2");
                    ((RGBLamp) d).setColorByName(message);
                    Log.i(TAG, "handleArrivedColorMessage: " + ((RGBLamp) d).getSelectedColor() + " " + ((RGBLamp) d).getSelectedColorName());
                    onDataChangedListener.onDataHasChanged();
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
        Log.i(TAG, "handleArrivedOnOffMessage: ");

        for (Device d : deviceList) {
            if (d.getTopic().equals(topic)) {
                d.setOn(message.equalsIgnoreCase("an"));
                onDataChangedListener.onDataHasChanged();
            }
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
     * Verbindungsversuch beginnt nacht 0,5 Sekunden, damit man die Progressbar für eine kurze Zeit zu Gesicht bekommt
     * Lade-Layout wieder bereit machen
     */
    private void tryToConnectToMQTTBroker() {

        appBarLayout.setVisibility(GONE);
        viewPager.setVisibility(GONE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mqttHandler.connect();
            }
        }, 500);
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
            listMqttLog[0] = "Bisher keine Log-Einträge";
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, R.id.text_mqtt_log, listMqttLog);
        listView.setAdapter(adapter);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("MQTT Log");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nix machen
                    }
                }

        );

        dialogBuilder.setNeutralButton("Log löschen", new DialogInterface.OnClickListener() {
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
            String text = "Verbindung zum Profil " + profileHandler.getCurrentProfile().getName() + " verloren";
            getSupportActionBar().setSubtitle(text);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            Log.e(TAG, "onMQTTConnectionLost: ");
        }
    }

    @Override
    public void onMQTTConnection(boolean isConnectionSuccessful, boolean forcedAppEntering, String connectionIP) {
     /*   Log.i(TAG, "onMQTTConnection: ");

        appWasEnteredByLongClickOnNoConnectionIcon = forcedAppEntering;

        if (isConnectionSuccessful) {
            Log.i(TAG, "onMQTTConnection: ");
            initRoomsAndGroups();
            initFragment();
            initTabs();


            animateLoadingLayout();
            currentlyConnectedServer = connectionIP;
            getSupportActionBar().setSubtitle(appWasEnteredByLongClickOnNoConnectionIcon ? "Es besteht keine Verbindung" : connectionIP);

        } else {

            Log.i(TAG, "onMQTTConnection: NO SUCCESS");

            snackbar = Snackbar
                    .make(coordinatorLayout, "Verbindungsfehler", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.RED)
                    .setAction("ERNEUT VERSUCHEN", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tryToConnectToMQTTBroker();
                        }
                    });

            setSnackbarTextSize(snackbar);
            snackbar.show();

            if (getSupportActionBar() != null) {
                getSupportActionBar().setSubtitle("Keine Verbindung");
            }
        }*/
    }

    private void subscribeMQTTTopics() {

        String prefix = getTopicPrefix();

        Log.i(TAG, "??? subscribeMQTTTopics: " + deviceList.size());

        for (Device d : deviceList) {
            String topic = prefix + d.getRoom().getTopic() + "/" + d.getTopic();
            Log.i(TAG, "subscribeMQTTTopics: " + topic);
            mqttHandler.subscribeToTopic(topic);
        }
    }

    private void unsubscribeMQTTTopic(Device deviceToEdit) {

        String prefix = getTopicPrefix();
        String topic = prefix + deviceToEdit.getRoom().getTopic() + "/" + deviceToEdit.getTopic();
        mqttHandler.unsubscribeFromTopic(topic);

    }

    public String getTopicPrefix() {
        return profileHandler.getCurrentProfile().getTopicPrefix().equals("") ? "" : (profileHandler.getCurrentProfile().getTopicPrefix() + "/");
    }

    /**
     * Die App per zurück-Button schließen. Wenn eine Kategorie angezeigt wird (Raum, Gruppe) zurück zur Startseite
     */
    @Override
    public void onBackPressed() {

        if (CURRENT_FRAGMENT == DEVICE_SINGLE_SORT_LIST_FRAGMENT) {
            fragChanger(GONE, VISIBLE, false, getResources().getString(R.string.app_name), mode);
            CURRENT_FRAGMENT = 999;
        } else {
            Log.i(TAG, "onBackPressed: 3");
            logout();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Wenn ein Menü-ELement geklickt wurde
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

    private void logout() {

        mqttHandler.disconnect(TAG + " logout");
        profileHandler.setCurrentProfile(null);
        super.onBackPressed();
    }


    /**
     * Wenn ein Raum oder Gruppe geklickt wurde
     *
     * @param name Gibt an, welche Sorte gezeigt werden soll
     * @param mode TODO im Moement nicht benötigt
     */
    public void callingMainFromGridClick(String name, int mode) {

        Log.i(TAG, "handleRoomClick: " + name);

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

        Log.i(TAG, "switchInItemHasBeenClicked: " + adapterPosition + " " + isChecked);

        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            allFragment.switchTheSwitch(adapterPosition, isChecked);
        } else {
            deviceSingleSortListFragment.switchTheSwitch(adapterPosition, isChecked);
        }
    }

    public void editButtonHasBeenClicked(final int adapterPosition) {

        deviceToEdit = deviceList.get(adapterPosition);
        deviceToEditAdapterPos = adapterPosition;
        showDeviceEditorDialog();
    }

    private void showUndoSnackbar(final Device d, final int adapterPosition) {

        Log.i(TAG, "showUndoSnackbar: " + d.getName() + " " + d.getRoom().getName());

        Snackbar undoDeleteSnackbar = Snackbar
                .make(coordinatorLayout, d.getName() + " wurde gelöscht", 5000)
                .setActionTextColor(Color.RED)
                .setAction("Rückgängig", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (deviceToEditAdapterPos != -1) {
                            deviceList.add(adapterPosition, d);
                        } else {
                            deviceList.add(d);
                        }
                        onDataChangedListener.onDataHasChanged();

                    }
                });

        setSnackbarTextSize(undoDeleteSnackbar);
        undoDeleteSnackbar.show();
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
        Log.i(TAG, "listItemHasBeenClicked: Pos: " + adapterPosition + " Cur. Frag.: " + CURRENT_FRAGMENT);

        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            Log.i(TAG, "listItemHasBeenClicked: ALL_FRAGMENT");
            allFragment.openDialog(adapterPosition, listItemType);
        } else {
            deviceSingleSortListFragment.openDialog(adapterPosition, listItemType);
        }
    }

    public MQTTHandler getMqttHandler() {
        return mqttHandler;
    }

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }

    public String getCurrentlyConnectedServer() {
        return this.currentlyConnectedServer;
    }

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public ArrayList<Room> getRoomsList() {
        return roomsList;
    }

    public int getCURRENT_LIST_CATEGORY() {
        return CURRENT_LIST_CATEGORY;
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
        private Fragment fragmentAtPos0;

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.i(TAG, "getPageTitle: " + fragmentTitleList.get(position));
            return fragmentTitleList.get(position);
        }

    }
}
