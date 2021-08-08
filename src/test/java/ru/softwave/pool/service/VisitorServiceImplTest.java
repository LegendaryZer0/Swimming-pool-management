package ru.softwave.pool.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.softwave.pool.model.entity.Visitor;
import ru.softwave.pool.repo.VisitorRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("VisitorServiceImpl is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
class VisitorServiceImplTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private VisitorRepository visitorRepository;
    @Autowired
    private VisitorService visitorService;

    private Visitor visitor;
    @BeforeEach
    void setUp() {
        visitor = Visitor.builder().name("name").sex(true).email("some@email").hashPassword("pass").build();
        when(visitorRepository.findVisitorByEmail("some@email")).thenReturn(Optional.of(visitor));
        when(visitorRepository.save(visitor)).thenReturn(visitor);
    }

    @Nested
    @DisplayName("registrVisitor() is working")
    class SaveVisitor{
        @Test
        void saveVisitor() {
           try{
               visitorService.saveVisitor(visitor);
           }catch (Exception e){
               fail();
           }

        }

    }


    @Nested
    @DisplayName("findVisitorByEmail is working")
    class FindVisitorByEmail{
        @Test
        void return_visitor_with_email() {
            assertThat(visitorService.findVisitorByEmail("some@email"),is(visitor));

        }
    }

}