package com.example.guitartrainer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChordDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_detail);

        displayChord();
    }

    protected void displayChord() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Chord chord = gson.fromJson(getIntent().getStringExtra(Chord.CHORD_EXTRA_KEY), Chord.class);

        TextView chordNameView = findViewById(R.id.chordName);
        chordNameView.setText(chord.name);

        TextView positionsView = findViewById(R.id.positions);

        StringBuilder sb = new StringBuilder();
        for (Position position : chord.positions) {
            sb.append(position.fret).append(" ").append(position.finger).append(" ").append(position.string);
            sb.append("|");
        }
        positionsView.setText(sb.toString());

        TextView barView = findViewById(R.id.bar);
        barView.setText(Integer.toString(chord.bar));

    }
}
