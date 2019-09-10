package com.t_systems.webstore.exception;

public class UserExistsException extends Exception {

    UserExistsException(String message) {
        super(message);
    }

    /**
     * UserExistsException constructor
     * @param username username
     * @param email user email
     */
    public UserExistsException(String username, String email) {
        super("User ("+username+", "+email+") already exists!");
    }
}
