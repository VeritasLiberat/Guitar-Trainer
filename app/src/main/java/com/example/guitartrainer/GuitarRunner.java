package com.example.guitartrainer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Message;

import java.util.Random;

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

    public static final int beatsPerMinute = 60;  // beats per minute
    public static final int beatsPerMeasure = 4;

    public static final int millisecondsInAMinute = 60000;

    public static final int beatLengthMilli = millisecondsInAMinute / beatsPerMinute;
    public static final int measureLengthMilli = beatLengthMilli * beatsPerMeasure;

    public static int currentBeat = 1;

    static final float loudVolume = 1.0f;
    static final float normalVolume = 0.4f;


    ChordLegacy currentChord;
    ChordLegacy nextChord;

    Context context;
    public MediaPlayer mediaPlayer;

    GuitarRunner(Context context) {
        currentChord = ChordLegacy.getRandomChord();
        nextChord = ChordLegacy.getRandomChord();
        this.context = context;
        mediaPlayer = MediaPlayer.create(context, R.raw.metronome);
    }

    @Override
    public void run() {
        System.out.println("Current Beat" + currentBeat);
        playSound(currentBeat == 1);
        flashMetronome();

        if (currentBeat == 1) {
            changeChord();
        } else if (currentBeat == beatsPerMeasure) {
            currentBeat = 0;
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

    void changeChord() {
        Message message = MainActivity.chordHandler.obtainMessage();
        message.obj = new String[]{currentChord.toString(), nextChord.toString()};
        MainActivity.chordHandler.sendMessage(message);

        currentChord = nextChord;
        nextChord = ChordLegacy.getRandomChord();
    }
}
