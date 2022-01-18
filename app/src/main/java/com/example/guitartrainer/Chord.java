package com.example.guitartrainer;

public class Chord {
    String name;
    Position[] positions;
    Bar bar;

    Chord(String name, Position[] positions, Bar bar) {
        this.name = name;
        this.positions = positions;
        this.bar = bar;
    }

    class Bar {
        GuitarString string;
        int fret;

        Bar(GuitarString string, int fret) {
            this.string = string;
            this.fret = fret;
        }
    }
}
