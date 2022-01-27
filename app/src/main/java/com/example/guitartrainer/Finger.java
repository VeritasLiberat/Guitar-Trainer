package com.example.guitartrainer;

import com.google.gson.annotations.Expose;

public enum Finger {
    @Expose
    T, I, M, R, P;

    public static Finger getFinger(String finger) {
        switch (finger) {
            case "T": return Finger.T;
            case "I": return Finger.I;
            case "M": return Finger.M;
            case "R": return Finger.R;
            case "P": return Finger.P;
            default: return null;
        }
    }
}
