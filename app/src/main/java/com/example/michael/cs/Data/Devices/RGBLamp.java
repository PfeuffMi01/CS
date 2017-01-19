package com.example.michael.cs.Data.Devices;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
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
import com.thebluealliance.spectrum.SpectrumPalette;

import static com.example.michael.cs.List_Stuff.ListItem.TAG;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class RGBLamp extends Lamp {

    public String[] colorNames;
    public int[] colorIntVals;

    public String status;
    public int selectedColor;
    public String selectedColorName;


    public RGBLamp(int deviceType, Context context, String _id, boolean isOn, String name, Room room, Group group, int dim, int selectedColor, String status, String topic) {
        super(deviceType, context, _id, isOn, name, room, group, dim, topic);

        setSelectedColor(selectedColor);
        this.status = status;
    }

    public String getSelectedColorName() {
        return selectedColorName;
    }

    public void setSelectedColorName(String selectedColorName) {
        this.selectedColorName = selectedColorName;
    }


    public int getSelectedColor() {
        Log.i(TAG, "getSelectedColor: " + selectedColor);
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {

        colorNames = context.getResources().getStringArray(R.array.colorMqtt);
        colorIntVals = context.getResources().getIntArray(R.array.colorIntVals);

        this.selectedColor = selectedColor;

        for (int i = 0; i < colorIntVals.length; i++) {
            if (selectedColor == colorIntVals[i]) {
                selectedColorName = colorNames[i];
            }
        }

        Log.i(TAG, "setSelectedColor: " + selectedColor + " called " + selectedColorName);
    }

    public void setColorByName(String name) {

        colorNames = context.getResources().getStringArray(R.array.colorMqtt);
        colorIntVals = context.getResources().getIntArray(R.array.colorIntVals);

        for (int i = 0; i < colorNames.length; i++) {
            if (colorNames[i].equalsIgnoreCase(name)) {
                Log.i(TAG, "setColorByName: " + colorNames[i]);
                setSelectedColor(colorIntVals[i]);
            }
        }
    }

    public void setSelectedColor(MainActivity mainActivity, int selectedColor) {

        colorNames = context.getResources().getStringArray(R.array.colorMqtt);
        colorIntVals = context.getResources().getIntArray(R.array.colorIntVals);

        this.selectedColor = selectedColor;

        for (int i = 0; i < colorIntVals.length; i++) {
            if (selectedColor == colorIntVals[i]) {
                selectedColorName = colorNames[i];
                mqttBrokerNotifier(mainActivity, selectedColorName);
            }
        }

        Log.i(TAG, "setSelectedColor: " + selectedColor + " called " + selectedColorName);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void showDialogForThisDevice(final MainActivity mainActivity, OnDataChangedListener dataChangedListener, final int adapterPosition) {
//        mainActivity.startService(new Intent(context, MQTTService.class));
        final OnDataChangedListener listener = dataChangedListener;


        final LayoutInflater inflater = mainActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rbg_lamp, null);

        final SpectrumPalette spectrumPalette = (SpectrumPalette) dialogView.findViewById(R.id.spectrum_palette);
        final SeekBar dimSeek = (SeekBar) dialogView.findViewById(R.id.seek_dim);
        final TextView tvDimVal = (TextView) dialogView.findViewById(R.id.tv_dim_val);

        spectrumPalette.setSelectedColor(getSelectedColor());
        tvDimVal.setText(getDim() + "%");

        spectrumPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
                                                       @Override
                                                       public void onColorSelected(@ColorInt int color) {
                                                           deviceActivator(mainActivity);

                                                           for (int i = 0; i < colorIntVals.length; i++) {
                                                               if (color == colorIntVals[i]) {
                                                                   setSelectedColor(mainActivity, colorIntVals[i]);
                                                               }
                                                           }
                                                           listener.onDataHasChanged(adapterPosition);
                                                       }
                                                   }
        );

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
                                                   mqttBrokerNotifier(mainActivity, dim);

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
        Toast.makeText(m, s, Toast.LENGTH_SHORT).show();
    }

    private void deviceActivator(MainActivity mainActivity) {
        if (!isOn()) {
            setOn(true);
//            mqttBrokerNotifier(mainActivity, "/on");
        }
    }

    public void mqttBrokerNotifier(MainActivity mainActivity, String message) {

        String topic = getRoom().getTopic() + "/" + this.getTopic();

        mainActivity.getMqttHandler().mqttPublish(topic, message);
        Log.i(TAG, "mqttBrokerNotifier: " + topic + message);
    }

}
