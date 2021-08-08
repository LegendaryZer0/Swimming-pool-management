package ru.softwave.pool.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Time;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "team")
public class Group {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column private Time startTime;

  @Column private Time endTime;

  @Column(nullable = false, unique = true)
  private String name;

  @Fetch(FetchMode.SUBSELECT)
  @JsonIgnore
  @ToString.Exclude
  @ManyToMany(mappedBy = "groups", cascade = CascadeType.ALL)
  private Set<Visitor> visitors;

  @Column(nullable = false, name = "GROUP_SIZE")
  private byte groupCapacity;

  @Column(nullable = false, name = "MALE_CURRENT_COUNT")
  private byte maleCurrentCount = 0;

  @Column(nullable = false, name = "FEMALE_CURRENT_COUNT")
  private byte femaleCurrentCount = 0;

  @Column(nullable = false, name = "MALE_MAX_COUNT")
  private byte maleMaxCount = 0;

  @Column(nullable = false, name = "FEMALE_MAX_COUNT")
  private byte femaleMaxCount = 0;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "trainer_id")
  private Trainer trainer;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Group group = (Group) o;

    return Objects.equals(id, group.id);
  }

  @Override
  public int hashCode() {
    return 478181614;
  }
}
