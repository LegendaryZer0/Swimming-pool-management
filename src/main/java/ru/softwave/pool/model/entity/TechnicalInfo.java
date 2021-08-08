package ru.softwave.pool.model.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TechnicalInfo {

  @Id
  @Column(name = "user_id")
  private Long id;

  @Fetch(FetchMode.JOIN)
  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  private UUID uuidToConfirm;
}
