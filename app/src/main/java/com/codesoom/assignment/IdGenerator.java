package com.codesoom.assignment;

@FunctionalInterface
public interface IdGenerator<T> {
    T generate();
}
