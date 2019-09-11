package com.t_systems.webstore.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "category")
public class Category extends AbstractEntity{

    @Column(unique = true, nullable = false)
    private String name;

    private String image;
}
