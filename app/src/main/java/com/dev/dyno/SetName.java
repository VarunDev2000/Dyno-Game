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
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SetName extends AppCompatActivity {

    public  static  final  String PREFS_NAME = "LocalStorage";

    private BottomNavigationView bottomNavigationView;

    private MediaPlayer ClickSound;

    private TextInputLayout name_layout;
    TextInputEditText name_text;
    private Button okay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        name_layout = findViewById(R.id.name_layout);
        name_text = findViewById(R.id.name_text);
        okay = findViewById(R.id.okay_btn);

        if(getDynoName() != ""){
            name_layout.setHintAnimationEnabled(false);
            name_text.setText(getDynoName());
        }

        //For smooth transition between activities
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //BottomNav Bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ClickSound = MediaPlayer.create(SetName.this,R.raw.click_sound);
        ClickSound.setVolume((float)0.6,(float)0.6);

        bottomNavigationView.setSelectedItemId(R.id.set_name);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.set_name:
                        return true;

                    case R.id.game:
                        ClickSound.start();
                        startActivity(new Intent(getApplicationContext(),GamePage.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        return true;

                    case R.id.settings:
                        ClickSound.start();
                        startActivity(new Intent(getApplicationContext(),SettingsPage.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        return true;
                }

                return false;
            }
        });


        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name_layout.getEditText().getText().toString().isEmpty()){
                    String dyno_name = "" + name_layout.getEditText().getText().toString();
                    //Log.d("TAG", "onClick: Name -> " + dyno_name);

                    saveDynoName(dyno_name);

                    Intent intent = new Intent(getApplicationContext(),GamePage.class);
                    startActivity(intent);
                }

                else{
                    name_layout.setError("Name cannot be empty!");
                }
            }
        });

    }


    private void saveDynoName(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("dyno_name",name);
        editor.apply();
    }

    private String getDynoName(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        String name = sharedPreferences.getString("dyno_name","");

        return name;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.set_name);
    }

}
