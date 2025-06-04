package com.Gallery.dto;

public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;

    public void validateEmail() {
        if (email.isEmpty()){
            throw new IllegalArgumentException("Email is empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }


    public void validatePassword() {

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        if(!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+,.\\-=]).{8,}$")){
            throw new IllegalArgumentException("Password must contain at least a letter, a number, and a special character");
        }

    }

    public void validateUsername() {
        if(username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if(username.length()<2){
            throw new IllegalArgumentException("Username must be at least 2 characters long.");
        }
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
