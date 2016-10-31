package com.example.rain.chickenfarmtycoon;

import java.io.Serializable;

/**
 * Created by rain on 2016/10/15.
 */
public class Chicken implements Serializable{
    private String name;
    private int level;
    private int weight;
    private boolean hen;
    private double price;

    public Chicken(int level) {
        RandomHelper randomHelper = new RandomHelper();
        if(randomHelper.makeRandom(1,2) == 1) {
            this.hen = true;
            this.name = "母鸡";
        }
        else {
            this.hen = false;
            this.name = "公鸡";
        }

        this.level = level;
        this.weight = 0;
        this.price = 0;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isHen() {
        return hen;
    }

    public void setHen(boolean hen) {
        this.hen = hen;
    }

    public double getPrice() {
        price = 0.03*weight;
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void growWeight() {
        weight = weight + 15*level;
    }
}
