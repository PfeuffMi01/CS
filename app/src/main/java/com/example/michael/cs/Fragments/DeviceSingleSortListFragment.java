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
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.R;

import java.util.ArrayList;

import static com.example.michael.cs.ListItem.TAG;


public class DeviceSingleSortListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String sortToShow;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private ArrayList<Device> allDevicesList;
    private ArrayList<Device> onlyNeededDeviceCategory;
    private CustomAdapter adapter;


    public DeviceSingleSortListFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceSingleSortListFragment newInstance(String param1, String param2) {
        DeviceSingleSortListFragment fragment = new DeviceSingleSortListFragment();
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
            sortToShow = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device_single_sort_list, container, false);
        mainActivity = (MainActivity) getActivity();

        initDataLists();
        initRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    private void initDataLists() {

        allDevicesList = mainActivity.getDeviceList();
        if (onlyNeededDeviceCategory == null) {
            onlyNeededDeviceCategory = new ArrayList<>();
        }
        onlyNeededDeviceCategory.clear();

        //Standardeinstellung
        boolean isCategoryRoom = true;
        boolean isCategoryGroup = false;

        //Einstellung verändern, wenn es eine Gruppe ist
        if (mainActivity.getGroupList() != null) {
            for (Group group : mainActivity.getGroupList()) {
                if (group.getName().equals(sortToShow)) {

                    Log.i(TAG, "initDataLists: " + group.getName() + " " + sortToShow);

                    isCategoryGroup = true;
                    isCategoryRoom = false;
                }
            }
        }

        if (isCategoryGroup) {

            mainActivity.setCURRENT_LIST_CATEGORY(MainActivity.GROUP_FRAGMENT);

            Log.i(TAG, "initDataLists: group " + sortToShow);
            for (Device d : allDevicesList) {
                if (d.getGroup().getName().equals(sortToShow)) {
                    onlyNeededDeviceCategory.add(d);
                }
            }
        } else if (isCategoryRoom) {

            mainActivity.setCURRENT_LIST_CATEGORY(MainActivity.ROOM_FRAGMENT);
            Log.i(TAG, "initDataLists: room " + sortToShow);
            for (Device d : allDevicesList) {

                if (d.getRoom().getName().equals(sortToShow)) {
                    onlyNeededDeviceCategory.add(d);
                }
            }
        }
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_device_sinlge_sort_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new CustomAdapter(getContext(), onlyNeededDeviceCategory);
        recyclerView.setAdapter(adapter);
    }

    public void initDialogForItemClickOnSingleSortDeviceList(int adapterPosition, int listItemType) {
        onlyNeededDeviceCategory.get(adapterPosition).showDialogForThisDevice(mainActivity);
        adapter.notifyItemChanged(adapterPosition);
    }

    public void changeSwitchState(int adapterPosition, boolean b) {

       try {
        onlyNeededDeviceCategory.get(adapterPosition).setOn(b);
        adapter.notifyDataSetChanged();
       } catch (Exception e) {
           Log.e(TAG, "changeSwitchState: ");
       }
    }
}
