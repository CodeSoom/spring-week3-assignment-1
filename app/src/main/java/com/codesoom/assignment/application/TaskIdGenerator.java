package com.codesoom.assignment.application;

import com.codesoom.assignment.exceptions.NotSupportedIdTypeException;
import org.springframework.stereotype.Component;

@Component
public class TaskIdGenerator implements IdGenerator<Long>{

    public static final long INCREASE_NUMBER = 1L;

    @Override
    public Long generate(Object source) {
        if (!(source instanceof Long)) {
            throw new NotSupportedIdTypeException(source.getClass().getName(), Long.class.getName());
        }

        Long sequence = (Long) source;
        return sequence + INCREASE_NUMBER;
    }
}
