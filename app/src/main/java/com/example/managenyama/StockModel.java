package com.example.managenyama;

public class StockModel {
    String stockName, stockQuantity, stockPrice;
    public StockModel(){

    }

    public StockModel(String stockName, String stockQuantity, String stockPrice){
        this.stockName = stockName;
        this.stockQuantity = stockQuantity;
        this.stockPrice = stockPrice;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(String stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }
}
