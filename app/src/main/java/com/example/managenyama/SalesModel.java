package com.example.managenyama;

public class SalesModel {
    String meatType, Quantity, meatPrice, timeOfSale;


    public SalesModel(String meatType, String quantity, String meatPrice, String timeOfSale) {

        this.meatType = meatType;
        Quantity = quantity;
        this.meatPrice = meatPrice;
        this.timeOfSale = timeOfSale;
    }

    public String getMeatType() {
        return meatType;
    }

    public void setMeatType(String meatType) {
        this.meatType = meatType;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getMeatPrice() {
        return meatPrice;
    }

    public void setMeatPrice(String meatPrice) {
        this.meatPrice = meatPrice;
    }

    public String getTimeOfSale() {
        return timeOfSale;
    }

    public void setTimeOfSale(String timeOfSale) {
        this.timeOfSale = timeOfSale;
    }


}
