package ru.softwave.pool.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.softwave.pool.model.entity.Trainer;
import ru.softwave.pool.repo.TrainerRepository;
import ru.softwave.pool.util.comparator.AvailableTrainersPreferredTimeComparator;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("GroupServiceImpl is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
class TrainerServiceImplTest {

    @Autowired
    private TrainerService trainerService;


    @Autowired
    @MockBean
    private TrainerRepository trainerRepository;
    @Autowired
    private AvailableTrainersPreferredTimeComparator trainerComparator;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer =
                Trainer.builder()
                        .email("email@address")
                        .name("Ася")
                        .minWorkWeekHours((byte) 0)
                        .maxWorkWeekHours((byte) 125)
                        .currentWorkWeekHours((byte) 0)
                        .email("email@address")
                        .name("SomeName")
                        .build();
        when(trainerRepository.findByEmail("email@address")).thenReturn(java.util.Optional.ofNullable(trainer));
        when(trainerRepository.findByEmail("toThrow")).thenReturn(Optional.empty());
    }
    @Nested
    @DisplayName("findByEmail is working()")
    class FindByEmail{
        @Test
        public  void return_trainer_by_email(){
            trainerService.findTrainerByEmail("email@address");
        }

        @Test
        public void throw_exception_when_trainer_not_found(){
           assertThrows(EntityNotFoundException.class,()->{trainerService.findTrainerByEmail("toThrow");}) ;
        }
    }
}