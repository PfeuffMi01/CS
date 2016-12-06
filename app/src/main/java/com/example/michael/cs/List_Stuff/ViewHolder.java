package com.example.michael.cs.List_Stuff;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Vaterklasse für {@link ViewHolderGroup} und {@link ViewHolderRoom}
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "ViewHolder";
    public ViewHolder(View itemView) {
        super(itemView);
    }

    public void bindType(ListItem item) {
    }
}
