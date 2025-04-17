package com.android.gardencart.models;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String id;
    private String name;
    private int image;
    private List<String> tags;
    private double pricePerUnit;
    private int availableQuantity;

    public Item(String id, String name, int image, List<String> tags, double pricePerUnit, int availableQuantity) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.tags = (tags == null) ? new ArrayList<>() : tags;
        this.pricePerUnit = pricePerUnit;
        this.availableQuantity = availableQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}
