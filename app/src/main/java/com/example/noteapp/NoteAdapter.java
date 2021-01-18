package com.example.noteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private OnItemClickListenerNote onItemClickListenerNote;
    private ArrayList<Note> notes;

    public NoteAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.editText.setText(note.getNote());
        holder.itemView.setBackgroundColor(note.getColor());
        if(note.getImageUri() != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageURI(note.getImageUri());
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        if(note.isChecked() != null) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(note.isChecked());
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView editText;
        ImageView imageView;
        CheckBox checkBox;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            editText = itemView.findViewById(R.id.edit_text_note);
            checkBox = itemView.findViewById(R.id.check_box_note);
            imageView = itemView.findViewById(R.id.image_view_note);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListenerNote != null) {
                        onItemClickListenerNote.onItemClicked(getAdapterPosition(), notes.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public void setOnItemClickListenerNote(OnItemClickListenerNote onItemClickListenerNote) {
        this.onItemClickListenerNote = onItemClickListenerNote;

    }

    interface OnItemClickListenerNote {
        void onItemClicked(int adapterPosition, Note note);
    }

    public void updateData(ArrayList<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }
}
