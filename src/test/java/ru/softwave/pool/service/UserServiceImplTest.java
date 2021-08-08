package ru.softwave.pool.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.softwave.pool.model.entity.TechnicalInfo;
import ru.softwave.pool.model.entity.User;
import ru.softwave.pool.repo.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserServiceImpl is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @MockBean
    @Autowired
    private UserRepository userRepository;

    private User validUser;
    private UUID validUUID;
    private UUID notFoundUUID;
    @BeforeEach
    void setUp() {
        notFoundUUID = UUID.fromString("234e4568-e89b-12d3-a456-426614174000");
        validUUID= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        validUser  = User.builder().email("email").hashPassword("pass").technicalInfo(TechnicalInfo.builder().uuidToConfirm(validUUID).build()).build();
        when(userRepository.save(validUser)).thenReturn(validUser);
        when(userRepository.findByTechnicalInfo_UuidToConfirmEquals(validUUID)).thenReturn(java.util.Optional.ofNullable(validUser));

        when(userRepository.findByTechnicalInfo_UuidToConfirmEquals(notFoundUUID)).thenReturn(Optional.empty());

    }

    @Test
    void sendConfirmEmail() {
    }
        @Nested
        @DisplayName("confirmEmail is working()")
        class ConfirmEmail{
            @Test
            public void confirm_email_with_uuid() {
                userService.confirmEmail(validUUID);
                assertEquals(validUser.getIsConfirmed(),true);
            }

            @Test
            public  void throw_exception_user_not_found(){
                assertThrows(EntityNotFoundException.class,()->{userService.confirmEmail(notFoundUUID);});
            }
        }

}