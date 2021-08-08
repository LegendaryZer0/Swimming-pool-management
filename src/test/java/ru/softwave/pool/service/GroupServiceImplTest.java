package ru.softwave.pool.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.softwave.pool.model.entity.Group;
import ru.softwave.pool.model.entity.Trainer;
import ru.softwave.pool.repo.GroupRepository;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("GroupServiceImpl is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
class GroupServiceImplTest {

  @Autowired private GroupService groupService;

  @Autowired @MockBean private TrainerService trainerService;

  @Autowired @MockBean private GroupRepository groupRepository;

  private Trainer trainer;

  private Trainer busyTrainer;

  private Group validGroup;

  @BeforeEach
  public void setUp() {
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
    validGroup =
        Group.builder()
            .startTime(Time.valueOf("15:00:24"))
            .endTime(Time.valueOf("16:00:11"))
            .name("A-111")
            .build();
    busyTrainer =
        Trainer.builder()
            .email("busyemail@address")
            .name("Ася")
            .minWorkWeekHours((byte) 0)
            .maxWorkWeekHours((byte) 125)
            .currentWorkWeekHours((byte) 125)
            .email("busyemail@address")
            .name("SomeName")
            .build();
    doNothing().when(trainerService).registrTrainer(trainer);
    when(groupRepository.save(validGroup)).thenReturn(validGroup);
    when(groupRepository.findByName("A-111")).thenReturn(java.util.Optional.ofNullable(validGroup));
    when(trainerService.findTrainerByEmail("email@address")).thenReturn(trainer);
    when(trainerService.findTrainerByEmail("busyemail@address")).thenReturn(busyTrainer);
  }

  @Nested
  @DisplayName("putTrainerToGroup() is working")
  class PutTrainerToGroup {

    @Test
    public void put_valid_trainer_to_group() {
      groupService.putTrainerToGroup("A-111", "email@address");
    }

    @Test
    public void put_busy_trainer_throws_ex() {
      try {
        groupService.putTrainerToGroup("A-111", "busyemail@address");
        fail("Exception should be thrown");
      } catch (IllegalArgumentException e) {
        System.out.println("Success");
      } catch (Exception e) {
        e.printStackTrace();
        fail("IllegalArgumentException should be thrown");
      }
    }
  }
}
