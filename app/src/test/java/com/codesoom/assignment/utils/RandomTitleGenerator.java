package com.codesoom.assignment.utils;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RandomTitleGenerator {

    /**
     * 10자리의 랜덤한 문자열을 생성하여 반환합니다.
     * ex) ׅ�i�lV��,��T-c\�K3
     *
     * @return 10자리의 랜덤한 문자열
     */
    public static String getRandomTitle() {
        byte[] array = new byte[10];
        new Random().nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }

    /**
     * 인자로 주어진 문자열과 다른 랜덤 문자열을 리턴한다.
     *
     * @param s 동일하지 않아야 할 문자열
     * @return s와 다른 랜덤 문자열
     */
    public static String getRandomTitleDifferentFrom(String s) {
        String title;

        do {
            title = RandomTitleGenerator.getRandomTitle();
        } while (title.equals(s));

        return title;
    }
}
