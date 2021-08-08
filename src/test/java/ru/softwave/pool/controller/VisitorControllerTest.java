package ru.softwave.pool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.softwave.pool.model.dto.VisitorDto;
import ru.softwave.pool.service.VisitorService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("VisitorController is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
class VisitorControllerTest {

  @MockBean @Autowired private VisitorService visitorService;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    doNothing()
        .when(visitorService)
        .registrVisitor(
            (VisitorDto.builder()
                .sex(false)
                .username("qwerty007@email.com")
                .name("Anna")
                .build()
                .convertToVisitor()));
  }

  @Nested
  @DisplayName("registrVisitor is working")
  class SaveVisitor {
    @Test
    void save_valid_visitor() throws Exception {

      mockMvc
          .perform(
              post("/visitor-management/visitor")
                  .accept(MediaType.APPLICATION_JSON)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(
                      objectMapper.writeValueAsString(
                          VisitorDto.builder()
                              .sex(false)
                              .username("qwerty007@email.com")
                              .password("anypassword here")
                              .name("Anna")
                              .build())))
          .andDo(print())
          .andExpect(jsonPath("$", is("OK")));
    }

    @Test
    void save_nameless_visitor_throws_exception() throws Exception {

      mockMvc
          .perform(
              post("/visitor-management/visitor")
                  .accept(MediaType.APPLICATION_JSON)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(
                      objectMapper.writeValueAsString(
                          VisitorDto.builder()
                              .sex(false)
                              .username("qwerty007@email.com")
                              .password("anypassword here")
                              .build())))
          .andDo(print())
          .andExpect(status().is(400));
    }
  }
}
