package com.example.guitartrainer;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChordListFragment extends Fragment {
    private static final String TAG = "ChordListFragment";

    protected RecyclerView mRecyclerView;
    protected ChordListAdapter chordListAdapter;
    protected Chord[] chords;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getChords();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chord_list_frag, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        chordListAdapter = new ChordListAdapter(chords);
        mRecyclerView.setAdapter(chordListAdapter);

        return rootView;
    }

    private void getChords() {
        // todo: get all the chords from the chords.xml, build Chord objects, return that array
        Resources res = getResources();
        TypedArray chordsTypedArray = res.obtainTypedArray(R.array.chords);
        String[] chordsStringArray = chordsTypedArray.getResources().getStringArray(R.array.chords);
        chords = new Chord[chordsStringArray.length];

        int j = 0;
        for (String chordString : chordsStringArray) {
            System.out.println("### " + chordString);
            String[] chordValues = chordString.split("\\|");

            String chordName = chordValues[0];

            int bar = Integer.parseInt(chordValues[2]);

            String[] positionStrings = chordValues[1].split(",");
            Position[] positions = new Position[positionStrings.length];
            int i = 0;
            for (String positionString : positionStrings) {
                String[] positionValues = positionString.split(";");

                String guitarStringString = positionValues[0];
                GuitarString guitarString = GuitarString.getGuitarString(guitarStringString);

                int fret = Integer.parseInt(positionValues[1]);

                String fingerString = positionValues[2];
                Finger finger = Finger.getFinger(fingerString);

                Position position = new Position(guitarString, fret, finger);
                positions[i] = position;
                i++;
            }

            Chord chord = new Chord(chordName, positions, bar);
            chords[j] = chord;
            j++;
        }
        System.out.println("");
    }
}
