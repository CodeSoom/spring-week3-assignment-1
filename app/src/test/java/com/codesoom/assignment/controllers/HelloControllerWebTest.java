package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("HelloController의 Mockito를 활용한 단위 테스트")
public class HelloControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_sayHello {

        @Nested
        @DisplayName("만약 '/' URL로 요청을 보낼 경우")
        class Context_send_request_to_root_url {

            private String helloMessage = "Hello, world!";

            @Test
            @DisplayName("'Hello, world!' 메세지를 반환합니다.")
            void it_return_hello_message() throws Exception {

                mockMvc.perform(get("/"))
                        .andExpect(content().string(helloMessage));

            }

        }

    }

}
