package ru.softwave.pool.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class Visitor extends User {

  @Getter
  @Setter
  @Fetch(FetchMode.SUBSELECT)
  @JoinTable(
      name = "VISITOR_GROUP_LINK",
      joinColumns = @JoinColumn(name = "VISITOR_ID"),
      inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
  @ManyToMany(cascade = CascadeType.ALL)
  private Set<Group> groups;

  private boolean sex;

  public boolean getSex() {
    return sex;
  }

  public void setSex(boolean sex) {
    this.sex = sex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Visitor visitor = (Visitor) o;

    return Objects.equals(getId(), visitor.getId());
  }

  @Override
  public int hashCode() {
    return 1119687905;
  }
}
