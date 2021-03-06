package com.example.guitartrainer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GuitarRunner implements Runnable {
    enum ChordLegacy {
        A,
        Am,
        B,
        Bm,
        C,
        Cm,
        D,
        Dm,
        E,
        Em,
        F,
        Fm,
        G,
        Gm;

        public static ChordLegacy getRandomChord() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    public static int beatsPerMinute = 60;
    public static int beatsPerMeasure = 4;

    public static final int millisecondsInAMinute = 60000;

    public static int beatLengthMilli = millisecondsInAMinute / beatsPerMinute;
    public static int measureLengthMilli = beatLengthMilli * beatsPerMeasure;

    public int currentBeat;

    static final float loudVolume = 1.0f;
    static final float normalVolume = 0.4f;

    ChordLegacy currentChord;
    ChordLegacy nextChord;

    Context context;
    MainActivity mainActivity;
    public MediaPlayer mediaPlayer;

    public static long timeWhenTimerStopped = 0;
    boolean isCountOff = true;

    GuitarRunner(Context context, MainActivity mainActivity) {
        currentBeat = 1;
        nextChord = ChordLegacy.getRandomChord();
        this.context = context;
        this.mainActivity = mainActivity;
        mediaPlayer = MediaPlayer.create(context, R.raw.metronome);
    }

    @Override
    public void run() {
        System.out.println("Current Beat" + currentBeat);
        playSound(currentBeat == 1);
        flashMetronome();

        if (isCountOff) {
            handleCountOff();
        } else {
            if (currentBeat == 1) {
                changeChord();
            } else if (currentBeat >= beatsPerMeasure) {
                currentBeat = 0;
            }
        }

        currentBeat++;
    }

    private void playSound(boolean playLoud) {
        if (playLoud) {
            mediaPlayer.setVolume(loudVolume, loudVolume);
        } else {
            mediaPlayer.setVolume(normalVolume, normalVolume);
        }

        mediaPlayer.start();
    }

    void flashMetronome() {
        Message message = MainActivity.metronomeHandler.obtainMessage();
        MainActivity.metronomeHandler.sendMessage(message);
    }

    void handleCountOff() {
        Message message = MainActivity.chordHandler.obtainMessage();
        message.obj = new String[]{Integer.toString(currentBeat), nextChord.toString()};
        MainActivity.chordHandler.sendMessage(message);
        if (currentBeat >= beatsPerMeasure) {
            currentBeat = 0;
            isCountOff = false;
            currentChord = nextChord;
            nextChord = ChordLegacy.getRandomChord();
        }
    }

    void changeChord() {
        Message message = MainActivity.chordHandler.obtainMessage();
        message.obj = new String[]{currentChord.toString(), nextChord.toString()};
        MainActivity.chordHandler.sendMessage(message);

        currentChord = nextChord;
        nextChord = ChordLegacy.getRandomChord();
    }
}
