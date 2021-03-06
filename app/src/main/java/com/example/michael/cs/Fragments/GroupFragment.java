package com.example.michael.cs.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.List_Stuff.RecyclerAdapterGroups;
import com.example.michael.cs.R;

import java.util.ArrayList;


public class GroupFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private MainActivity mainActivity;
    private ArrayList<Group> groupList;
    private RecyclerView recyclerView;
    private View view;
    private RecyclerAdapterGroups adapter;

    public GroupFragment() {
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

        view = inflater.inflate(R.layout.fragment_group, container, false);
        mainActivity = (MainActivity) getActivity();

        this.groupList = mainActivity.getGroupList();
        initRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Erstellen der RecyclerView für die Gruppenelemente mit {@link GridLayoutManager}
     * Die Daten werden vorher aus der {@link MainActivity geholt}
     */
    private void initRecyclerView() {


        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.room_or_group_grid_columns));
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_group);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new RecyclerAdapterGroups(getContext(), groupList);
        recyclerView.setAdapter(adapter);
    }

}
