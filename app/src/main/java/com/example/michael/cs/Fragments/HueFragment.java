package com.example.michael.cs.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.michael.cs.AsyncTasks.HueExampleAsyncTask;
import com.example.michael.cs.AsyncTasks.HueExampleAsyncTaskListener;
import com.example.michael.cs.R;
import com.flask.colorpicker.ColorPickerView;

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

    private String intToHex(int color) {
        return "0x" + Integer.toHexString(color);

    }

    /**
     * Zeigt den Dialog um das Hue Licht zu steuern
     * Erst wird das Dialog Layout geladen und dann der Wert "an" oder "aus" auf den swtich gesetzt
     * Bei Klick auf den Dialog Knopf: Den int Wert des ColorPickers in Hex umwandeln.
     */
    private void showHueDialog() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.light_dialog, null);

        final ColorPickerView colorPickerView = (ColorPickerView) dialogView.findViewById(R.id.color_picker_view);
        final CheckBox hueCheckboxx = (CheckBox) dialogView.findViewById(R.id.hue_check);

        try {
            colorPickerView.setSelectedColor(hueCurrentColor);
        } catch (Exception e) {

        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(dialogView);

        hueCheckboxx.setChecked(isHueOn);

        dialogBuilder.setNegativeButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int selectedColor = colorPickerView.getSelectedColor();
                Log.i(TAG, "onClick: " + selectedColor);

                Toast.makeText(getContext(), intToHex(selectedColor), Toast.LENGTH_SHORT).show();

                hueCurrentColor = selectedColor;
                isHueOn = hueCheckboxx.isChecked();

                startHueAsyncTask();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void startHueAsyncTask() {

        new HueExampleAsyncTask(this, getContext()).execute("http://www.google.de");

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
        Toast.makeText(getContext(), "finished", Toast.LENGTH_SHORT).show();
    }
}
