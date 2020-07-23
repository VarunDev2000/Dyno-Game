package com.dev.dyno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Congratulations_screen extends AppCompatActivity {

    public  static  final  String PREFS_NAME = "LocalStorage";

    private TextView stage;
    private ImageButton close;
    private int current_stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //For smooth transition between activities
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_congratulations_screen);


        stage = findViewById(R.id.stage);
        close = findViewById(R.id.close);

        current_stage = getCurrent_stage();
        stage.setText("You have reached STAGE " + current_stage);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),GamePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private int getCurrent_stage(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        int stage = sharedPreferences.getInt("current_stage",1);

        return stage;
    }
}
