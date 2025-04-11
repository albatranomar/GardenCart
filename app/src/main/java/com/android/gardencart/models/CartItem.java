package com.android.gardencart.models;

import java.util.List;

public class CartItem extends Item {
    private int amount;
    
    public CartItem(String id, String name, List<String> tags, double pricePerUnit, int amount) {
        super(id, name, tags, pricePerUnit);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", pricePerUnit=" + getPricePerUnit() +
                ", amount=" + amount +
                '}';
    }
}
