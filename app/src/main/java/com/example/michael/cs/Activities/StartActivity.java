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
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
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
public class StartActivity extends AppCompatActivity implements OnConnectionListener, OnConnectionLostListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
    private SwitchCompat forceLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i(TAG, "onCreate: Lifecycle");

        super.onCreate(savedInstanceState);
        profileHandler = ProfileHandler.getInstance(this);
        mqttHandler = MQTTHandler.getInstance(this);

        initUI();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupProfiles();
    }

    private void initUI() {
        setContentView(R.layout.activity_start);

        forceLogin = (SwitchCompat) findViewById(R.id.switch_force_login);
        imageHSC = (ImageView) findViewById(R.id.hsc_img);
        imageNoConn = (ImageView) findViewById(R.id.no_connection_img);
        profileSpinner = (Spinner) findViewById(R.id.profiles_spinner);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        connectBtn = (AppCompatButton) findViewById(R.id.connect_btn);
        newProfileBtn = (AppCompatButton) findViewById(R.id.new_profile_btn);

        imageHSC.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                removeAllProfiles();
                return false;
            }
        });

        connectBtn.setOnClickListener(this);
forceLogin.setOnCheckedChangeListener(this);
        newProfileBtn.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View view) {
                                                 showNewProfileDialog();
                                             }
                                         }

        );


    }

    private void removeAllProfiles() {
        int amountProfiles = sharedPreferences.getInt("pref_amount_profiles", 0);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < amountProfiles; i++) {
            editor.remove("pref_profiles_" + (i + 1));
        }

        editor.remove("pref_amount_profiles");
        editor.apply();

        profileHandler.initSavedProfiles();
        setupProfiles();
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
        final TextInputEditText user = (TextInputEditText) dialogView.findViewById(R.id.profile_user_et);
        final TextInputEditText pwd = (TextInputEditText) dialogView.findViewById(R.id.profile_pwd_et);
        final TextInputEditText topicPre = (TextInputEditText) dialogView.findViewById(R.id.profile_topic_prefix_et);

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
                mqttHandler.disconnect(TAG + " showNewProfileDialog");
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
                String username = String.valueOf(user.getText());
                String pass = String.valueOf(pwd.getText());
                String tPre = String.valueOf(topicPre.getText());


                if (!n.equals("") && !ip_.equals("") && !p.equals("")) {

                    Profile tmpProfile = new Profile(n, ip_, p, tPre, username, pass);
                    profileHandler.addProfile(tmpProfile);
                    thingsToDoBeforeOnDestroy();

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
        Log.i(TAG, "onStart: Lifecycle");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: Lifecycle");
      //  mqttHandler.disconnect(TAG + " onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: Lifecycle");
        thingsToDoBeforeOnDestroy();
        mqttHandler.disconnect(TAG + " onDestroy");
        super.onDestroy();
    }


    private void thingsToDoBeforeOnDestroy() {

        Log.i(TAG, "thingsToDoBeforeOnDestroy: ");

        String[] profiles = profileHandler.getStringToSaveToPrefs();
        int amountProfiles = profiles.length;

        Log.i(TAG, "thingsToDoBeforeOnDestroy: " + amountProfiles);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("pref_amount_profiles", amountProfiles);

        for (int i = 0; i < amountProfiles; i++) {
            editor.putString("pref_profiles_" + (i + 1), profiles[i]);
            Log.i(TAG, "thingsToDoBeforeOnDestroy: " + profiles[i]);
        }

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
            connectBtn.setVisibility(View.VISIBLE);

        } else {

            String[] spinnerArray = new String[]{"Noch keine Profile vorhanden"};
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    spinnerArray);
            profileSpinner.setAdapter(spinnerArrayAdapter);
            profileSpinner.setEnabled(false);
            connectBtn.setEnabled(false);
            connectBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public void onMQTTConnection(boolean isConnectionSuccessful, boolean forcedAppEntering, String connectionIP) {

        Log.i(TAG, "onMQTTConnection: ");
        
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.connect_btn:
                initConnection();
                break;

            default:
                break;
        }
    }

    private void initConnection() {

        if (sharedPreferences.getBoolean("pref_force_login", false)) {
            onMQTTConnection(true, true, "");
        } else {
            
            // Nur wenn eine Internetverbindung besteht
            if (InternetConnectionHandler.isInternetAvailable(StartActivity.this)) {

                loadingViewHandler(true, false);

               /* if (mqttHandler.isConnected()) {
                    mqttHandler.disconnect();
                }*/

                Profile profileToConnectTo = profileHandler.getProfileList().get(profileSpinner.getSelectedItemPosition());
                String pwd = profileToConnectTo.getPassword();

                mqttHandler.setConnetionDetails(
                        profileToConnectTo.getServerIP(),
                        profileToConnectTo.getServerPort(),
                        "",
                        profileToConnectTo.getUsername(),
                        pwd.toCharArray());

                mqttHandler.setOnConnectionListener(StartActivity.this);
                mqttHandler.connect();

                Toast.makeText(StartActivity.this, "Verbindung zu "
                        + profileToConnectTo.getName()
                        + " "
                        + profileToConnectTo.getServerIP(), Toast.LENGTH_SHORT).show();
            } else {
                
                //TODO Trotzdem in die App gehen, aber mit Hinweis dass man offline ist.
                loadingViewHandler(false, true);
                InternetConnectionHandler.noInternetAvailable(StartActivity.this);
            }

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("pref_force_login", b);
        editor.apply();

    }
}
