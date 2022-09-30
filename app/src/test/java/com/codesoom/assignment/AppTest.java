package com.codesoom.assignment;

import com.codesoom.assignment.controllers.HomeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
@DisplayName("APP 클래스")
class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("home(HomeController) 메소드는")
    class Describe_home {
        @Test
        @DisplayName("호출하면 서버 상태를 리턴한다")
        void it_returns_server_status() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Server is OK"));
        }


    }

    @Test
    @DisplayName("Spring Application이 잘 작동하는지 확인한다.")
    void contextLoads() {
        App.main(new String[]{});
    }
}