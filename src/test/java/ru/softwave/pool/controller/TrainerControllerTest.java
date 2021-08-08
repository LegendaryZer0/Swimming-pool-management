package ru.softwave.pool.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.softwave.pool.model.dto.AvailableTrainersDto;
import ru.softwave.pool.service.TrainerService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TrainerController is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
class TrainerControllerTest {
  @Autowired @MockBean private TrainerService trainerService;
  @Autowired private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    when(trainerService.getAvailableTrainers(new String[] {"minWorkWeekHours"}))
        .thenReturn(
            List.of(
                AvailableTrainersDto.builder().minWorkWeekHours((byte) 0).build(),
                AvailableTrainersDto.builder().minWorkWeekHours((byte) 1).build(),
                AvailableTrainersDto.builder().minWorkWeekHours((byte) 2).build()));
    when(trainerService.getAvailableTrainersSortedByTimeIntersection("8:00:00","12:00:00"))
            .thenReturn(
                    List.of(
                            AvailableTrainersDto.builder().name("Valla").minWorkWeekHours((byte) 0).build(),
                            AvailableTrainersDto.builder().minWorkWeekHours((byte) 1).build(),
                            AvailableTrainersDto.builder().minWorkWeekHours((byte) 2).build()));
  }


  @Nested
  @DisplayName("getAvailableTrainers() is working")
  class GetAvailableTrainers {

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void return_trainers_sorted_by_minWorkWeekHours() throws Exception {
      mockMvc
          .perform(get("/trainer-management/trainers").param("sortProperties", "minWorkWeekHours"))
          .andDo(print())
          .andExpect(jsonPath("$[0].minWorkWeekHours", is(0)));
    }
  }

  @Nested
  @WithMockUser(authorities = "ADMIN")
  @DisplayName("getAvailableTrainersSortedByIntersection() is working")
  class getAvailableTrainersSortedByIntersection {
    @Test
    public void return_trainers_sorted_by_intersection_time() throws Exception {
      mockMvc
          .perform(
              get("/trainer-management/trainers/sorted/intersection")
                  .param("timeStart", "8:00:00")
                  .param("timeEnd", "12:00:00"))
          .andDo(print())
              .andExpect(jsonPath("$[0].name",is("Valla")));
    }
  }
}
