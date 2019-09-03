package com.t_systems.webstore.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "customProduct")
public class CustomProduct extends AbstractProduct {
    private String name;

    @OneToOne
    private User author;
}

