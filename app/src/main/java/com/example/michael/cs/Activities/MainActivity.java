package com.example.michael.cs.Activities;

import android.animation.Animator;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.example.michael.cs.Interfaces.OnConnectionListener;
import com.example.michael.cs.MQTTHandler;
import com.example.michael.cs.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.attr.mode;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.michael.cs.Constants.GROUP_DOOR_SENSOR;
import static com.example.michael.cs.Constants.GROUP_HUMIDITY;
import static com.example.michael.cs.Constants.GROUP_LAMPS;
import static com.example.michael.cs.Constants.GROUP_MOVEMENT_SENSOR;
import static com.example.michael.cs.Constants.GROUP_PLUGS;
import static com.example.michael.cs.Constants.GROUP_TEMP;
import static com.example.michael.cs.Constants.GROUP_WINDOW_SENSOR;
import static com.example.michael.cs.Constants.MQTT_CONNECTION_ERROR_NOTI_ID;
import static com.example.michael.cs.Constants.MQTT_TOPIC_BEDROOM;
import static com.example.michael.cs.Constants.MQTT_TOPIC_DOOR;
import static com.example.michael.cs.Constants.MQTT_TOPIC_FLOOR;
import static com.example.michael.cs.Constants.MQTT_TOPIC_GARAGE;
import static com.example.michael.cs.Constants.MQTT_TOPIC_GARDEN;
import static com.example.michael.cs.Constants.MQTT_TOPIC_HUMIDITY;
import static com.example.michael.cs.Constants.MQTT_TOPIC_KITCHEN;
import static com.example.michael.cs.Constants.MQTT_TOPIC_LIGHT;
import static com.example.michael.cs.Constants.MQTT_TOPIC_LIVINGROOM;
import static com.example.michael.cs.Constants.MQTT_TOPIC_MOTION;
import static com.example.michael.cs.Constants.MQTT_TOPIC_OFFICE;
import static com.example.michael.cs.Constants.MQTT_TOPIC_SOCKET;
import static com.example.michael.cs.Constants.MQTT_TOPIC_TEMPERATURE;
import static com.example.michael.cs.Constants.MQTT_TOPIC_WINDOW;
import static com.example.michael.cs.Constants.ROOM_BED;
import static com.example.michael.cs.Constants.ROOM_GARAGE;
import static com.example.michael.cs.Constants.ROOM_GARDEN;
import static com.example.michael.cs.Constants.ROOM_HALLWAY;
import static com.example.michael.cs.Constants.ROOM_KITCHEN;
import static com.example.michael.cs.Constants.ROOM_LIVING;
import static com.example.michael.cs.Constants.ROOM_OFFICE;
import static com.example.michael.cs.Constants.STATUS_OK;

public class MainActivity extends AppCompatActivity implements OnConnectionListener {
    private static final String TAG = "MainActivity";
    public static final int ALL_FRAGMENT = 0;
    public static final int ROOM_FRAGMENT = 1;
    public static final int GROUP_FRAGMENT = 2;
    public static final int DEVICE_SINGLE_SORT_LIST_FRAGMENT = 3;

    public int CURRENT_FRAGMENT;
    public int CURRENT_LIST_CATEGORY;

    private AllFragment allFragment;
    private RoomFragment roomFragment;
    private GroupFragment groupFragment;

    private FrameLayout fragContainer;

    private ArrayList<Room> roomsList;
    private ArrayList<Group> groupList;

    private ArrayList<Device> deviceList;
    private DeviceSingleSortListFragment deviceSingleSortListFragment;

    private Snackbar snackbar;

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private ImageView connectionFailImg;
    private RelativeLayout mqttLoadingLayout;
    private ProgressBar progressBar;
    private TextView loadingText;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MQTTHandler mqttHandler;



    /*
    ############################## LIFECYCLE #################################################
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_main);

        initLoadingUI();

        initMQTT();
        initExampleData();
        initFragment();
        initTabs();
    }

    /**
     * Lifecycle Aufruf bevor die App beendet wird.
     * Benachrichtigung der fehlerhaften MQTT Verbindung löschen
     */
    @Override
    protected void onDestroy() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(MQTT_CONNECTION_ERROR_NOTI_ID);
        super.onDestroy();
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

        return super.onCreateOptionsMenu(menu);
    }



    /*
    ############################## INITS #################################################
     */

    /**
     * Initiert alle Elemente, die für das Lade-Layout nötig sind und setzt Listener und Farben
     */
    private void initLoadingUI() {

        connectionFailImg = (ImageView) findViewById(R.id.connection_failure_image);
        loadingText = (TextView) findViewById(R.id.connection_text);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mqttLoadingLayout = (RelativeLayout) findViewById(R.id.mqtt_loading_layout);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);

        // Bei langem Klick auf das "Connection Failed" Bild die App zwingen trotzdem zu starten
        connectionFailImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onMQTTConnection(true, true);
                return true;
            }
        });
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


        getSupportActionBar().setSubtitle("Verbunden mit tcp://schlegel2.ddns.net:1883");


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
                    CURRENT_FRAGMENT = ALL_FRAGMENT;
                } else {
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
     * Holen des Singleton Objekts
     * Setzen des Listeners
     */
    private void initMQTT() {
        mqttHandler = MQTTHandler.getInstance(getApplicationContext());
        mqttHandler.setOnConnectionListener(this);
        tryToConnectToMQTTBroker();
    }

    /**
     * Erstellen aller Räume, Gruppen und Geräte
     * TODO Nur die wirklich verfügbaren Geräte anlegen (Status vom Server)
     */
    private void initExampleData() {

        roomsList = new ArrayList<>();
        groupList = new ArrayList<>();
        deviceList = new ArrayList<>();

        Room livingRoom = new Room(R.drawable.living_room, ROOM_LIVING, MQTT_TOPIC_LIVINGROOM);
        Room bedRoom = new Room(R.drawable.bed_room, ROOM_BED, MQTT_TOPIC_BEDROOM);
        Room garageRoom = new Room(R.drawable.garage_room, ROOM_GARAGE, MQTT_TOPIC_GARAGE);
        Room hallwayRoom = new Room(R.drawable.hallway, ROOM_HALLWAY, MQTT_TOPIC_FLOOR);
        Room gardenRoom = new Room(R.drawable.garden_room_, ROOM_GARDEN, MQTT_TOPIC_GARDEN);
        Room kitchenRoom = new Room(R.drawable.kitchen_room, ROOM_KITCHEN, MQTT_TOPIC_KITCHEN);
        Room officeRoom = new Room(R.drawable.office_roomm, ROOM_OFFICE, MQTT_TOPIC_OFFICE);


        roomsList.add(livingRoom);
        roomsList.add(bedRoom);
        roomsList.add(garageRoom);
        roomsList.add(hallwayRoom);
        roomsList.add(gardenRoom);
        roomsList.add(kitchenRoom);
        roomsList.add(officeRoom);
        Collections.sort(roomsList);

        Group lamps = new Group(GROUP_LAMPS, R.drawable.lamps_group);
        Group plugs = new Group(GROUP_PLUGS, R.drawable.plug_group);
        Group movementSens = new Group(GROUP_MOVEMENT_SENSOR, R.drawable.movement_sens);
        Group doorSens = new Group(GROUP_DOOR_SENSOR, R.drawable.door_sens);
        Group windowSens = new Group(GROUP_WINDOW_SENSOR, R.drawable.window_sens);
        Group humidity = new Group(GROUP_HUMIDITY, R.drawable.humidity);
        Group temperature = new Group(GROUP_TEMP, R.drawable.temp);

        groupList.add(lamps);
        groupList.add(plugs);
        groupList.add(movementSens);
        groupList.add(doorSens);
        groupList.add(windowSens);
        groupList.add(humidity);
        groupList.add(temperature);
        Collections.sort(groupList);

        deviceList.add(new RGBLamp(this, "rgblamp1", false, "Philips LED RGB Lampe", livingRoom, lamps, 0, getResources().getColor(R.color.white), STATUS_OK, MQTT_TOPIC_LIGHT));
        deviceList.add(new WhiteLamp(this, "whitelamp1", false, "Philips LED weiss ", officeRoom, lamps, 12, STATUS_OK, MQTT_TOPIC_LIGHT));
        deviceList.add(new RGBLamp(this, "rgblamp2", false, "Osram LED RGB Lampe ", kitchenRoom, lamps, 50, getResources().getColor(R.color.white), STATUS_OK, MQTT_TOPIC_LIGHT));

        deviceList.add(new Plug(this, "plug", false, "Homematic Steckdose", bedRoom, plugs, STATUS_OK, MQTT_TOPIC_SOCKET));

        deviceList.add(new MovementSensor(this, "move1", true, "Philips Bewegungssensor", hallwayRoom, movementSens, "17.11.16 12:45 Uhr", MQTT_TOPIC_MOTION));

        deviceList.add(new DoorSensor(this, "door1", true, "Türsensor ", hallwayRoom, doorSens, STATUS_OK, MQTT_TOPIC_DOOR));
        deviceList.add(new WindowSensor(this, "window1", true, "Fenstersensor ", livingRoom, windowSens, STATUS_OK, MQTT_TOPIC_WINDOW));

        deviceList.add(new MovementSensor(this, "move2", true, "Homematic Bewegungssensor", garageRoom, movementSens, "16.11.16 13:32 Uhr", MQTT_TOPIC_MOTION));

        deviceList.add(new Temp(this, "temp", true, "Homematic Wetterstation", gardenRoom, temperature, 5, MQTT_TOPIC_TEMPERATURE));
        deviceList.add(new HumiditySensor(this, "humidity", true, "Homematic Wetterstation", gardenRoom, humidity, 75, MQTT_TOPIC_HUMIDITY));

        deviceList.add(new PlugWithConsumption(this, "plug2", false, "Elgato Steckdose", bedRoom, plugs, STATUS_OK, "5000", MQTT_TOPIC_SOCKET));

        deviceList.add(new RGBLamp(this, "ledBand", false, "LED-Band RGB", bedRoom, lamps, 12, getResources().getColor(R.color.white), STATUS_OK, MQTT_TOPIC_LIGHT));

        for (Device device : deviceList) {
            Log.i(TAG, "initExampleData: " + device.getName() + " " + device.getTopic());
        }
    }



    /*
    ############################## UI & VIEWS #################################################
     */

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

    /**
     * Ist für das TabLayout nötig
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final FragmentManager fragmentManager;
        private Fragment fragmentAtPos0;
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

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

    /*
    ############################## MQTT #################################################
     */

    /**
     * Verbindungsversuch beginnt nacht 3 Sekunden, damit man die Progressbar für eine kurze Zeit zu Gesicht bekommt
     * Lade-Layout wieder bereit machen
     */
    private void tryToConnectToMQTTBroker() {

        connectionFailImg.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(VISIBLE);
        loadingText.setText("Verbindung wird aufgebaut...");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mqttHandler.connect();
            }
        }, 3000);
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
    public void onMQTTConnection(boolean isConnectionSuccessful, boolean forcedAppEntering) {

        if (isConnectionSuccessful) {

            Log.i(TAG, "onMQTTConnection: SUCCESS");

            if (snackbar != null) {
                snackbar.dismiss();
            }

            mqttLoadingLayout.setVisibility(GONE);
            appBarLayout.setVisibility(VISIBLE);
            viewPager.setVisibility(VISIBLE);

            if (getSupportActionBar() != null && !forcedAppEntering) {
                getSupportActionBar().setSubtitle("Verbunden mit tcp://schlegel2.ddns.net:1883");
            }

            animateLoadingLayout();
        } else {

            Log.i(TAG, "onMQTTConnection: NO SUCCESS");
            connectionFailImg.setVisibility(VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            loadingText.setText("");

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
        }

    }

    private void animateLoadingLayout() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            int cx = (mqttLoadingLayout.getLeft() + mqttLoadingLayout.getRight()) / 2;
            int cy = (mqttLoadingLayout.getTop() + mqttLoadingLayout.getBottom()) / 2;
            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim = null;

            anim = ViewAnimationUtils.createCircularReveal(mqttLoadingLayout, cx, cy, 0, finalRadius);


            anim.setDuration(2000);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {


                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();

        }
    }




    /*
    ############################## USER INTERACTION ABFAGEN #################################################
     */

    /**
     * Die App per zurück-Button schließen. Wenn eine Kategoerie angezeigt wird (Raum, Gruppe) zurück zur Startseite
     */
    @Override
    public void onBackPressed() {

        if (CURRENT_FRAGMENT == DEVICE_SINGLE_SORT_LIST_FRAGMENT) {

            fragChanger(GONE, VISIBLE, false, getResources().getString(R.string.app_name), mode);
            CURRENT_FRAGMENT = 999;
        } else {
            super.onBackPressed();
        }
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

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
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


    /*
    ############################## GETTER & SETTER #################################################
     */

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

    public int getCURRENT_LIST_CATEGORY() {
        return CURRENT_LIST_CATEGORY;
    }

    public void setCURRENT_LIST_CATEGORY(int CURRENT_LIST_CATEGORY) {
        this.CURRENT_LIST_CATEGORY = CURRENT_LIST_CATEGORY;
    }
}
