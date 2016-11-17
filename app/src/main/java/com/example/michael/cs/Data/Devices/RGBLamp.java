package com.example.michael.cs.Data.Devices;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.CSHelper;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;

import java.util.Calendar;

import static com.example.michael.cs.Constants.DIALOG_LISTENER_DELAY;
import static com.example.michael.cs.ListItem.TAG;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class RGBLamp extends Lamp {

    public String colorHex;
    public String status;


    public RGBLamp(String _id, boolean isOn, String name, Room room, Group group, int dim, String colorHex, String status) {
        super(_id, isOn, name, room, group, dim);

        this.colorHex = colorHex;
        this.status = status;

    }


    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void showDialogForThisDevice(final MainActivity mainActivity) {

        Calendar cal = Calendar.getInstance();
        final long systemTimeDialogStart = cal.getTimeInMillis();

        LayoutInflater inflater = mainActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rbg_lamp, null);

        final ColorPickerView colorPickerView = (ColorPickerView) dialogView.findViewById(R.id.color_picker);
        final SeekBar dimSeek = (SeekBar) dialogView.findViewById(R.id.seek_dim);
        final TextView tvDimVal = (TextView) dialogView.findViewById(R.id.tv_dim_val);

        tvDimVal.setText(getDim() + "%");

        colorPickerView.addOnColorSelectedListener
                (new OnColorSelectedListener() {
                     @Override
                     public void onColorSelected(int i) {

                         Calendar cal = Calendar.getInstance();
                         if (cal.getTimeInMillis() - systemTimeDialogStart >= DIALOG_LISTENER_DELAY) {

                             setColorHex(CSHelper.intToHex(i));
                             deviceActivator();
                         }
                     }
                 }
                );


        dimSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                Calendar cal = Calendar.getInstance();
                if (cal.getTimeInMillis() - systemTimeDialogStart >= DIALOG_LISTENER_DELAY) {

                    setDim(i);
                    tvDimVal.setText(getDim() + "%");
                    deviceActivator();
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "onStopTrackingTouch: ");
            }
        });
        //TODO Dimm Seekbar auch noch implementieren und Wert speichern

        try {
            colorPickerView.setColor(Color.parseColor(this.getColorHex()), false);
            Log.i(TAG, "showDialogForThisDevice: " + getColorHex() + " " + Color.parseColor(getColorHex()));
        } catch (Exception e) {

        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mainActivity);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(getName() + "     ID: " + get_id());

        try {
            colorPickerView.setColor(Color.parseColor(this.getColorHex()), false);
        } catch (Exception e) {
        }
        dimSeek.setProgress(this.getDim());

        dialogBuilder.setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void deviceActivator() {
        if (!isOn()) {
            setOn(true);
        }
    }

}
