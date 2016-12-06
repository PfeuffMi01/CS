package com.example.michael.cs.List_Stuff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michael.cs.Data.Room;
import com.example.michael.cs.R;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapterRooms extends RecyclerView.Adapter<ViewHolder> {


    private static final String TAG = "RecyclerAdapter";
    private List<Room> roomList;
    private Context context;


    public RecyclerAdapterRooms(Context context, ArrayList<Room> rooms) {

        this.context = context;
        this.roomList = rooms;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = null;

        view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recycler_grid_item, viewGroup, false);
        return new ViewHolderRoom(context, view);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        int type = getItemViewType(pos);

        ListItem item = roomList.get(pos);
        viewHolder.bindType(item);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}

