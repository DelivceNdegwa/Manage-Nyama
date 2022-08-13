package com.delivce.managenyama.models;

public class MeatCategory {
    long id, ownerId;
    float defaultPrice;
    String name;

    public MeatCategory() {
    }

    public MeatCategory(float defaultPrice, long ownerId, String name) {
        this.defaultPrice = defaultPrice;
        this.ownerId = ownerId;
        this.name = name;
    }

    public MeatCategory(long id, float defaultPrice, long ownerId, String name) {
        this.id = id;
        this.defaultPrice = defaultPrice;
        this.ownerId = ownerId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(float defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
