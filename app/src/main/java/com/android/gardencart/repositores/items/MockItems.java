package com.android.gardencart.repositores.items;

import com.android.gardencart.R;
import com.android.gardencart.models.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        if (items != null && !items.isEmpty()) {
            return items;
        }

        String[] vegetableNames = new String[]{
                "Bean", "Bitter_Gourd", "Bottle_Gourd", "Brinjal", "Broccoli",
                "Cabbage", "Capsicum", "Carrot", "Cauliflower", "Cucumber",
                "Papaya", "Potato", "Pumpkin", "Radish", "Tomato"
        };

        int[] vegetableImages = new int[]{
                R.drawable.bean,
                R.drawable.bitter_gourd,
                R.drawable.bottle_gourd,
                R.drawable.brinjal,
                R.drawable.broccoli,
                R.drawable.cabbage,
                R.drawable.capsicum,
                R.drawable.carrot,
                R.drawable.cauliflower,
                R.drawable.cucumber,
                R.drawable.papaya,
                R.drawable.potato,
                R.drawable.pumpkin,
                R.drawable.radish,
                R.drawable.tomato
        };

        items = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 15; i++) {
            String id = i + "#";
            String name = vegetableNames[i];
            int image = vegetableImages[i];
            double price = 0.5 + (5.0 - 0.5) * random.nextDouble();
            Collections.shuffle(Arrays.asList(allTags));
            List<String> tags = Arrays.asList(allTags).subList(0, random.nextInt(3) + 1);

            items.add(new Item(id, name, image, new ArrayList<>(tags), Math.round(price * 100.0) / 100.0));
        }

        return items;
    }

    @Override
    public List<String> getTags() {
        return Arrays.asList(allTags);
    }
}
