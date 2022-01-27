package com.example.guitartrainer;

import com.google.gson.annotations.Expose;

public class Chord {
    public static final String CHORD_EXTRA_KEY = "chord";

    @Expose
    String name;
    @Expose
    Position[] positions;
//    Bar bar;
    @Expose
    int bar;

    Chord(String name, Position[] positions, int bar) {
        this.name = name;
        this.positions = positions;
        this.bar = bar;
    }

//    class Bar {
//        GuitarString string;
//        int fret;
//
//        Bar(GuitarString string, int fret) {
//            this.string = string;
//            this.fret = fret;
//        }
//    }
}
