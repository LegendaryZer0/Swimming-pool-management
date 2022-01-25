package ru.softwave.pool.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Entity
public class Admin extends User {
}
