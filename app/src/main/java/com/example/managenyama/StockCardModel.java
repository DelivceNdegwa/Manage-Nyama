package com.example.managenyama;

public class StockCardModel {
    String stockName, stockQuantity;
    int  stockImage;


    public StockCardModel(String stockName, int stockImage) {
        this.stockName = stockName;
        this.stockImage = stockImage;

    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getStockImage() {
        return stockImage;
    }

    public void setStockImage(int stockImage) {
        this.stockImage = stockImage;
    }

    public String getStockQuantity() { return stockQuantity; }

    public void setStockQuantity(String stockQuantity) { this.stockQuantity = stockQuantity; }

}
