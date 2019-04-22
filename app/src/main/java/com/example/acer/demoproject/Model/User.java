package com.example.acer.demoproject.Model;

/**
 * Created by Acer on 4/22/2019.
 */

public class User {
    String name, address, username, password;

    public User(String name, String address, String username, String password){
        this.name = name;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password){
        this.name = "";
        this.address = "";
        this.username = username;
        this.password = password;
    }
}
