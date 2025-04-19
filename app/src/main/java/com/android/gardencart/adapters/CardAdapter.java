package com.android.gardencart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gardencart.R;
import com.android.gardencart.models.Item;

import java.util.List;
import java.util.stream.Collectors;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<Item> items;
    private final Context context;
    private final OnAddClickedListener listener;

    public CardAdapter(Context context, List<Item> items, OnAddClickedListener listener) {
        this.context = context;
        this.items = items.stream().filter(i -> i.getAvailableQuantity() > 0).collect(Collectors.toList());
        this.listener = listener;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items.stream().filter(i -> i.getAvailableQuantity() > 0).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Item item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(item.getPricePerUnit() + "$ / unit");
        holder.image.setImageResource(item.getImage());
        holder.button.setOnClickListener(v -> this.listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnAddClickedListener {
        void onClick(Item item);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;
        ImageView button;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCardTitle);
            price = itemView.findViewById(R.id.tvCardPrice);
            image = itemView.findViewById(R.id.ivCardImage);
            button = itemView.findViewById(R.id.ibAddToCart);
        }
    }
}
