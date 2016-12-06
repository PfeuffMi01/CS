package com.example.michael.cs.List_Stuff;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.R;

import static com.example.michael.cs.Constants.ROOMS;


public class ViewHolderRoom extends ViewHolder implements View.OnClickListener {

    private static final String TAG = "ViewHolderRoom";
    private final Context context;

    String name;

    private View itemView;
    private CardView cardView;
    private TextView tVRoomTitle;
    private ImageView ivRoom;


    public ViewHolderRoom(Context context, View iV) {
        super(iV);

        this.itemView = iV;
        this.context = context;

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        tVRoomTitle = (TextView) itemView.findViewById(R.id.text);
        ivRoom = (ImageView) itemView.findViewById(R.id.image);

        cardView.setOnClickListener(this);
    }

    /**
     * Providing the content for the Views from the ListItems
     *
     * @param item ListItem to ask for content
     */
    public void bindType(ListItem item) {

        name = ((Room) item).getName();
        int image = ((Room) item).getImage();

        Log.i(TAG, "bindType: " + name + " " + image);

        tVRoomTitle.setText(name);
        ivRoom.setImageResource(image);

    }


    @Override
    public void onClick(View view) {
        ((MainActivity) context).callingMainFromGridClick(name, ROOMS);
    }
}