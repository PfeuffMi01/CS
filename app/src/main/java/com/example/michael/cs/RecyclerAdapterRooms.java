package com.example.michael.cs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michael.cs.Data.Rooms.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class RecyclerAdapterRooms extends RecyclerView.Adapter<ViewHolder> {


    private static final String TAG = "RecyclerAdapter";
    private List<Room> roomList;
    private Context context;


    public RecyclerAdapterRooms(Context context, ArrayList<Room> rooms) {

        this.context = context;
        this.roomList = rooms;

    }


    /**
     * Creates a new ViewHolder depending on its ListItem type
     *
     * @return Retruns the created ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = null;

        view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_room, viewGroup, false);
        return new ViewHolderRoom(context, view);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        int type = getItemViewType(pos);

        ListItemRoom item = roomList.get(pos);
        viewHolder.bindType(item);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}

