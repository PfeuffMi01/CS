package com.example.michael.cs.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.michael.cs.Interfaces.OnConnectionListener;
import com.example.michael.cs.Interfaces.OnConnectionLostListener;
import com.example.michael.cs.InternetConnectionHandler;
import com.example.michael.cs.MQTTHandler;
import com.example.michael.cs.Profile;
import com.example.michael.cs.ProfileHandler;
import com.example.michael.cs.R;

/**
 * Created by Patrick PC on 22.12.2016.
 */
public class StartActivity extends AppCompatActivity implements OnConnectionListener, OnConnectionLostListener {

    private static final String TAG = "StartActivity";
    private SharedPreferences sharedPreferences;
    private ProfileHandler profileHandler;
    private MQTTHandler mqttHandler;
    private Spinner profileSpinner;
    private AppCompatButton newProfileBtn;
    private AppCompatButton connectBtn;
    private ImageView imageHSC;
    private ImageView imageNoConn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileHandler = ProfileHandler.getInstance(this);
        mqttHandler = MQTTHandler.getInstance(this);

        initUI();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupProfiles();
    }


    private void initUI() {
        setContentView(R.layout.activity_start);

        imageHSC = (ImageView) findViewById(R.id.hsc_img);
        imageNoConn = (ImageView) findViewById(R.id.no_connection_img);
        profileSpinner = (Spinner) findViewById(R.id.profiles_spinner);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        connectBtn = (AppCompatButton) findViewById(R.id.connect_btn);
        newProfileBtn = (AppCompatButton) findViewById(R.id.new_profile_btn);

        imageHSC.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("pref_profiles");
                editor.apply();

                profileHandler.initSavedProfiles();
                setupProfiles();
                return false;
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {

                                              if (InternetConnectionHandler.isInternetAvailable(StartActivity.this)) {

                                                  loadingViewHandler(true, false);

                                                  if (mqttHandler.isConnected()) {
                                                      mqttHandler.disconnect();
                                                  }

                                                  Profile profileToConnectTo = profileHandler.getProfileList().get(profileSpinner.getSelectedItemPosition());

                                                  mqttHandler.setConnetionDetails(profileToConnectTo.getServerIP(), profileToConnectTo.getServerPort(), "");
                                                  mqttHandler.setOnConnectionListener(StartActivity.this);
                                                  mqttHandler.connect();

                                                  Toast.makeText(StartActivity.this, "Verbindung zu "
                                                          + profileToConnectTo.getName()
                                                          + " "
                                                          + profileToConnectTo.getServerIP(), Toast.LENGTH_SHORT).show();
                                              }
                                              else {
                                                  loadingViewHandler(false, true);
                                                  InternetConnectionHandler.noInternetAvailable(StartActivity.this);
                                              }
                                          }

                                      }

        );

        newProfileBtn.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View view) {
                                                 showNewProfileDialog();
                                             }
                                         }

        );


    }

    private void loadingViewHandler(boolean isProgressBarVisisble, boolean isNoConnImgVisible) {

        progressBar.setVisibility(isProgressBarVisisble ? View.VISIBLE : View.INVISIBLE);
        imageNoConn.setVisibility(isNoConnImgVisible ? View.VISIBLE : View.INVISIBLE);

    }

    private void showNewProfileDialog() {

        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_profile, null);

        final TextInputEditText name = (TextInputEditText) dialogView.findViewById(R.id.profilename_et);
        final TextInputEditText ip = (TextInputEditText) dialogView.findViewById(R.id.profile_ip_et);
        final TextInputEditText port = (TextInputEditText) dialogView.findViewById(R.id.profile_port_et);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Profil hinzufügen");
        dialogBuilder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }
        );


        dialogBuilder.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mqttHandler.disconnect();
            }
        });

      /*  dialogBuilder.setNegativeButton("Test", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });*/

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;

                String n = String.valueOf(name.getText());
                String ip_ = String.valueOf(ip.getText());
                String p = String.valueOf(port.getText());

                if (!n.equals("") && !ip_.equals("") && !p.equals("")) {

                    Profile tmpProfile = new Profile(n, ip_, p);
                    profileHandler.addProfile(tmpProfile);

                    wantToCloseDialog = true;
                    setupProfiles();

                } else {
                    Toast.makeText(StartActivity.this, "Eingaben unvollständig", Toast.LENGTH_LONG).show();
                }

                if (wantToCloseDialog) {
                    alertDialog.dismiss();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thingsToDoBeforeOnDestroy();
    }


    private void thingsToDoBeforeOnDestroy() {

        String saveToPrefs = profileHandler.getStringToSaveToPrefs();

        Log.i(TAG, "onDestroy: " + saveToPrefs);

        if (mqttHandler.isConnected()) {
            Log.i(TAG, "thingsToDoBeforeOnDestroy: onDestroy");
//            mqttHandler.disconnect();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pref_profiles", saveToPrefs);
        editor.apply();
    }

    @Override
    protected void onResume() {
        mqttHandler.setOnConnectionListener(this);
        mqttHandler.setOnConnectionLostListener(this);
        super.onResume();
    }

    private void setupProfiles() {

        if (profileHandler.getProfileList().size() > 0) {

            String[] spinnerArray = new String[profileHandler.getProfileList().size()];

            Log.i(TAG, "setupProfiles: " + profileHandler.getProfileList().size());

            for (int i = 0; i < profileHandler.getProfileList().size(); i++) {
                spinnerArray[i] = profileHandler.getProfileList().get(i).getName();
                Log.i(TAG, "setupProfiles: spinnerArray[i] ");
            }

            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    spinnerArray);
            profileSpinner.setAdapter(spinnerArrayAdapter);
            profileSpinner.setEnabled(true);
            connectBtn.setEnabled(true);
/*            connectBtn.setTextColor(getResources().getColor(R.color.white));
            connectBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));*/
            connectBtn.setVisibility(View.VISIBLE);

        } else {

            String[] spinnerArray = new String[]{"Noch keine Profile vorhanden"};
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    spinnerArray);
            profileSpinner.setAdapter(spinnerArrayAdapter);
            profileSpinner.setEnabled(false);
            connectBtn.setEnabled(false);
/*            connectBtn.setTextColor(getResources().getColor(R.color.text_gray));
            connectBtn.setBackgroundColor(getResources().getColor(R.color.text_light_gray));*/
            connectBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public void onMQTTConnection(boolean isConnectionSuccessful, boolean forcedAppEntering, String connectionIP) {

        if (isConnectionSuccessful) {

            Log.i(TAG, "onMQTTConnection: success");            
            profileHandler.setCurrentProfile(profileHandler.getProfileList().get(profileSpinner.getSelectedItemPosition()));
            loadingViewHandler(false, false);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);


        } else {
            Log.i(TAG, "onMQTTConnection: error");
            loadingViewHandler(false, false);
        }

    }

    @Override
    public void onMQTTConnectionLost() {
        Toast.makeText(this, TAG + " Connection Lost", Toast.LENGTH_SHORT).show();
    }
}
