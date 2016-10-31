package com.example.michael.cs.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michael.cs.Data.Rooms.BathRoom;
import com.example.michael.cs.Data.Rooms.BedRoom;
import com.example.michael.cs.Data.Rooms.DiningRoom;
import com.example.michael.cs.Data.Rooms.GarageRoom;
import com.example.michael.cs.Data.Rooms.LivingRoom;
import com.example.michael.cs.Data.Rooms.Room;
import com.example.michael.cs.R;
import com.example.michael.cs.RecyclerAdapterRooms;

import java.util.ArrayList;


public class RoomFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private View view;
    private RecyclerAdapterRooms adapter;
    private ArrayList<Room> roomsList;


    public RoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance(String param1, String param2) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        view = inflater.inflate(R.layout.fragment_room, container, false);

        initExampleRooms();
        initRecyclerView();

        return view;
    }

    private void initExampleRooms() {

        roomsList = new ArrayList<>();
        LivingRoom lR = new LivingRoom(R.drawable.living_room, "Wohnzimmer");
        BedRoom bR = new BedRoom(R.drawable.bed_room, "Schlafzimmer");
        GarageRoom gR = new GarageRoom(R.drawable.garage_room, "Garage");
        BathRoom bathR = new BathRoom(R.drawable.bath_room, "Badezimmer");
        DiningRoom dR = new DiningRoom(R.drawable.dining_room, "Esszimmer");

        roomsList.add(lR);
        roomsList.add(bR);
        roomsList.add(gR);
        roomsList.add(bathR);
        roomsList.add(dR);
    }

    private void initRecyclerView() {


        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_rooms);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
      

        adapter = new RecyclerAdapterRooms(getContext(), roomsList);
        recyclerView.setAdapter(adapter);
    }

}
