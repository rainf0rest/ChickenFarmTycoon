package com.example.rain.chickenfarmtycoon;

import java.util.Random;
/**
 * Created by rain on 2016/10/28.
 */
/**
 * A class to make random number
 */
public class RandomHelper {
    public RandomHelper() {
    }

    public int makeRandom(int a, int b) {
        Random random = new Random();
        return random.nextInt(b - a + 1) + a;
    }
}