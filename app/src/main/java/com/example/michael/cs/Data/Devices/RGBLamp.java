package com.example.michael.cs.Data.Devices;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.Interfaces.OnDataChangedListener;
import com.example.michael.cs.R;
import com.thebluealliance.spectrum.SpectrumPalette;



public class RGBLamp extends Lamp {

    public String status;
    private String[] colorNames;
    private int[] colorIntVals;
    private int selectedColor;
    private String selectedColorName;


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


    private int getSelectedColor() {
        return selectedColor;
    }

    private void setSelectedColor(int selectedColor) {

        colorNames = context.getResources().getStringArray(R.array.colorMqtt);
        colorIntVals = context.getResources().getIntArray(R.array.colorIntVals);

        this.selectedColor = selectedColor;

        for (int i = 0; i < colorIntVals.length; i++) {
            if (selectedColor == colorIntVals[i]) {
                selectedColorName = colorNames[i];
            }
        }
    }

    public void setColorByName(String name) {

        colorNames = context.getResources().getStringArray(R.array.colorMqtt);
        colorIntVals = context.getResources().getIntArray(R.array.colorIntVals);

        for (int i = 0; i < colorNames.length; i++) {
            if (colorNames[i].equalsIgnoreCase(name)) {
                setSelectedColor(colorIntVals[i]);
            }
        }
    }

    private void setSelectedColor(MainActivity mainActivity, int selectedColor) {

        colorNames = context.getResources().getStringArray(R.array.colorMqtt);
        colorIntVals = context.getResources().getIntArray(R.array.colorIntVals);

        this.selectedColor = selectedColor;

        for (int i = 0; i < colorIntVals.length; i++) {
            if (selectedColor == colorIntVals[i]) {
                selectedColorName = colorNames[i];
                mqttBrokerNotifier(mainActivity, selectedColorName);
            }
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Zeigt den Dialog speziell für RGB Lampen mit Farb und Dim Auswahl
     *
     * @param mainActivity
     * @param dataChangedListener
     * @param adapterPosition
     */
    public void showDialogForThisDevice(final MainActivity mainActivity, OnDataChangedListener dataChangedListener, final int adapterPosition) {
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
                                                           deviceActivator();

                                                           for (int colorIntVal : colorIntVals) {
                                                               if (color == colorIntVal) {
                                                                   setSelectedColor(mainActivity, colorIntVal);
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

                                               }

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {

                                                   setDim(seekBar.getProgress());
                                                   deviceActivator();

                                                   String dim = "" + seekBar.getProgress();
                                                   mqttBrokerNotifier(mainActivity, dim);

                                                   listener.onDataHasChanged(adapterPosition);
                                               }
                                           }

        );

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mainActivity);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(getName() + "     ID: ");
        dimSeek.setProgress(this.getDim());
        dialogBuilder.setPositiveButton(context.getString(R.string.done), new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }

        );

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Gerät aktivieren, wenn es aus war und eine Änderung z.B. Farbe getätigt wurde
     */
    private void deviceActivator() {
        if (!isOn()) {
            setOn(true);
        }
    }

    private void mqttBrokerNotifier(MainActivity mainActivity, String message) {

        String topic = getRoom().getTopic() + "/" + this.getTopic();
        mainActivity.getMqttHandler().mqttPublish(topic, message);
    }

}
