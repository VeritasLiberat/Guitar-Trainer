package com.example.guitartrainer;

import com.google.gson.annotations.Expose;

public enum GuitarString {
    @Expose
    El, A, D, G, B ,Eh;

    public static GuitarString getGuitarString(String stringName) {
        switch (stringName) {
            case "El": return GuitarString.El;
            case "A": return GuitarString.A;
            case "D": return GuitarString.D;
            case "G": return GuitarString.G;
            case "B": return GuitarString.B;
            case "Eh": return GuitarString.Eh;
            default: return null;
        }
    }
}
