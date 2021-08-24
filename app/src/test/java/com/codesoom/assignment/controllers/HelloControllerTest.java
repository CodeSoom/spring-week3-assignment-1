package com.codesoom.assignment.controllers;

import com.codesoom.assignment.controllers.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class HelloControllerTest {
    @Autowired
    HelloController helloController;
    @Test
    void sayHello(){
        //HelloController helloController2 = new HelloController();
        String result = helloController.sayHello();
        assertThat(result).isNotEmpty()
                            .isNotBlank()
                            .isEqualTo("hello world");
    }
}
