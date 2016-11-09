package com.example.michael.cs;

/**
 * ViewHolder for Section Headlines
 * Gets called by the MyAdapter class
 *
 * @author Patrick Engelhardt
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Group;

/**
 * ViewHolder for ReminderData CardViews
 * Created by Patrick PC on 20.09.2015.
 */
public class ViewHolderGroup extends ViewHolder {

    private static final String TAG = "ViewHolderRoom";
    private final Context context;

    private View itemView;
    private CardView cardView;
    private TextView tVGroupTitle;
    private ImageView ivGroup;


    public ViewHolderGroup(Context context, View iV) {
        super(iV);

        this.itemView = iV;
        this.context = context;

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        tVGroupTitle = (TextView) itemView.findViewById(R.id.text);
        ivGroup = (ImageView) itemView.findViewById(R.id.image);
    }

    /**
     * Providing the content for the Views from the ListItems
     *
     * @param item ListItem to ask for content
     */
    public void bindType(ListItem item) {

        String name = ((Group) item).getName();
        int image = ((Group) item).getImage();

        Log.i(TAG, "bindType: " + name + " " + image);

        tVGroupTitle.setText(name);
        ivGroup.setImageResource(image);

    }

    /**
     * Calling MainActivity from onClick
     *
     * @param pos
     * @param cv
     */
    public void callHome(int pos, CardView cv) {
        ((MainActivity) context).callingMainFromGroupClick(pos, cv);
    }

}