package com.t_systems.webstore.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
public class Address {
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
