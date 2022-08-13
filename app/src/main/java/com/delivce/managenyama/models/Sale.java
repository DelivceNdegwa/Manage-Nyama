package com.delivce.managenyama.models;

public class Sale {
    long id, stockId;
    float salePrice;
    String dateMade;
    boolean isCustomizedPrice;

    public Sale() {
    }

    public Sale(long stockId, float salePrice, String dateMade, boolean isCustomizedPrice) {
        this.stockId = stockId;
        this.salePrice = salePrice;
        this.dateMade = dateMade;
        this.isCustomizedPrice = isCustomizedPrice;
    }

    public Sale(long id, long stockId, float salePrice, String dateMade, boolean isCustomizedPrice) {
        this.id = id;
        this.stockId = stockId;
        this.salePrice = salePrice;
        this.dateMade = dateMade;
        this.isCustomizedPrice = isCustomizedPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public String getDateMade() {
        return dateMade;
    }

    public void setDateMade(String dateMade) {
        this.dateMade = dateMade;
    }

    public boolean isCustomizedPrice() {
        return isCustomizedPrice;
    }

    public void setCustomizedPrice(boolean customizedPrice) {
        isCustomizedPrice = customizedPrice;
    }
}
