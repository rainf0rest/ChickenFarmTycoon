package com.example.rain.chickenfarmtycoon;

import java.io.Serializable;
import java.text.ChoiceFormat;

/**
 * Created by rain on 2016/10/28.
 */
public class Egg implements Serializable {

    private String name;

    private int level;

    private int bornTime;

    private int price;

    public Egg(int level) {
        name = "蛋";
        this.level = level;
        bornTime = 10* level;
        price = 1* level;
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

    public int getBornTime() {
        return bornTime;
    }

    public void setBornTime(int bornTime) {
        this.bornTime = bornTime;
    }

    public boolean decBornTime() {
        bornTime--;
        if(bornTime == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public Chicken born() {
        Chicken newChciken = new Chicken(level);
        return newChciken;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
