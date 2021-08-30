package com.codesoom.assignment.application;

public interface IdGenerator<T> {
    T generate(Object source);
}
