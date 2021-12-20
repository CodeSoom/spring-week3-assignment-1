package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
