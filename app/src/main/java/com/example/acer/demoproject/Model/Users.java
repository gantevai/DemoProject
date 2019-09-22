package com.example.acer.demoproject.Model;

/**
 * Created by Acer on 6/20/2019.
 */

public class Users {
    public int totalItem;
    public boolean ratedItem[] = new boolean[15];
    public double ratesOfItem[] = new double[15];

    public Users(double ratesOfItem[], int totalItem) {
        this.ratesOfItem = ratesOfItem;
        this.totalItem = totalItem;
    }

    public void setRatedItem() {
        for (int i = 0; i < totalItem; i++) {
            ratedItem[i] = ratesOfItem[i] != 0;
        }
    }
}
