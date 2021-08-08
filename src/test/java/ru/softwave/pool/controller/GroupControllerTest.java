package ru.softwave.pool.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.softwave.pool.model.entity.Group;
import ru.softwave.pool.service.GroupService;

import javax.persistence.EntityNotFoundException;
import java.sql.Time;
import java.time.LocalTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("GroupController is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
public class GroupControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private GroupService groupService;

  @BeforeEach
  private void setUp() {
    when(groupService.findGroupByName("test")).thenReturn(Group.builder().name("test").build());
    when(groupService.createGroupSimple("A-111"))
        .thenReturn(
            Group.builder()
                .groupCapacity((byte) 0)
                .femaleMaxCount((byte) 0)
                .maleMaxCount((byte) 0)
                .femaleCurrentCount((byte) 0)
                .maleCurrentCount((byte) 0)
                .endTime(Time.valueOf(LocalTime.of(0, 0)))
                .startTime(Time.valueOf(LocalTime.of(0, 0)))
                .name("A-111")
                .build());

    when(groupService.findGroupByName("toThrow")).thenThrow( new EntityNotFoundException("There is no such group"));
  }
  @DisplayName("findByName(String name) is working")
  @Nested
  class FindByNameTest{
    @Test
    @WithMockUser(authorities = "ADMIN")
    public void return_group_found() throws Exception{
      mockMvc.perform(get("/group-management/groups/test")).andDo(print()).andExpect(jsonPath("$.name",is("test")));
    }
    @Test
    @WithMockUser(authorities = "VISITOR")
    public void deny_visitor_access() throws Exception {
      mockMvc.perform(get("/group-management/groups/test")).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void throw_on_not_exists() throws Exception{
      mockMvc.perform(get("/group-management/groups/toThrow")).andDo(print()).andExpect(status().isConflict());
    }
  }
  @Nested
  @DisplayName("createGroupSimple() is working")
  class CreateGroupSimple{
    @Test
    @WithMockUser(authorities = "ADMIN")
    public void return_simple_created_group() throws Exception {
      mockMvc.perform(post("/group-management/groups/A-111")).andDo(print()).andExpect(jsonPath("$.name",is("A-111")));
    }
  }


}
