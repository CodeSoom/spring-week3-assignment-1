package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("App 클래스")
class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("getGreeting() 메소드는 ")
    class Describe_getGreeting {
        @Test
        @DisplayName("서버 생존용 문자열을 리턴합니다.")
        void it_return_string() {
            App app = new App();

            assertNotNull(app.getGreeting());
        }
    }

    @Nested
    @DisplayName("GET Mapping - '/healthcheck'는")
    class Describe_get_healthcheck {
        @Test
        @DisplayName("200(Ok)와 서버 생존용 문자열을 리턴합니다.")
        void it_return_ok_and_string() throws Exception {
            mockMvc.perform(get("/healthcheck"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andDo(print());
        }
    }
}
