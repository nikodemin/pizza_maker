package com.t_systems.webstore.model.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Data
@Table(name="card")
public class Card{
    private String cardNumber;
    private String month;
    private String year;
    private String cvv;
}
