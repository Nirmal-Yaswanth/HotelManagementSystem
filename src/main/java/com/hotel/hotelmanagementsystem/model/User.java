package com.hotel.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor; // Corrected import
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor // Corrected annotation
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // In a real app, this should be hashed (e.g., using BCrypt)

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role; // e.g., "USER", "ADMIN"
}
