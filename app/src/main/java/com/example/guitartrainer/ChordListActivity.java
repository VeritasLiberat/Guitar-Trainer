package com.example.guitartrainer;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


public class ChordListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_list);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ChordListFragment fragment = new ChordListFragment();
            transaction.replace(R.id.chord_fragment, fragment);
            transaction.commit();
        }
    }

}
