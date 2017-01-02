package com.example.michael.cs.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Interfaces.OnDataChangedListener;
import com.example.michael.cs.Interfaces.OnListItemClick;
import com.example.michael.cs.List_Stuff.CustomAdapter;
import com.example.michael.cs.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.michael.cs.Constants.OFF;
import static com.example.michael.cs.Constants.ON;
import static com.example.michael.cs.Constants.isDebugEnabled;
import static com.example.michael.cs.List_Stuff.ListItem.TAG;

/**
 * Zeigt in einer RecyclerView alle Geräte an
 */

public class AllFragment extends Fragment implements OnListItemClick, OnDataChangedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private ArrayList<Device> allDevicesList;
    private CustomAdapter adapter;
    private LinearLayout emptyView;


    public AllFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all, container, false);
        mainActivity = (MainActivity) getActivity();

        mainActivity.setOnDataChangedListener(this);

        emptyView = (LinearLayout) view.findViewById(R.id.empty_view_all);

        refreshList();
        initRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Von der MainActivity eine aktuelle Liste der Geräte holen
     */
    private void refreshList() {

        if (allDevicesList == null) {
            allDevicesList = new ArrayList<>();
        }

        allDevicesList = ((MainActivity) getActivity()).getDeviceList();
    }

    /**
     * Die RecyclerView mit der zuvor erstellten Geräteliste initialisieren
     */
    private void initRecyclerView() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_all);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    mainActivity.hideFab();
                } else {
                    mainActivity.showFab();
                }

            }
        });

        adapter = new CustomAdapter(getContext(), allDevicesList);
        recyclerView.setAdapter(adapter);

        emptyViewHandler();
    }

    /**
     * Wird von der MainActivity aufgerufen, nachdem der User auf einen ViewHolder geklickt hat
     * Aufruf der showDialogForThisDevice Methode in dem betroffenen Gerät
     *
     * @param adapterPosition Geräteposition in der Liste
     * @param listItemType
     */
    @Override
    public void openDialog(int adapterPosition, int listItemType) {

        try {
            refreshList();
            allDevicesList.get(adapterPosition).showDialogForThisDevice(mainActivity, this, adapterPosition);
        } catch (Exception e) {
            if (isDebugEnabled) {
//                Toast.makeText(getContext(), "Exception: Opening dialog for " + listItemType + " at pos " + adapterPosition, Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * Wird von der MainActivity aufgerufen, nachdem der User auf einen Switch auf dem ViewHolder geklickt hat
     * Änderung des "isOn" Status des betroffenen Geräts
     *
     * @param adapterPosition Geräteposition in der Liste
     * @param isOn            neuer Zustand
     */
    public void switchTheSwitch(int adapterPosition, boolean isOn) {
        try {
            allDevicesList.get(adapterPosition).setOn(isOn);
            adapter.notifyItemChanged(adapterPosition);

            String message = isOn ? ON : OFF;


            mainActivity.getMqttHandler().mqttPublish(allDevicesList.get(adapterPosition).getTopic(), message);
        } catch (Exception e) {
            Log.e(TAG, "changeSwitchState: ");
        }
    }

    /**
     * Hanlder um bei einer leeren Liste das traurige Gesicht und den "Keine Geräte vorhanden" Text anzuzeigen
     */
    private void emptyViewHandler() {

        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(GONE);
            emptyView.setVisibility(VISIBLE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }

    /*
    ######################### LISTENER #############################################
     */

    @Override
    public void onDataHasChanged() {
        refreshList();
        adapter.notifyDataSetChanged();
        emptyViewHandler();
    }

    @Override
    public void onDataHasChanged(int position) {
        adapter.notifyItemChanged(position);
        emptyViewHandler();
    }
}
