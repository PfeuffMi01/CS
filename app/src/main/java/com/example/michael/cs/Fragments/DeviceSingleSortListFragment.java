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
import android.widget.Toast;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.List_Stuff.CustomAdapter;
import com.example.michael.cs.Interfaces.OnDataChangedListener;
import com.example.michael.cs.Interfaces.OnListItemClick;
import com.example.michael.cs.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.michael.cs.Constants.isDebugEnabled;
import static com.example.michael.cs.List_Stuff.ListItem.TAG;

/**
 * Zeigt in einer RecyclerView alle Geräte einer bestimmten Kategorie (Raum, Gruppe) an
 */

public class DeviceSingleSortListFragment extends Fragment implements OnListItemClick, OnDataChangedListener {

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

    /**
     * Festlegen der Kategorie, die gezeigt werden soll mit anschließedener Listenerstellung
     * @param sortToShow
     */
    public void setSortToShow(String sortToShow) {
        this.sortToShow = sortToShow;
        initDataLists();
        initRecyclerView();
    }

    /**
     * Anlegen einer Geräteliste der benötigten Kategorie
     *
     * - Eine Liste aller Geräte erstellen
     * - Dann eine kategoriespezifische Liste erstellen
     */
    private void initDataLists() {

        allDevicesList = mainActivity.getDeviceList();
        if (onlyNeededDeviceCategory == null) {
            onlyNeededDeviceCategory = new ArrayList<>();
        }
        onlyNeededDeviceCategory.clear();

        //Standardeinstellung
        isCategoryRoom = true;
        isCategoryGroup = false;

        /*
        Einstellung verändern, wenn es eine Gruppe ist
        Interieren durch alle Gruppennammen. Bei einem eqlas(sortToShow) weiß man, dass es eine Gruppe ist,
        ansonsten ein Raum
         */
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

    /**
     * Die RecyclerView mit der zuvor erstellten Geräteliste initialisieren
     */
    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_device_single_sort_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new CustomAdapter(getContext(), onlyNeededDeviceCategory);
        recyclerView.setAdapter(adapter);

        emptyViewHandler();
    }

    /**
     * Hanlder um bei einer leeren Liste das traurige Gesicht und den "Keine Geräte vorhanden" Text anzuzeigen
     */
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

    /**
     * Das Bild für die EmptyView grau einfärben
     * @param drawable
     * @return
     */
    protected Drawable convertToGrayscale(Drawable drawable) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(filter);

        return drawable;
    }

    /**
     * Wird von der MainActivity aufgerufen, nachdem der User auf einen Switch auf dem ViewHolder geklickt hat
     * Änderung des "isOn" Status des betroffenen Geräts
     * @param adapterPosition Geräteposition in der Liste
     * @param isOn neuer Zustand
     */
    public void switchTheSwitch(int adapterPosition, boolean isOn) {

        try {
            onlyNeededDeviceCategory.get(adapterPosition).setOn(isOn);
            adapter.notifyDataSetChanged();
            mainActivity.getMqttHandler().mqttPublish(onlyNeededDeviceCategory.get(adapterPosition).getTopic(), isOn ? "on" : "off");
        } catch (Exception e) {
            Log.e(TAG, "changeSwitchState: ");
        }

    }

    /**
     * Wird von der MainActivity aufgerufen, nachdem der User auf einen ViewHolder geklickt hat
     * Aufruf der showDialogForThisDevice Methode in dem betroffenen Gerät
     * @param adapterPosition Geräteposition in der Liste
     * @param listItemType
     */
    @Override
    public void openDialog(int adapterPosition, int listItemType) {

        try {
            initDataLists();
            onlyNeededDeviceCategory.get(adapterPosition).showDialogForThisDevice(mainActivity, this, adapterPosition);
        } catch (Exception e) {
            if (isDebugEnabled) {
                Toast.makeText(getContext(), "Exception: Opening dialog for " + listItemType + " at pos " + adapterPosition, Toast.LENGTH_LONG).show();
            }
        }


    }



    /*
    ######################### LISTENER #############################################
     */

    @Override
    public void onDataHasChanged() {

    }

    @Override
    public void onDataHasChanged(int position) {
        adapter.notifyItemChanged(position);
    }
}
