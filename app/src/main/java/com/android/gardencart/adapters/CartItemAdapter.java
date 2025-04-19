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
import com.android.gardencart.models.CartItem;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<CartItem> items;
    private final Context context;
    private final OnRemoveClickedListener listener;

    public CartItemAdapter(Context context, List<CartItem> items, OnRemoveClickedListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_cart_item_card, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.name.setText(item.getName());
        holder.details.setText(item.getPricePerUnit() + "$ / unit * (" + item.getAmount() + " units)");
        holder.image.setImageResource(item.getImage());
        holder.button.setOnClickListener(v -> {
            items.remove(position);
            listener.onClick(item);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnRemoveClickedListener {
        void onClick(CartItem item);
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, details;
        ImageView image;
        ImageView button;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCartItemName);
            details = itemView.findViewById(R.id.tvCartItemDetails);
            image = itemView.findViewById(R.id.ivCartItemImage);
            button = itemView.findViewById(R.id.btRemoveCartItem);
        }
    }
}
