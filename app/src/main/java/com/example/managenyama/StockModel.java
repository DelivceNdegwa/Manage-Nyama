package com.example.managenyama;

public class StockModel {
    String stockName;
    String stockQuantity;
    String stockPrice;
    String butcheryName;
    public StockModel(){

    }

    public StockModel(String stockName, String stockQuantity, String stockPrice, String butcheryName){
        this.stockName = stockName;
        this.stockQuantity = stockQuantity;
        this.stockPrice = stockPrice;
        this.butcheryName = butcheryName;
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

    public String getButcheryName() { return butcheryName; }

    public void setButcheryName(String butcheryName) { this.butcheryName = butcheryName; }
}
