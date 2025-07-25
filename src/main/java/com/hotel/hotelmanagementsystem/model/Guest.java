package com.hotel.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Marks this class as a JPA entity
@Table(name = "guests") // Maps this entity to the "guests" table in the database
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
public class Guest {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures auto-incrementing ID
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true) // Email should be unique for each guest
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = true) // Address can be optional
    private String address;

    // Note: For simplicity, we are not adding password/login here.
    // If guests need to log in, this entity would need authentication fields.
}
