package com.example.thirdandroidapp;

public class User {
    private String id, Name, Qmail, Password;

    public User(String id, String name, String qmail, String password) {
        this.id = id;
        Name = name;
        Qmail = qmail;
        Password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getQmail() {
        return Qmail;
    }

    public String getPassword() {
        return Password;
    }
}
