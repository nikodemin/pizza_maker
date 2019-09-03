package com.t_systems.webstore.model.enums;

public enum OrderStatus {
    UNPAID("Unpaid"),
    PAID("Paid"),
    DELIVERED("Delivered");
    private String text;

    OrderStatus(String str) {
        this.text = str;
    }

    @Override
    public String toString() {
        return text;
    }
}
