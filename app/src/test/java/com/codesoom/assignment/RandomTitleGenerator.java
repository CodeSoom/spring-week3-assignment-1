package com.codesoom.assignment;

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
}
