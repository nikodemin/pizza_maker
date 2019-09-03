package com.t_systems.webstore.model.enums;

public enum DeliveryMethod {
    COURIER("Courier"),
    PICKUP("Pick up");

    private String text;

    DeliveryMethod(String str) {
        text = str;
    }

    @Override
    public String toString() {
        return text;
    }
}
