package com.example.michael.cs.Data.Devices;

import android.content.Context;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.R;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class Temp extends Device {

    public static final int imgRgbLamp = R.drawable.temp;
    public int temp;

    public Temp(Context context, String _id, boolean isOn, String name, Room room, Group group, int temp, String topic) {
        super(context,_id, isOn, name, room, group, topic);

        this.temp = temp;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

 /*   public void showDialogForThisDevice(final MainActivity mainActivity) {

        Calendar cal = Calendar.getInstance();
        final long systemTimeDialogStart = cal.getTimeInMillis();

        LayoutInflater inflater = mainActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_temp, null);

        final SeekBar tempSeek = (SeekBar) dialogView.findViewById(R.id.seek_temp);
        final TextView tvTempVal = (TextView) dialogView.findViewById(R.id.tv_temp_val);

        tempSeek.setMax(MAX_TEMP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempSeek.setProgress(getTemp(), true);
        } else {
            tempSeek.setProgress(getTemp());
        }
        tvTempVal.setText((getTemp()) + "°C");


        tempSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                Calendar cal = Calendar.getInstance();
                if (cal.getTimeInMillis() - systemTimeDialogStart >= DIALOG_LISTENER_DELAY) {

                    setTemp(i);
                    tvTempVal.setText(getTemp() + "°C");
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


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mainActivity);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(getName() + "     ID: " + get_id());
        tempSeek.setProgress((int) this.getTemp());

        dialogBuilder.setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }*/

    private void deviceActivator() {
        if (!isOn()) {
            setOn(true);
        }
    }
}
