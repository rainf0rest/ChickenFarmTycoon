package com.example.rain.chickenfarmtycoon;

import java.io.Serializable;
import java.text.ChoiceFormat;

/**
 * Created by rain on 2016/10/28.
 */
public class Egg implements Serializable {

    private String name;

    private int level;

    private int bornTime, bornTimeTop;
    private boolean born;

    private int price;

    public Egg(int level) {
        name = "蛋";
        this.level = level;
        bornTime = 10* level;
        bornTimeTop = bornTime;
        price = 1* level;
        born = false;
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

    public boolean isBorn() {
        return born;
    }

    public void setBorn(boolean born) {
        this.born = born;
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
        RandomHelper randomHelper = new RandomHelper();
        newChciken.setWeight(50 + randomHelper.makeRandom(1, 5)*5);
        return newChciken;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBornTimeTop() {
        return bornTimeTop;
    }

    public void setBornTimeTop(int bornTimeTop) {
        this.bornTimeTop = bornTimeTop;
    }
}
