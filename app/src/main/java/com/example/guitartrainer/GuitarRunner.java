package com.example.guitartrainer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Message;
import android.text.Editable;
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

    public static int beatsPerMinute = 60;  // beats per minute
    public static int beatsPerMeasure = 4;

    public static final int millisecondsInAMinute = 60000;

    public static int beatLengthMilli = millisecondsInAMinute / beatsPerMinute;
    public static int measureLengthMilli = beatLengthMilli * beatsPerMeasure;

    public static int currentBeat = 1;

    static final float loudVolume = 1.0f;
    static final float normalVolume = 0.4f;


    ChordLegacy currentChord;
    ChordLegacy nextChord;

    Context context;
    MainActivity mainActivity;
    public MediaPlayer mediaPlayer;
    EditText editTempo;

    GuitarRunner(Context context, MainActivity mainActivity) {
        currentChord = ChordLegacy.getRandomChord();
        nextChord = ChordLegacy.getRandomChord();
        this.context = context;
        this.mainActivity = mainActivity;
        mediaPlayer = MediaPlayer.create(context, R.raw.metronome);
        editTempo = mainActivity.findViewById(R.id.editTempo);
    }

    @Override
    public void run() {
//        System.out.println("Current Beat" + currentBeat);
        playSound(currentBeat == 1);
        flashMetronome();
        checkForTempoChange();

        if (currentBeat == 1) {
            changeChord();
        } else if (currentBeat == beatsPerMeasure) {
            currentBeat = 0;
        }

        currentBeat++;
    }

    private void checkForTempoChange() {
        int userSetTempo;
        try {
            userSetTempo = Integer.getInteger(editTempo.getText().toString());
        } catch (NullPointerException npe) {
            return;
        }
        if (userSetTempo != beatsPerMinute) {
            beatsPerMinute = userSetTempo;
            mainActivity.scheduler.shutdown();
            mainActivity.scheduler.scheduleAtFixedRate(this, 0, GuitarRunner.beatLengthMilli, TimeUnit.MILLISECONDS);
        }
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
