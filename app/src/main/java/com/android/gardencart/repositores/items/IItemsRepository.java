package com.android.gardencart.repositores.items;

import com.android.gardencart.models.Item;

import java.util.List;

public interface IItemsRepository {
    List<Item> getItems();
    Item getItemByItemId(String itemId);
    List<String> getTags();
    void resetItems();
    void putItems(List<Item> newItems);
}
