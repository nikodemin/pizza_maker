package com.t_systems.webstore.model.enums;

public enum PaymentMethod {
    CASH("Cash"),
    CARD("Card");

    private String text;

    PaymentMethod(String str){
        text = str;
    }

    @Override
    public String toString() {
        return text;
    }
}
