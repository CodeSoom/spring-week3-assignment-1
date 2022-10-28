package com.codesoom.assignment.utils;

import java.util.Random;

public class NumberGenerator {

    /**
     * 1 ~ 100의 랜덤한 int 타입 정수를 리턴한다.
     *
     * @return 1 ~ 100의 랜덤한 int 타입 정수
     */
    public static int getRandomIntegerBetweenOneAndOneHundred() {
        return new Random().nextInt(101);
    }

    /**
     * 0 또는 양수의 랜덤한 long 타입 정수를 리턴한다.
     *
     * @return 0 또는 양수의 랜덤한 long 타입 정수
     */
    public static Long getRandomNotNegativeLong() {
        long l = new Random().nextLong();
        return l < 0 ? -l : l;
    }

    /**
     * 음수의 랜덤한 long 타입 정수를 리턴한다.
     *
     * @return 음수의 랜덤한 long 타입 정수
     */
    public static Long getRandomNegativeLong() {
        long l;
        do {
            l = -getRandomNotNegativeLong();
        } while (l == 0L);

        return l;
    }
}
