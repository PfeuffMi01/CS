package com.example.michael.cs.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.michael.cs.Fragments.AllFragment;
import com.example.michael.cs.Fragments.GroupFragment;
import com.example.michael.cs.Fragments.HueFragment;
import com.example.michael.cs.Fragments.RoomFragment;
import com.example.michael.cs.R;

import static com.example.michael.cs.R.id.fragment_container;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public static final int HUE_FRAGMENT = 0;
    public static final int ALL_FRAGMENT = 1;
    public static final int ROOM_FRAGMENT = 2;
    public static final int GROUP_FRAGMENT = 3;

    public int STARTPAGE_FRAGMENT = HUE_FRAGMENT;
    public int CURRENT_FRAGMENT;

    private BottomNavigationView bottomNavigationView;

    private HueFragment hueFragment;
    private AllFragment allFragment;
    private RoomFragment roomFragment;
    private GroupFragment groupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigation();

        showStartFragment();

    }

    private void showStartFragment() {

        onNavigationItemSelected(bottomNavigationView.getMenu().getItem(STARTPAGE_FRAGMENT));
        CURRENT_FRAGMENT = STARTPAGE_FRAGMENT;
    }


    /**
     * Initialisiert die drei Tabs am unteren Bildschirmrand um zwischen "Alle" "Räume" und "Gruppen" zu wechseln
     */
    private void initBottomNavigation() {
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {

            case R.id.action_hue:
                hueFragment = new HueFragment();
                fragmentTransaction.replace(fragment_container, hueFragment, "HueFragment");
                fragmentTransaction.commit();

                CURRENT_FRAGMENT = HUE_FRAGMENT;
                break;

            case R.id.action_all:
                allFragment = new AllFragment();
                fragmentTransaction.replace(fragment_container, allFragment, "AllFragment");
                fragmentTransaction.commit();

                CURRENT_FRAGMENT = ALL_FRAGMENT;
                break;
            case R.id.action_rooms:
                roomFragment = new RoomFragment();
                fragmentTransaction.replace(fragment_container, roomFragment, "RoomFragment");
                fragmentTransaction.commit();

                CURRENT_FRAGMENT = ROOM_FRAGMENT;
                break;
            case R.id.action_groups:
                groupFragment = new GroupFragment();
                fragmentTransaction.replace(fragment_container, groupFragment, "GroupFragment");
                fragmentTransaction.commit();

                CURRENT_FRAGMENT = GROUP_FRAGMENT;
                break;
        }
        return false;
    }
}
