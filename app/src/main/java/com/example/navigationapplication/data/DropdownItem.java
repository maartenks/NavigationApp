package com.example.navigationapplication.data;

public class DropdownItem {
    private String itemName;
    private int itemImage;

    public DropdownItem(String itemName, int itemImage) {
        this.itemImage = itemImage;
        this.itemName = itemName;
    }

    public int getItemImage() {
        return itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
