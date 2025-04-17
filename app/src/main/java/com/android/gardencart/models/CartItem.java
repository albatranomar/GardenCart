package com.android.gardencart.models;

import java.util.List;

public class CartItem extends Item {
    private int amount;
    
    public CartItem(String id, String name, int image, List<String> tags, double pricePerUnit, int amount, int availableQuantity) {
        super(id, name, image, tags, pricePerUnit, availableQuantity);
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
