package com.hotel.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Marks this class as a JPA entity
@Table(name = "rooms") // Maps this entity to the "rooms" table in the database
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
public class Room {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures auto-incrementing ID
    private Long id;

    @Column(nullable = false, unique = true) // Room number should be unique
    private String roomNumber;

    @Column(nullable = false)
    private String roomType; // e.g., "Single", "Double", "Suite", "Deluxe"

    @Column(nullable = false)
    private Double pricePerNight; // Price for one night

    @Column(nullable = false)
    private Integer capacity; // Max number of guests

    @Column(nullable = false)
    private boolean isAvailable; // True if room is currently available for booking

    // You can add more fields like description, amenities, etc.
}
