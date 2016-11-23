package com.example.michael.cs.Fragments;


import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.List_Stuff.CustomAdapter;
import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.OnListItemClick;
import com.example.michael.cs.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.michael.cs.List_Stuff.ListItem.TAG;


public class DeviceSingleSortListFragment extends Fragment implements OnListItemClick {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String sortToShow;

    // TODO: Rename and change types of parameters
    public String mParam1;
    private String mParam2;
    private View view;
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private ArrayList<Device> allDevicesList;

    private ArrayList<Device> onlyNeededDeviceCategory;
    private CustomAdapter adapter;
    private LinearLayout emptyView;
    private TextView emptyTextView;
    private ImageView emptyImageView;
    private boolean isCategoryRoom;
    private boolean isCategoryGroup;
    private int emptyViewDrawable;


    public DeviceSingleSortListFragment() {
        // Required empty public constructor
    }

    public void setSortToShow(String sortToShow) {
        this.sortToShow = sortToShow;
        initDataLists();
        initRecyclerView();
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

        emptyView = (LinearLayout) view.findViewById(R.id.empty_view);
        emptyTextView = (TextView) view.findViewById(R.id.empty_view_text);
        emptyImageView = (ImageView) view.findViewById(R.id.empty_view_icon);
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
        isCategoryRoom = true;
        isCategoryGroup = false;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_device_single_sort_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new CustomAdapter(getContext(), onlyNeededDeviceCategory);
        recyclerView.setAdapter(adapter);

        emptyViewHandler();
    }

    private void emptyViewHandler() {

        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(GONE);

            if (isCategoryGroup) {
                emptyTextView.setText("Keine " + sortToShow + " vorhanden");
            } else {
                emptyTextView.setText("Keine Geräte in diesem Raum");
            }

            emptyImageView.setImageDrawable(convertToGrayscale(getContext().getResources().getDrawable(R.drawable.sad_face)));
            emptyView.setVisibility(VISIBLE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }

    protected Drawable convertToGrayscale(Drawable drawable) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(filter);

        return drawable;
    }

    public void changeSwitchState(int adapterPosition, boolean b) {

        try {
            onlyNeededDeviceCategory.get(adapterPosition).setOn(b);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "changeSwitchState: ");
        }
    }


    @Override
    public void openDialog(int adapterPosition, int listItemType) {
        initDataLists();
        onlyNeededDeviceCategory.get(adapterPosition).showDialogForThisDevice(mainActivity);
    }
}
