package com.dev.dyno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsPage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private MediaPlayer ClickSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);


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

    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.settings);
    }

}
