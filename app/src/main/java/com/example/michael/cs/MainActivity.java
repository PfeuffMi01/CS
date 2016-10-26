package com.example.michael.cs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private CardView hueCard;
    private int hueCurrentColor;
    private boolean isHueOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBottomNavigation();
        initHueExample();
    }

    /**
     * Initialisiert alles, was mit dem Hue-Beispiel zu tun hat
     */
    private void initHueExample() {
        hueCard = (CardView) findViewById(R.id.hue_card);
        hueCard.setOnClickListener(this);

        hueCurrentColor = -1;
        isHueOn = false;
    }

    /**
     * Initialisiert die drei Tabs am unteren Bildschirmrand um zwischen "Alle" "Räume" und "Gruppen" zu wechseln
     */
    private void initBottomNavigation() {
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_all:
                                Toast.makeText(MainActivity.this, "Alle", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_rooms:
                                Toast.makeText(MainActivity.this, "Räume", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_groups:
                                Toast.makeText(MainActivity.this, "Gruppen", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.hue_card:
                showHueDialog();
                break;
        }

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

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.light_dialog, null);

        final ColorPickerView colorPickerView = (ColorPickerView) dialogView.findViewById(R.id.color_picker_view);
        final SwitchCompat hueSwitch = (SwitchCompat) dialogView.findViewById(R.id.hue_switch);

        try {
            colorPickerView.setSelectedColor(hueCurrentColor);
        } catch (Exception e) {

        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Philips Hue steuern");

        hueSwitch.setChecked(isHueOn);

        dialogBuilder.setNegativeButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int selectedColor = colorPickerView.getSelectedColor();
                Log.i(TAG, "onClick: " + selectedColor);

                Toast.makeText(MainActivity.this, intToHex(selectedColor), Toast.LENGTH_SHORT).show();

                hueCurrentColor = selectedColor;
                isHueOn = hueSwitch.isChecked();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
