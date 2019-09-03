package com.t_systems.webstore.exception;

public class UserExistsException extends Exception {

    UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String username, String email) {
        super("User ("+username+", "+email+") already exists!");
    }
}
