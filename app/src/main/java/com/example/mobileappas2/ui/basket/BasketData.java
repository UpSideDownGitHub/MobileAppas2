package com.example.mobileappas2.ui.basket;

import com.example.mobileappas2.Database.DataHolders.Products;

import java.util.ArrayList;

public class BasketData {
	// list of profucts that can be added to and taken away with 
	// genraliszed functions with all of the functionlity that will be 
	// needed 
    private static ArrayList<Products> products = new ArrayList();
    public static void addProduct(Products toAdd) { products.add(toAdd); }
    public static void removeProduct(int position) { products.remove(position); }
    public static void removeAllProducts() { products.clear(); }
    public static ArrayList<Products> getBasketData(){ return products; }
    public static int length() { return products.size(); }
}
