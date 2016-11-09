package com.example.michael.cs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michael.cs.Data.Devices.Device;

import java.util.ArrayList;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class RecyclerAdapterAll extends RecyclerView.Adapter<ViewHolder> {


    private static final String TAG = "RecyclerAdapterGroups";
    private ArrayList<Device> deviceList;
    private Context context;


    public RecyclerAdapterAll(Context context, ArrayList<Device> devices) {

        this.context = context;
        this.deviceList = devices;
    }


    /**
     * Creates a new ViewHolder depending on its ListItem type
     *
     * @return Retruns the created ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = null;

        Log.i(TAG, "onCreateViewHolder: " + type);

        view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recycler_grid_item, viewGroup, false);
        return new ViewHolderGroup(context, view);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        int type = getItemViewType(pos);

       /* ListItem item = deviceList.get(pos);
        viewHolder.bindType(item);*/
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}

