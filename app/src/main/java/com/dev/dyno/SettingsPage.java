package com.dev.dyno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsPage extends AppCompatActivity {

    public  static  final  String PREFS_NAME = "LocalStorage";

    private BottomNavigationView bottomNavigationView;

    private MediaPlayer ClickSound;

    private Switch game_sound;
    private Button clear_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        game_sound = findViewById(R.id.game_sound);
        clear_data = findViewById(R.id.clear_data);

        //Set Sound option
        game_sound.setChecked(getGameSoundOption());

        //For smooth transition between activities
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //BottomNav Bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ClickSound = MediaPlayer.create(SettingsPage.this,R.raw.click_sound);
        ClickSound.setVolume((float)0.6,(float)0.6);

        bottomNavigationView.setSelectedItemId(R.id.settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.set_name:
                        ClickSound.start();
                        startActivity(new Intent(getApplicationContext(),SetName.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        return true;

                    case R.id.game:
                        ClickSound.start();
                        startActivity(new Intent(getApplicationContext(),GamePage.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        return true;

                    case R.id.settings:
                        return true;
                }

                return false;
            }
        });


        game_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    Toast.makeText(SettingsPage.this,"Sound On",Toast.LENGTH_SHORT).show();
                    saveGameSoundOption(true);
                }
                else{
                    Toast.makeText(SettingsPage.this,"Sound off",Toast.LENGTH_SHORT).show();
                    saveGameSoundOption(false);
                }
            }
        });


        clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Delete Local data
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                settings.edit().clear().commit();

                Toast.makeText(SettingsPage.this,"Data cleared",Toast.LENGTH_SHORT).show();

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(),GamePage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        },
                        500
                );
            }
        });
    }

    private void saveGameSoundOption(boolean sound){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("game_sound",sound);
        editor.apply();
    }

    private boolean getGameSoundOption(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        boolean sound = sharedPreferences.getBoolean("game_sound",true);

        return sound;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.settings);
    }

}
