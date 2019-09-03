package com.t_systems.webstore.model.entity;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ingredient")
@Data
public class Ingredient extends AbstractEntity{
    @Column(unique = true, nullable = false)
    private String name;

    private Integer price;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Category> categories;
}
