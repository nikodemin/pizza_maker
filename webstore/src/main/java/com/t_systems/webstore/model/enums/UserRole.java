package com.t_systems.webstore.model.enums;

public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String text;

    UserRole(String str) {
        text = str;
    }

    @Override
    public String toString() {
        return text;
    }
}
