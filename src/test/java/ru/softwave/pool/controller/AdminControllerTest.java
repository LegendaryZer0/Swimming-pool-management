package ru.softwave.pool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.softwave.pool.model.dto.AdminSaveDto;
import ru.softwave.pool.service.AdminService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AdminController is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    private AdminSaveDto adminSaveDto;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){

        adminSaveDto = AdminSaveDto.builder().name("name here").password("passwordHere").username("username@Here").build();
        doNothing().when(adminService).registrAdmin(adminSaveDto.convertToAdmin());
        doThrow(new IllegalArgumentException()).when(adminService).registrAdmin(null);
    }

    @Nested

    @DisplayName("registrAdmin is working()")
    class SaveAdmin{
        @WithMockUser(authorities = "ADMIN")
        @Test
        public void return_ok_on_valid_dto() throws Exception {
            mockMvc.perform(post("/admin-management/admins").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(adminSaveDto))).andExpect(status().isOk()).andDo(print());
        }

        @Test
        @WithMockUser(authorities = "VISITOR")
        public void deny_on_bad_authority() throws Exception {
            mockMvc.perform(post("/admin-management/admins").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(adminSaveDto))).andExpect(status().isForbidden()).andDo(print());
        }

    }



}
