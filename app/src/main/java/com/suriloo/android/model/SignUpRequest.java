package com.suriloo.android.model;

public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private int age;

    public SignUpRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = 0;
    }
}