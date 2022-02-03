package com.example.guitartrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * - Get a 'how to learn guitar' book
 *
 * - Learn Mode
 *      Learn one scale/chord at a time
 *      Use scale/chord instance to generate view of the frets
 *          scale: string and fret has a dot on it with the number of the note in the scale inlaid
 *              ex: Dot on the second fret of the A string with the number 7,
 *              indicating the seventh note of the scale
 *          chord: string and fret has a dot on it with the suggested finger inlaid
 *              ex: Dot on the 3rd fret of the B string with the letter R,
 *              indicating the suggested Finger is the Ring Finger
 *      Decide whether a scale/chord is included in the training or not
 *
 * - Train Mode
 *      Metronome
 *          Plays sound and flashes the current BPM on every beat
 *          Sound is different on the first beat of the measure as this is when the chord changes
 *          Ability to change tempo
 *
 * - Tuner
 *      Grab tuner I use and branch off of that
 *
 * - Play
 *      Let you search for tabs and play them
 *
 *
 * track total time in app on each activity
 */

public class MainActivity extends AppCompatActivity {
    TextView currentChordView;
    TextView tempoView;
    TextView nextChordView;
    EditText editTempo;
    Chronometer timer;

    static Handler chordHandler;
    static Handler metronomeHandler;

    GuitarRunner guitarRunner;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        buildHandlers();
        buildViews();

        guitarRunner = new GuitarRunner(this, this);
        scheduler.scheduleAtFixedRate(guitarRunner, 0, GuitarRunner.beatLengthMilli, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onStop() {
        super.onStop();
        scheduler.shutdown();

        timer.stop();
        GuitarRunner.timeWhenTimerStopped = SystemClock.elapsedRealtime() - timer.getBase();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(guitarRunner, 0, GuitarRunner.beatLengthMilli, TimeUnit.MILLISECONDS);
        }

        timer.setBase(SystemClock.elapsedRealtime() - GuitarRunner.timeWhenTimerStopped);
        timer.start();
    }

    void buildHandlers() {
        chordHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String[] chords = (String[]) msg.obj;
                currentChordView.setText(chords[0]);
                nextChordView.setText(chords[1]);
            }
        };

        metronomeHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                final Animation animation = new AlphaAnimation(0, 1); // Change alpha from fully visible to invisible
                animation.setDuration(GuitarRunner.beatLengthMilli / 8); // in ms
                animation.setInterpolator(new LinearInterpolator());
                tempoView.startAnimation(animation);
            }
        };
    }

    void buildViews() {
        tempoView = findViewById(R.id.tempo);
        tempoView.setText(GuitarRunner.beatsPerMinute + " BPM");

        editTempo = findViewById(R.id.editTempo);

        currentChordView = findViewById(R.id.currentChord);
        nextChordView = findViewById(R.id.nextChord);

        timer = findViewById(R.id.timer);
    }

    public void processTempoChange(View view) {
        String inputTempoString = editTempo.getText().toString();
        if (inputTempoString.equals("")) {return;}

        int userSetTempo = Integer.parseInt(inputTempoString);
        if (userSetTempo != GuitarRunner.beatsPerMinute) {
            GuitarRunner.beatsPerMinute = userSetTempo;
            GuitarRunner.beatLengthMilli = GuitarRunner.millisecondsInAMinute / GuitarRunner.beatsPerMinute;

            tempoView.setText(GuitarRunner.beatsPerMinute + " BPM");

            editTempo.setText("");
            editTempo.setHint(R.string.edit_tempo_hint);
            editTempo.clearFocus();
            
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);  // hide keyboard

            scheduler.shutdown();
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(guitarRunner, 0, GuitarRunner.beatLengthMilli, TimeUnit.MILLISECONDS);
        }
    }

    public void goToLearnMode(View view) {
        Intent intent = new Intent(this, ChordListActivity.class);
        startActivity(intent);
    }

    public void restartTimer(View view) {
        timer.setBase(SystemClock.elapsedRealtime());
        GuitarRunner.timeWhenTimerStopped = 0;
    }
}