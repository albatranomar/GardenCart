package com.android.gardencart.repositores.items;

import com.android.gardencart.R;
import com.android.gardencart.models.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockItems implements IItemsRepository {
    private final String[] allTags = new String[]{"organic", "local", "imported", "fresh", "seasonal"};
    private static MockItems instance;
    private List<Item> items;

    public static MockItems getInstance() {
        if (instance == null) {
            instance = new MockItems();
        }

        return instance;
    }

    @Override
    public List<Item> getItems() {
        if (items == null || items.isEmpty()) {
            items = getMockItems();
        }

        return items;
    }

    @Override
    public Item getItemByItemId(String itemId) {
        for (Item i: items) {
            if (i.getId().equalsIgnoreCase(itemId)) {
                return i;
            }
        }

        return null;
    }

    @Override
    public List<String> getTags() {
        return Arrays.asList(allTags);
    }

    @Override
    public void resetItems() {
        items = getMockItems();
    }

    @Override
    public void putItems(List<Item> newItems) {
        items = newItems;
    }

    private List<Item> getMockItems() {
        return Arrays.asList(
                new Item("0#", "Bean", R.drawable.bean, Arrays.asList("organic", "fresh"), 3.45, 25),
                new Item("1#", "Bitter_Gourd", R.drawable.bitter_gourd, Arrays.asList("local", "seasonal"), 2.78, 15),
                new Item("2#", "Bottle_Gourd", R.drawable.bottle_gourd, Arrays.asList("imported", "fresh", "organic"), 4.12, 10),
                new Item("3#", "Brinjal", R.drawable.brinjal, Arrays.asList("local", "fresh"), 1.99, 30),
                new Item("4#", "Broccoli", R.drawable.broccoli, Arrays.asList("organic", "seasonal"), 3.87, 20),
                new Item("5#", "Cabbage", R.drawable.cabbage, Arrays.asList("local", "fresh"), 1.25, 50),
                new Item("6#", "Capsicum", R.drawable.capsicum, Arrays.asList("imported", "organic"), 2.56, 12),
                new Item("7#", "Carrot", R.drawable.carrot, Arrays.asList("fresh", "seasonal"), 0.99, 40),
                new Item("8#", "Cauliflower", R.drawable.cauliflower, Arrays.asList("local", "organic"), 2.34, 18),
                new Item("9#", "Cucumber", R.drawable.cucumber, Collections.singletonList("fresh"), 1.45, 22),
                new Item("10#", "Papaya", R.drawable.papaya, Arrays.asList("imported", "seasonal"), 4.67, 8),
                new Item("11#", "Potato", R.drawable.potato, Arrays.asList("local", "fresh", "organic"), 0.78, 60),
                new Item("12#", "Pumpkin", R.drawable.pumpkin, Arrays.asList("seasonal", "organic"), 3.12, 14),
                new Item("13#", "Radish", R.drawable.radish, Arrays.asList("local", "fresh"), 1.67, 27),
                new Item("14#", "Tomato", R.drawable.tomato, Arrays.asList("imported", "fresh"), 2.99, 35)
        );
    }
}
