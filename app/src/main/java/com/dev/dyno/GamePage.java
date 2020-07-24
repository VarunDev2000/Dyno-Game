package com.dev.dyno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.dyno.animation.MyBounceInterpolator;

import java.util.concurrent.TimeUnit;

public class GamePage extends AppCompatActivity {

    public  static  final  String PREFS_NAME = "LocalStorage";

    private MediaPlayer BgSong;
    private MediaPlayer AppleBite;

    private TextView stage;
    private TextView apples_eaten;
    private TextView instruction1;
    private TextView instruction2;
    private ImageButton feed_button;
    private Button play_again_button;
    private ImageView dyno_image;

    private RelativeLayout main_layout;
    private LinearLayout loading_screen;

    private int current_stage;
    private int current_apples_eaten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //For smooth transition between activities
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_game_page);


        stage = findViewById(R.id.stage);
        apples_eaten = findViewById(R.id.apples_eaten);
        instruction1 = findViewById(R.id.instruction1);
        instruction2 = findViewById(R.id.instruction2);
        feed_button = findViewById(R.id.feed);
        play_again_button = findViewById(R.id.play_again);
        dyno_image = findViewById(R.id.dyno_image);

        main_layout = findViewById(R.id.main_layout);
        loading_screen = findViewById(R.id.loading_screen);

        current_stage = getCurrent_stage();
        current_apples_eaten = getCurrent_apples_eaten();



        //Initialization
        stage.setText("STAGE " + current_stage);
        apples_eaten.setText(""+current_apples_eaten);
        Resources res = getResources();
        String mDrawableName = "dyno_stage" + current_stage;
        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID );
        dyno_image.setImageDrawable(drawable);
        if(current_apples_eaten >= 20){
            stage.setText("STAGE 5");

            instruction1.setText("You won!");
            instruction2.setText("Congratulations! Dyno has grown big and strong with your help.");

            dyno_image.setImageResource(R.drawable.dyno_stage5);

            feed_button.clearAnimation();
            feed_button.setVisibility(View.GONE);
            play_again_button.setVisibility(View.VISIBLE);
        }




        //OnClicking Feed Button
        feed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Button Effects
                final Animation feedAnim = AnimationUtils.loadAnimation(GamePage.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                feedAnim.setInterpolator(interpolator);
                feed_button.startAnimation(feedAnim);

                //Bite Sound
                AppleBite = MediaPlayer.create(GamePage.this,R.raw.apple_bite);
                AppleBite.start();

                current_apples_eaten = current_apples_eaten + 1;
                saveCurrent_apples_eaten(current_apples_eaten);

                apples_eaten.setText(""+current_apples_eaten);

                float x = dyno_image.getScaleX();
                float y = dyno_image.getScaleY();

                dyno_image.setScaleX((float) (x + 0.01));
                dyno_image.setScaleY((float) (y + 0.01));


                if(current_apples_eaten >= 20){

                    stage.setText("STAGE 5");
                    instruction1.setText("You won!");
                    instruction2.setText("Congratulations! Dyno has grown big and strong with your help.");
                    dyno_image.setScaleX((float) (x - 0.2));
                    dyno_image.setScaleY((float) (y - 0.2));
                    dyno_image.setImageResource(R.drawable.dyno_stage5);
                    feed_button.clearAnimation();
                    feed_button.setVisibility(View.GONE);
                    play_again_button.setVisibility(View.VISIBLE);

                }

                else if(current_apples_eaten % 5 == 0){

                    current_stage = current_stage + 1;
                    saveCurrent_stage(current_stage);

                    Intent intent = new Intent(getApplicationContext(),Congratulations_screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    /*
                    stage.setText("STAGE " + current_stage);

                    Resources res = getResources();
                    String mDrawableName = "dyno_stage" + current_stage;
                    int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
                    Drawable drawable = res.getDrawable(resID );
                    dyno_image.setImageDrawable(drawable);
                     */

                }

            }
        });


        //OnClicking Play Again Button
        play_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main_layout.setVisibility(View.GONE);

                saveCurrent_stage(1);
                saveCurrent_apples_eaten(0);

                if(BgSong != null) {
                    BgSong.stop();
                    BgSong.release();
                    BgSong = null;
                }

                loading_screen.setVisibility(View.VISIBLE);


                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(),GamePage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        },
                        2000
                );
            }
        });

    }


    private void saveCurrent_stage(int current_stage){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("current_stage",current_stage);
        editor.apply();
    }

    private void saveCurrent_apples_eaten(int current_apples_eaten ){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("current_apples_eaten",current_apples_eaten);
        editor.apply();
    }


    private int getCurrent_stage(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        int stage = sharedPreferences.getInt("current_stage",1);

        return stage;
    }

    private int getCurrent_apples_eaten(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        int apples_eaten = sharedPreferences.getInt("current_apples_eaten",0);

        return apples_eaten;
    }


    @Override
    protected void onResume() {
        super.onResume();
        BgSong = MediaPlayer.create(GamePage.this,R.raw.bg_music);
        BgSong.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(BgSong != null) {
            BgSong.stop();
            BgSong.release();
            BgSong = null;
        }
    }

}
