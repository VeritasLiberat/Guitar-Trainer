package com.example.guitartrainer;

import com.google.gson.annotations.Expose;

public class Position {
    @Expose
    GuitarString string;
    @Expose
    int fret;
    @Expose
    Finger finger;

    Position(GuitarString string, int fret, Finger finger) {
        this.string = string;
        this.fret = fret;
        this.finger = finger;
    }
}
