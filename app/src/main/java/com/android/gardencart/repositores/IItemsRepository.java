package com.android.gardencart.repositores;

import com.android.gardencart.models.Item;

import java.util.List;

public interface IItemsRepository {
    List<Item> getItems();
    List<String> getTags();
}
