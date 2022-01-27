package com.example.guitartrainer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChordListAdapter extends RecyclerView.Adapter<ChordListAdapter.ViewHolder> {
    private final Chord[] chords;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    System.out.println("Element " + position + " clicked.");
                    Intent intent = new Intent(v.getContext(), ChordDetailActivity.class);

                    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                    String chord = gson.toJson(chords[position]);
                    intent.putExtra(Chord.CHORD_EXTRA_KEY, chord);

                    v.getContext().startActivity(intent);
                }
            });
            textView = (TextView) v.findViewById(R.id.historyRowView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ChordListAdapter(Chord[] chords) {
        this.chords = chords;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chord_list_item, viewGroup, false);

        return new ViewHolder(v);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Chord chord = chords[position];
        String displayText = chord.name;
        viewHolder.getTextView().setTextSize(24);
        viewHolder.getTextView().setText(displayText);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return chords.length;
    }
}
