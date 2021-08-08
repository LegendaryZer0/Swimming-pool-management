package ru.softwave.pool.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@Getter
@Setter
public class Trainer extends User {

  private byte minWorkWeekHours;
  private byte currentWorkWeekHours;
  private byte maxWorkWeekHours;
  private String name;
  private String surname;

  @Fetch(FetchMode.JOIN)
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private PreferedTimeEntity preferedTimeEntity;

  @JsonIgnore
  @ToString.Exclude
  @Fetch(FetchMode.SUBSELECT)
  @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Group> groups;
}
