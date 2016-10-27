package com.example.rain.chickenfarmtycoon;

/**
 * Created by rain on 2016/10/15.
 */
public class Chicken {
    private int kind;//male and female or egg
    //private int health;
    //private int age;
    private String name;
    private int number;

    public Chicken() {

    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void makeName() {
        switch (kind) {
            case 1:
                name = "蛋";
                break;
            case 2:
                name = "母鸡";
                break;
            case 3:
                name = "公鸡";
                break;
            default:
                name = "异种";
                break;
        }
    }
}
