package com.example.michael.cs.Activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.michael.cs.Constants;
import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Devices.MovementSensor;
import com.example.michael.cs.Data.Devices.Plug;
import com.example.michael.cs.Data.Devices.PlugWithConsumption;
import com.example.michael.cs.Data.Devices.RGBLamp;
import com.example.michael.cs.Data.Devices.Temp;
import com.example.michael.cs.Data.Devices.WeatherStation;
import com.example.michael.cs.Data.Devices.WhiteLamp;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.Fragments.AllFragment;
import com.example.michael.cs.Fragments.DeviceSingleSortListFragment;
import com.example.michael.cs.Fragments.GroupFragment;
import com.example.michael.cs.Fragments.RoomFragment;
import com.example.michael.cs.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.mode;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.michael.cs.Constants.GROUP_LAMPS;
import static com.example.michael.cs.Constants.GROUP_MOVEMENT_SENSORT;
import static com.example.michael.cs.Constants.GROUP_PLUGS;
import static com.example.michael.cs.Constants.GROUP_TEMP;
import static com.example.michael.cs.Constants.ROOM_BATH;
import static com.example.michael.cs.Constants.ROOM_BED;
import static com.example.michael.cs.Constants.ROOM_DINING;
import static com.example.michael.cs.Constants.ROOM_GARAGE;
import static com.example.michael.cs.Constants.ROOM_GARDEN;
import static com.example.michael.cs.Constants.ROOM_HALLWAY;
import static com.example.michael.cs.Constants.ROOM_KITCHEN;
import static com.example.michael.cs.Constants.ROOM_LIVING;
import static com.example.michael.cs.Constants.STATUS_OK;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int HUE_FRAGMENT = 0;
    public static final int ALL_FRAGMENT = 1;
    public static final int ROOM_FRAGMENT = 2;
    public static final int GROUP_FRAGMENT = 3;
    public static final int DEVICE_SINGLE_SORT_LIST_FRAGMENT = 4;

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

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
        initTabs();
        initExampleData();
    }

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


    }

    private void initTabs() {

        allFragment = new AllFragment();
        roomFragment = new RoomFragment();
        groupFragment = new GroupFragment();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        TypedArray imgs = getResources().obtainTypedArray(R.array.tab_icons);

        for (int i = 0; i < imgs.length(); i++) {
            tabLayout.getTabAt(i).setIcon(imgs.getResourceId(i, -1));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllFragment(), "Alle");
        adapter.addFragment(new RoomFragment(), "Räume");
        adapter.addFragment(new GroupFragment(), "Gruppen");
        viewPager.setAdapter(adapter);
    }

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
            return fragmentTitleList.get(position);
        }
    }

    private void initExampleData() {

        roomsList = new ArrayList<>();
        groupList = new ArrayList<>();
        deviceList = new ArrayList<>();

        Room livingRoom = new Room(R.drawable.living_room, ROOM_LIVING);
        Room bedRoom = new Room(R.drawable.bed_room, ROOM_BED);
        Room garageRoom = new Room(R.drawable.garage_room, ROOM_GARAGE);
        Room bathRoom = new Room(R.drawable.bath_room, ROOM_BATH);
        Room diningRoom = new Room(R.drawable.dining_room, ROOM_DINING);
        Room hallwayRoom = new Room(R.drawable.hallway, ROOM_HALLWAY);
        Room gardenRoom = new Room(R.drawable.garden_room_, ROOM_GARDEN);
        Room kitchenRoom = new Room(R.drawable.kitchen_room, ROOM_KITCHEN);

        roomsList.add(livingRoom);
        roomsList.add(bedRoom);
        roomsList.add(garageRoom);
        roomsList.add(bathRoom);
        roomsList.add(diningRoom);
        roomsList.add(hallwayRoom);
        roomsList.add(gardenRoom);
        roomsList.add(kitchenRoom);

        Group lamps = new Group(GROUP_LAMPS, R.drawable.lamps_group);
        Group temp = new Group(GROUP_TEMP, R.drawable.temp);
        Group plugs = new Group(GROUP_PLUGS, R.drawable.plug_group);
        Group movementSens = new Group(GROUP_MOVEMENT_SENSORT, R.drawable.movement_sens);
        Group doorSens = new Group(Constants.GROUP_DOOR_SENSOR, R.drawable.door_sens);
        Group windowSens = new Group(Constants.GROUP_WINDOW_SENSOR, R.drawable.window_sens);
        Group weatherStat = new Group(Constants.GROUP_WEATHER_STATION, R.drawable.weather_station);
//        Group genericDevices = new Group(GROUP_GENERIC_DEVICES, R.drawable.generic_device);

        groupList.add(lamps);
        groupList.add(temp);
        groupList.add(plugs);
        groupList.add(movementSens);
        groupList.add(doorSens);
        groupList.add(windowSens);
        groupList.add(weatherStat);
//        groupList.add(genericDevices);

        deviceList.add(new RGBLamp("rgblamp1", false, "Lampe 1", livingRoom, lamps, 0, "#f5500c", STATUS_OK));
        deviceList.add(new RGBLamp("rgblamp2", false, "Lampe 4", garageRoom, lamps, 50, "#f5500c", STATUS_OK));
      /*  deviceList.add(new RGBLamp("rgblamp3", false, "Lampe 5", diningRoom, lamps, 60, "#f5500c", STATUS_OK));
        deviceList.add(new RGBLamp("rgblamp4", false, "Lampe 6", livingRoom, lamps, 100, "#f5500c", STATUS_OK));*/

        deviceList.add(new WhiteLamp("whitelamp1", false, "Lampe 2", bedRoom, lamps, 12, STATUS_OK));
        deviceList.add(new WhiteLamp("whitelamp2", true, "Lampe 3", garageRoom, lamps, 27, STATUS_OK));
      /*  deviceList.add(new WhiteLamp("whitelamp3", false, "Lampe 7", bathRoom, lamps, 0, STATUS_OK));
        deviceList.add(new WhiteLamp("whitelamp4", true, "Lampe 8", bathRoom, lamps, 80, STATUS_OK));*/

        deviceList.add(new Temp("temp1", true, "Temp 1", diningRoom, temp, 24));
       /* deviceList.add(new Temp("temp2", false, "Temp 2", livingRoom, temp, 10));
        deviceList.add(new Temp("temp3", true, "Temp 3", bedRoom, temp, 30));*/

        deviceList.add(new Plug("plug1", false, "Steckdose 1", bathRoom, plugs));
        deviceList.add(new PlugWithConsumption("plug2", false, "Steckdose 2", kitchenRoom, plugs, "5000"));
      /*  deviceList.add(new Plug("plug2", true, "Steckdose 2", garageRoom, plugs));
        deviceList.add(new Plug("plug3", false, "Steckdose 3", livingRoom, plugs));*/

        deviceList.add(new MovementSensor("move1", true, "Movement Sens 1", gardenRoom, movementSens, "17.11.16 12:45 Uhr"));


        deviceList.add(new WeatherStation("weather1", true, "Wetterstation", gardenRoom, weatherStat, 75, 5));

//        deviceList.add(new GenericDevice("genericdevice_" + deviceList.size(), false, "Unbekanntes Gerät " + ++GenericDevice.numberOfGenericevices, garageRoom, genericDevices));
      /*  deviceList.add(new GenericDevice("genericdevice_" + deviceList.size(), true, "Unbekanntes Gerät " + ++GenericDevice.numberOfGenericevices, bathRoom, genericDevices));
        deviceList.add(new GenericDevice("genericdevice_" + deviceList.size(), false, "Unbekanntes Gerät " + ++GenericDevice.numberOfGenericevices, bedRoom, genericDevices));*/
    }


    @Override
    public void onBackPressed() {

        if (CURRENT_FRAGMENT == DEVICE_SINGLE_SORT_LIST_FRAGMENT) {

            fragChanger(GONE, VISIBLE, false, getResources().getString(R.string.app_name), mode);
            CURRENT_FRAGMENT = 999;
        } else {
            super.onBackPressed();
        }
    }

    public void callingMainFromGridClick(String name, int mode) {

        Log.i(TAG, "handleRoomClick: " + name);

        fragChanger(VISIBLE, GONE, true, name, mode);
    }

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

    public void toolbarManager(String title, boolean show) {

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        getSupportActionBar().setDisplayShowHomeEnabled(show);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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


    public void listItemHasBeenClicked(int adapterPosition, int listItemType, View view) {
        Log.i(TAG, "listItemHasBeenClicked: " + adapterPosition + " " + view.getId());
        Log.i(TAG, "listItemHasBeenClicked: " + CURRENT_FRAGMENT);
        if (CURRENT_FRAGMENT != DEVICE_SINGLE_SORT_LIST_FRAGMENT) {
            allFragment.initDialogForItemClickOnAllDeviceList(adapterPosition, listItemType);
        } else if (CURRENT_FRAGMENT == DEVICE_SINGLE_SORT_LIST_FRAGMENT) {
            deviceSingleSortListFragment.initDialogForItemClickOnSingleSortDeviceList(adapterPosition, listItemType);
        }

    }

    public void switchInItemHasBeenClicked(int adapterPosition, boolean b) {
        if (CURRENT_FRAGMENT != DEVICE_SINGLE_SORT_LIST_FRAGMENT) {
            allFragment.changeSwitchState(adapterPosition, b);
        } else if (CURRENT_FRAGMENT == DEVICE_SINGLE_SORT_LIST_FRAGMENT) {
            deviceSingleSortListFragment.changeSwitchState(adapterPosition, b);
        }
    }
}
