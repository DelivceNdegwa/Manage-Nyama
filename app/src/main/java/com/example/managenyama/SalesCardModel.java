package com.example.managenyama;

public class SalesCardModel {
    String salesName, saleNumber;
    int salesImage;


    public SalesCardModel(String salesName, int salesImage, String saleNumber) {
        this.salesName = salesName;
        this.salesImage = salesImage;
        this.saleNumber = saleNumber;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public int getSalesImage() {
        return salesImage;
    }

    public void setSalesImage(int salesImage) {
        this.salesImage = salesImage;
    }
}
