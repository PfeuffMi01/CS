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

import static com.example.michael.cs.Constants.GROUPS;

/**
 * ViewHolder for ReminderData CardViews
 * Created by Patrick PC on 20.09.2015.
 */
public class ViewHolderGroup extends ViewHolder implements View.OnClickListener {

    private static final String TAG = "ViewHolderRoom";
    private final Context context;

    private String name;


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

        cardView.setOnClickListener(this);
    }

    /**
     * Providing the content for the Views from the ListItems
     *
     * @param item ListItem to ask for content
     */
    public void bindType(ListItem item) {

        name = ((Group) item).getName();
        int image = ((Group) item).getImage();

        Log.i(TAG, "bindType: " + name + " " + image);

        tVGroupTitle.setText(name);
        ivGroup.setImageResource(image);

    }


    @Override
    public void onClick(View view) {
        ((MainActivity) context).callingMainFromGridClick(name, GROUPS);
    }
}