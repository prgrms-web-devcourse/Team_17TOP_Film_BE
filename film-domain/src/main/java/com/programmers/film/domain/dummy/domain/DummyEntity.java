package com.programmers.film.domain.dummy.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DummyEntity {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;
}
