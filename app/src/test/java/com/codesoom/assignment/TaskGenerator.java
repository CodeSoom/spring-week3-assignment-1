package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class TaskGenerator {

    public static Task generateTask(String title) {
        final Task task = new Task();
        task.setTitle(title);
        return task;
    }

    public static Task generateTaskWithRandomTitle() {
        return generateTask(getRandomTitle());
    }

    private static String getRandomTitle() {
        byte[] array = new byte[10];
        new Random().nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }
}
