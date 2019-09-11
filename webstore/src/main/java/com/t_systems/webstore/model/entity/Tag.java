package com.t_systems.webstore.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tag")
public class Tag extends AbstractEntity{
    private String name;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Category> categories;
}
