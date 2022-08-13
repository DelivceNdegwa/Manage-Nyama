package com.delivce.managenyama.models;

public class DailySales {
    long id, ownerId, categoryId, noOfSales;
    float totalPrice, totalQuantity;

    public DailySales() {
    }

    public DailySales(long ownerId, long categoryId, long noOfSales, float totalPrice, float totalQuantity) {
        this.ownerId = ownerId;
        this.categoryId = categoryId;
        this.noOfSales = noOfSales;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getNoOfSales() {
        return noOfSales;
    }

    public void setNoOfSales(long noOfSales) {
        this.noOfSales = noOfSales;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(float totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
