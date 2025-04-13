package com.android.gardencart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gardencart.R;

import java.util.List;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ChipViewHolder> {
    private final Context context;
    private final List<String> tags;
    private final OnChipClickedListener listener;

    public ChipAdapter(Context context, List<String> tags, OnChipClickedListener listener) {
        this.context = context;
        this.tags = tags;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("CHAPADAPTER", "idk");
        View view = LayoutInflater.from(context).inflate(R.layout.component_item_tag_chip, parent, false);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        String label = tags.get(position);
        holder.button.setText(label);
        holder.button.setTextColor(Color.WHITE);
        holder.button.setOnClickListener(v -> listener.onClick(label));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public interface OnChipClickedListener {
        void onClick(String label);
    }

    public static class ChipViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public ChipViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.chipButton);
        }
    }
}
