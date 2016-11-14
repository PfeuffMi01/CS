package com.example.michael.cs.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.CustomAdapter;
import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.R;

import java.util.ArrayList;

import static com.example.michael.cs.ListItem.TAG;


public class AllFragment extends Fragment {

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


    public AllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static AllFragment newInstance(String param1, String param2) {
        AllFragment fragment = new AllFragment();
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
        view = inflater.inflate(R.layout.fragment_all, container, false);
        mainActivity = (MainActivity) getActivity();

        allDevicesList = mainActivity.getDeviceList();
        initRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_all);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new CustomAdapter(getContext(), allDevicesList);
        recyclerView.setAdapter(adapter);
    }

    public void initDialogForItemClickOnAllDeviceList(int adapterPosition, int listItemType) {
        allDevicesList.get(adapterPosition).showDialogForThisDevice(mainActivity);
        adapter.notifyDataSetChanged();
    }

    public void changeSwitchState(int adapterPosition, boolean b) {
        try {
            allDevicesList.get(adapterPosition).setOn(b);
            adapter.notifyItemChanged(adapterPosition);
        } catch (Exception e) {
            Log.e(TAG, "changeSwitchState: ");
        }
    }
}
