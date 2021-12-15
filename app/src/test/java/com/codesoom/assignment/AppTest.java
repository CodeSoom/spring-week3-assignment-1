package com.codesoom.assignment;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void getGreeting() {
        String greeting = "Hello, world!";
        App app = new App();
        assertThat(app.getGreeting()).isEqualTo(greeting);
    }

    @Test
    void main() {
        String[] args = {""};
        App.main(args);
    }
}