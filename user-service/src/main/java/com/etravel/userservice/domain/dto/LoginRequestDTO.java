package com.etravel.userservice.domain.dto;

public class LoginRequestDTO {
    private String username;
    private String password;
    public String getUsername() { return username; }
    public void setUsername(String email) { this.username = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "LoginRequestDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}