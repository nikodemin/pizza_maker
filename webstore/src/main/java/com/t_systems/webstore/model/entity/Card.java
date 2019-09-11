package com.t_systems.webstore.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@Table(name="card")
public class Card{
    private String cardNumber;
    private String month;
    private String year;
    private String cvv;
}
