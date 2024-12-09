package me.animepdf.utils;

import java.util.ArrayList;

public class ItemCalculator {
    public static ArrayList<Integer> calculate(int number, int[] capacities) {
        ArrayList<Integer> distribution = new ArrayList<>();
        int remaining = number;

        for (int capacity : capacities) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Layer capacities must be greater than zero.");
            }
            int count = remaining / capacity;
            distribution.add(count);
            remaining %= capacity;
        }
        distribution.add(remaining);

        return distribution;
    }
}
