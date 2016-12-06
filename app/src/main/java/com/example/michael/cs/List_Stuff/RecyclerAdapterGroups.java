package com.example.michael.cs.List_Stuff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class RecyclerAdapterGroups extends RecyclerView.Adapter<ViewHolder> {


    private static final String TAG = "RecyclerAdapterGroups";
    private List<Group> groupList;
    private Context context;


    public RecyclerAdapterGroups(Context context, ArrayList<Group> groups) {

        this.context = context;
        this.groupList = groups;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = null;

        view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recycler_grid_item, viewGroup, false);
        return new ViewHolderGroup(context, view);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        int type = getItemViewType(pos);

        ListItem item = groupList.get(pos);
        viewHolder.bindType(item);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}

