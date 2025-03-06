package com.example.demo;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String login;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "crypted_password", nullable = false, length = 40)
    private String password;

    // Other fields...

    // Getters and setters...
}
