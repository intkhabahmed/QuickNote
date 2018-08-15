package com.intkhabahmed.quicknote.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intkhabahmed.quicknote.R;
import com.intkhabahmed.quicknote.models.Note;
import com.intkhabahmed.quicknote.utils.AppConstants;
import com.intkhabahmed.quicknote.utils.DateUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Note> mNotes;
    private OnItemClickListener mItemClickListener;

    public NotesAdapter(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Note note);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = mNotes.get(holder.getAdapterPosition());
        holder.noteTitle.setText(note.getTitle());
        holder.hashTag.setText("");
        for (String tag : note.getHashTags()) {
            holder.hashTag.append(String.format("%s%s%s", "#", tag.trim(), " "));
        }
        holder.noteDateCreated.setText(DateUtils.getFormattedTime(note.getDateCreated(), System.currentTimeMillis()));
        String description = note.getDescription();
        if (description.length() > 30) {
            holder.noteDescription.setText(String.format("%s%s", note.getDescription().substring(0, 30), "..."));
        } else {
            holder.noteDescription.setText(note.getDescription());
        }
        holder.itemView.setTag(note.getId());
    }

    @Override
    public int getItemCount() {
        if (mNotes == null) {
            return 0;
        }
        return mNotes.size();
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView noteTitle;
        private TextView noteDateCreated;
        private TextView hashTag;
        private TextView noteDescription;

        NotesViewHolder(View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title_tv);
            noteDateCreated = itemView.findViewById(R.id.date_created_tv);
            hashTag = itemView.findViewById(R.id.hash_tag_tv);
            noteDescription = itemView.findViewById(R.id.note_description_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onClick(mNotes.get(getAdapterPosition()));
        }
    }
}
