package com.example.demo.model;

public class UserRequestDTO {

//  private Long id;
    private String name;
    private String email;

    public UserRequestDTO() {} // default constructor

        public UserRequestDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
