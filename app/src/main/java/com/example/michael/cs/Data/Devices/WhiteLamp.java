package com.example.michael.cs.Data.Devices;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.Interfaces.OnDataChangedListener;
import com.example.michael.cs.R;

import static com.example.michael.cs.List_Stuff.ListItem.TAG;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class WhiteLamp extends Lamp {

    public String status;

    public WhiteLamp(Context context, String _id, boolean isOn, String name, Room room, Group group, int dim, String status, String topic) {
        super(context, _id, isOn, name, room, group, dim, topic);

        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void showDialogForThisDevice(final MainActivity mainActivity, OnDataChangedListener dataChangedListener, final int adapterPosition) {

        final OnDataChangedListener listener = dataChangedListener;

        LayoutInflater inflater = mainActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_white_lamp, null);

        final SeekBar dimSeek = (SeekBar) dialogView.findViewById(R.id.seek_dim);
        final TextView tvDimVal = (TextView) dialogView.findViewById(R.id.tv_dim_val);

        tvDimVal.setText(getDim() + "%");

        dimSeek.setDrawingCacheBackgroundColor(Color.RED);

        dimSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                               @Override
                                               public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                                   tvDimVal.setText(i + "%");
                                               }

                                               @Override
                                               public void onStartTrackingTouch(SeekBar seekBar) {
                                                   Log.i(TAG, "onStartTrackingTouch: ");
                                               }

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {
                                                   Log.i(TAG, "onStopTrackingTouch: ");
                                                   setDim(seekBar.getProgress());
                                                   deviceActivator(mainActivity);

                                                   String dim = "" + seekBar.getProgress();
                                                   if (seekBar.getProgress() < 10) {
                                                       dim = "0" + dim;
                                                   }
                                                   mqttBrokerNotifier(mainActivity, "/" + dim);

                                                   listener.onDataHasChanged(adapterPosition);
                                               }
                                           }

        );

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mainActivity);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(getName() + "     ID: " + get_id());
        dimSeek.setProgress(this.getDim());

        dialogBuilder.setPositiveButton("Fertig", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }

        );

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void toaster(MainActivity m, String s) {
        Toast.makeText(m, s, Toast.LENGTH_LONG).show();
    }

    private void deviceActivator(MainActivity mainActivity) {
        if (!isOn()) {
//            mqttBrokerNotifier(mainActivity, "/on");
            setOn(true);
        }
    }

    public void mqttBrokerNotifier(MainActivity mainActivity, String message) {
        mainActivity.getMqttHandler().mqttPublish(getTopic(), message);
    }
}
