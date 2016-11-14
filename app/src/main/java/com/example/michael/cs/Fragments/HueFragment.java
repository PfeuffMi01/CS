package com.example.michael.cs.Fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.michael.cs.AsyncTasks.HueExampleAsyncTask;
import com.example.michael.cs.AsyncTasks.HueExampleAsyncTaskListener;
import com.example.michael.cs.CSHelper;
import com.example.michael.cs.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;

public class HueFragment extends Fragment implements View.OnClickListener, HueExampleAsyncTaskListener {

    private static final String TAG = "HueFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView hueCard;
    private int hueCurrentColor;
    private boolean isHueOn;
    private View view;
    private int hueDim;

    public HueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static HueFragment newInstance(String param1, String param2) {
        HueFragment fragment = new HueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hue, container, false);


        initHueExample();


        return view;
    }

    /**
     * Initialisiert alles, was mit dem Hue-Beispiel zu tun hat
     */
    private void initHueExample() {
        hueCard = (CardView) view.findViewById(R.id.hue_card);
        hueCard.setOnClickListener(this);

        hueCurrentColor = -1;
        isHueOn = false;
    }

    public void setHueCurrentColor(String colorHex) {
        try {
            this.hueCurrentColor = Color.parseColor(colorHex);
        } catch (Exception e) {
            Log.e(TAG, "setHueCurrentColor: color parsing error");
        }
    }

    /**
     * Zeigt den Dialog um das Hue Licht zu steuern
     * Erst wird das Dialog Layout geladen und dann der Wert "an" oder "aus" auf den swtich gesetzt
     * Bei Klick auf den Dialog Knopf: Den int Wert des ColorPickers in Hex umwandeln.
     */
    private void showHueDialog() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rbg_lamp, null);

        final ColorPickerView colorPickerView = (ColorPickerView) dialogView.findViewById(R.id.color_picker);
//        final SwitchCompat dialogSwitch = (SwitchCompat) dialogView.findViewById(R.id.rgb_lamp_dialog_switch);
        final SeekBar dimSeek = (SeekBar) dialogView.findViewById(R.id.seek_dim);

//        dialogSwitch.setText("Philips HUE       ");

        colorPickerView.addOnColorSelectedListener
                (new OnColorSelectedListener() {
                     @Override
                     public void onColorSelected(int i) {
                         Log.i(TAG, "onColorSelected: " + i);
                         hueCurrentColor = i;
//                         setHueCurrentColor(CSHelper.intToHex(i));
                         Log.i(TAG, "onColorSelected: " + i);
                     }
                 }
                );


//        dialogSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Log.i(TAG, "onCheckedChanged: " + b);
//                isHueOn = b;
//            }
//        });

        dimSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i(TAG, "onProgressChanged: " + i + " " + b);
                hueDim = i;
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


        try {
            colorPickerView.setColor(hueCurrentColor, false);
            Log.i(TAG, "showHueDialog: " + hueCurrentColor);
        } catch (Exception e) {
            Log.e(TAG, "showHueDialog: error setting color for hue dialog " + hueCurrentColor);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Philips HUE");

//        dialogSwitch.setChecked(isHueOn);
        dimSeek.setProgress(hueDim);

        dialogBuilder.setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "onClick: " + hueCurrentColor + "\n" + hueDim + "\n" + isHueOn);
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void startHueAsyncTask() {

        String urlONOFF = "http://10.8.6.79:8083/fhem?cmd=set bridge1_HUEGroup0 " + (isHueOn ? "on" : "off");
        String urlRGB = "http://10.8.6.79:8083/fhem?cmd=set bridge1_HUEGroup0 rgb " + CSHelper.intToHex(hueCurrentColor);

        new HueExampleAsyncTask(this, getContext()).execute(urlONOFF);
        new HueExampleAsyncTask(this, getContext()).execute(urlRGB);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.hue_card:
                showHueDialog();
                break;
        }

    }

    @Override
    public void finished(boolean b) {

        if (isAdded()) {
            Toast.makeText(getContext(), "finished", Toast.LENGTH_SHORT).show();
        }
    }
}
