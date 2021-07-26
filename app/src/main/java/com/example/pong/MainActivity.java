package com.example.pong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pong.views.view1;

public class MainActivity extends AppCompatActivity {

    static boolean played_once = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ClickableViewAccessibility", "ApplySharedPref"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();
        final int[] high_score = {prefs.getInt("key", 0)};

        MediaPlayer song2 = MediaPlayer.create(getApplicationContext(),R.raw.sound2);

        view1.song = MediaPlayer.create(getApplicationContext(),R.raw.sound);

        TextView score_view = findViewById(R.id.textView2);
        score_view.setText(String.valueOf(view1.score));

        TextView highScore_view = findViewById(R.id.textView4);
        highScore_view.setText(String.valueOf(high_score[0]));

        TextView gameOver_view = findViewById(R.id.textView5);

        Button exit_btn = findViewById(R.id.button2);

        Button newGame_btn = findViewById(R.id.button);

        newGame_btn.setVisibility(View.INVISIBLE);
        exit_btn.setVisibility(View.INVISIBLE);
        gameOver_view.setVisibility(View.INVISIBLE);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(() -> {
                            score_view.setText(String.valueOf(view1.score));
                            if (high_score[0] <= view1.score) {
                                high_score[0] = view1.score;
                                highScore_view.setText(String.valueOf(high_score[0]));
                            }
                            if (view1.game_ovr) {
                                editor.putInt("key", high_score[0]);
                                editor.commit();
                                highScore_view.setText(String.valueOf(high_score[0]));
                                exit_btn.setVisibility(View.VISIBLE);
                                newGame_btn.setVisibility(View.VISIBLE);
                                gameOver_view.setVisibility(View.VISIBLE);
                                if(!played_once){
                                    song2.start();
                                    played_once =true;
                                }
                                exit_btn.setOnClickListener(v -> finishAndRemoveTask());
                                newGame_btn.setOnClickListener(v -> {
                                    view1.game_ovr = false;
                                    played_once = false;
                                    view1.score = 0;
                                    view1.ball.reset();
                                    Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                                            getBaseContext().getPackageName() );
                                    intent .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                });
                            }
                        });
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        thread.start();
    }
}