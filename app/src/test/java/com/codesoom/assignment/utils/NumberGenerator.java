package com.codesoom.assignment.utils;

import java.util.Random;

public class NumberGenerator {
    public static int getRandomIntegerBetweenOneAndOneHundred() {
        return new Random().nextInt(101);
    }

    public static Long getRandomNotNegativeLong() {
        long id = new Random().nextLong();
        return id < 0 ? -id : id;
    }
}
