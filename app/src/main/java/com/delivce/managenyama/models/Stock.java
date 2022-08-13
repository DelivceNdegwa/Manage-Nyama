package com.delivce.managenyama.models;

public class Stock {
    long id, supplierId, productId;
    float pricePurchased, salesPrice, quantity;
    String dateOfPurchase;

    public Stock() {
    }

    public Stock(long supplierId, long productId, float pricePurchased, float salesPrice, float quantity, String dateOfPurchase) {
        this.supplierId = supplierId;
        this.productId = productId;
        this.pricePurchased = pricePurchased;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.dateOfPurchase = dateOfPurchase;
    }

    public Stock(long id, long supplierId, long productId, float pricePurchased, float salesPrice, float quantity, String dateOfPurchase) {
        this.id = id;
        this.supplierId = supplierId;
        this.productId = productId;
        this.pricePurchased = pricePurchased;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.dateOfPurchase = dateOfPurchase;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public float getPricePurchased() {
        return pricePurchased;
    }

    public void setPricePurchased(float pricePurchased) {
        this.pricePurchased = pricePurchased;
    }

    public float getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(float salesPrice) {
        this.salesPrice = salesPrice;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }
}
