package com.example.guitartrainer;

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

    static final int beatsPerMinute = 120;  // beats per minute
    static final int beatsPerMeasure = 4;

    static final int millisecondsInAMinute = 60000;

    static final int beatLengthMilli = millisecondsInAMinute / beatsPerMinute;
    static final int measureLengthMilli = beatLengthMilli * beatsPerMeasure;

    static int currentBeat = 1;

    ChordLegacy currentChord;
    ChordLegacy nextChord;

    GuitarRunner() {
        currentChord = ChordLegacy.getRandomChord();
        nextChord = ChordLegacy.getRandomChord();
    }

    @Override
    public void run() {
        flashMetronome();
        playSound();
        if (currentBeat == 1) {
            changeChord();
        } else if (currentBeat == beatsPerMeasure) {
            currentBeat = 0;
        }
        currentBeat++;
    }

    private void playSound() {
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
