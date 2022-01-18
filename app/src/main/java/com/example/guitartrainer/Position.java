package com.example.guitartrainer;

public class Position {
    GuitarString string;
    int fret;
    Finger finger;

    Position(GuitarString string, int fret, Finger finger) {
        this.string = string;
        this.fret = fret;
        this.finger = finger;
    }
}
