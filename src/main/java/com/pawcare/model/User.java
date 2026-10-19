package com.pawcare.model;

/** Data only; SQL belongs in the DAO layer. */
public class User {
    private int id;
    private String username;
    private String role;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    @Override public String toString() { return username; }
}
