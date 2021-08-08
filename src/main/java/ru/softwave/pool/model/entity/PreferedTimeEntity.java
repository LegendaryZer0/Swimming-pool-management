package ru.softwave.pool.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Table(name = "PREFERED_TIME_ENTITY")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PreferedTimeEntity {
  @Column(name = "ID", nullable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Time startTime;
  private Time endTime;

  @Fetch(FetchMode.SUBSELECT)
  @ToString.Exclude
  @OneToMany(mappedBy = "preferedTimeEntity", fetch = FetchType.LAZY)
  private List<Trainer> trainers;

  public List<Trainer> getTrainers() {
    return trainers;
  }

  public void setTrainers(List<Trainer> trainers) {
    this.trainers = trainers;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Time getStartTime() {
    return startTime;
  }

  public void setStartTime(Time startTime) {
    this.startTime = startTime;
  }

  public Time getEndTime() {
    return endTime;
  }

  public void setEndTime(Time endTime) {
    this.endTime = endTime;
  }
}
