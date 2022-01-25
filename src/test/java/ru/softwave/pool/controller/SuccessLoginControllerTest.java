package ru.softwave.pool.controller;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.softwave.pool.service.UserService;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.BaseMatcher.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SuccessLoginController is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
public class SuccessLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private UserService userService;

  @Nested
  @DisplayName("confirmAccount() is working")
  class ConfirmAccount {
        @Test
        public void return_confirm_message() throws Exception {
            mockMvc.perform(get("/success/confirm/123e4567-e89b-12d3-a456-426614174000")).andExpect(status().isOk());
        }

    }

    @Nested
    @DisplayName("successLogin() is working")
    class successLogin{
        @Test
        @WithMockUser
        public void return_welcome_message() throws Exception {
            mockMvc.perform(post("/success/welcome")).andExpect(status().isOk());
        }
    }

}
