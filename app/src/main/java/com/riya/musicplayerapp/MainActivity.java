package com.riya.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button forward_btn, back_btn, play_btn, stop_btn;
    TextView time_txt,title_txt;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;

    Handler handler = new Handler();

    double startTime =0;
    double finalTime =0;
    int forword = 10000;
    int backword = 10000;
    static  int oneTimeonly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        play_btn = findViewById(R.id.play_btn);
        stop_btn = findViewById(R.id.pause_btn);
        forward_btn = findViewById(R.id.forword_btn);
        back_btn = findViewById(R.id.back_btn);
        time_txt = findViewById(R.id.timelefttext);
        title_txt = findViewById(R.id.songtitle);

        seekBar = findViewById(R.id.seekBar);

        mediaPlayer = MediaPlayer.create(this,R.raw.dogallansandhu);

        title_txt.setText(getResources().getIdentifier(
                "dogallansandhu",
                "raw",
                getPackageName()
        ));


        seekBar.setClickable(false);



        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();

            }
        });

        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;

                if((temp + forword) <= finalTime){
                    startTime = startTime +forword;
                    mediaPlayer.seekTo((int) startTime);

                }else{
                    Toast.makeText(MainActivity.this, "Can`t Jump forword", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if((temp - backword)>0){
                    startTime = startTime - backword;
                    mediaPlayer.seekTo((int) startTime );
                }else{
                    Toast.makeText(MainActivity.this, "Can`t move backword", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
            private  void PlayMusic(){
              mediaPlayer.start();
              finalTime = mediaPlayer.getDuration();
              startTime = mediaPlayer.getCurrentPosition();

              if( oneTimeonly ==0){
                  seekBar.setMax((int) finalTime);
                  oneTimeonly =1;
              }

              time_txt.setText(String.format(
                      "%d min, %d sed",
                      TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                      TimeUnit.MICROSECONDS.toSeconds((long) finalTime)-
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))
              ));

              seekBar.setProgress((int) startTime);
              handler.postDelayed(UpdateSongTime,100);

            }

            private  Runnable UpdateSongTime = new Runnable() {
                @Override
                public void run() {
                    startTime = mediaPlayer.getCurrentPosition();
                    time_txt.setText(String.format("%d min,%d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                   TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
                    ));

                    seekBar.setProgress((int) startTime);
                    handler.postDelayed(UpdateSongTime,100);

                }
            };
}