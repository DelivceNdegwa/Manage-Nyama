package com.delivce.managenyama.utils;

import java.util.HashMap;
import java.util.Map;

public class CommonVariables {
    // History notifications
    public final static int NEW_STOCK_PURCHASE = 1;
    public final static int DEPLETED_STOCK_PURCHASE = 2;
    public final static int UNFOLDED_STOCK = 3;
    public final static int SALE_MADE = 4;
    public final static int NEW_SUPPLIER = 5;

    public static final String STOCK_MONITOR_COLLECTION = "stock_monitor";
    public final String SALE_COLLECTION = "meat_sales";
    public final String CATEGORY_COLLECTION= "meat_categories";
    public final String SUPPLIER_COLLECTION= "meat_suppliers";
    public final String STOCK_COLLECTION = "meat_stock";

    public final String STOCK_MONITOR_PENDING = "pending";
    public final String STOCK_MONITOR_ACTIVE = "active";
    public final String STOCK_MONITOR_DEPLETED = "depleted";
    public final float STOCK_MONITOR_DEFAULT_SALE_QUANTITY = 0;
    public final float STOCK_MONITOR_DEFAULT_SALE_CUMMULATIVE_PRICE = 0;

    

}
