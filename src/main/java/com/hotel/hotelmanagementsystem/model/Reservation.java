package com.hotel.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; // For check-in and check-out dates

@Entity // Marks this class as a JPA entity
@Table(name = "reservations") // Maps this entity to the "reservations" table in the database
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
public class Reservation {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures auto-incrementing ID
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // Many reservations can belong to one guest
    @JoinColumn(name = "guest_id", nullable = false) // Foreign key column in reservations table
    private Guest guest;

    @ManyToOne(fetch = FetchType.EAGER) // Many reservations can be for one room
    @JoinColumn(name = "room_id", nullable = false) // Foreign key column in reservations table
    private Room room;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private Integer numberOfGuests;

    @Column(nullable = false)
    private Double totalPrice; // Calculated total price for the reservation

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Store enum as String in DB
    private ReservationStatus status; // e.g., PENDING, CONFIRMED, CANCELLED, CHECKED_IN, CHECKED_OUT

    // Enum for Reservation Status
    public enum ReservationStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        CHECKED_IN,
        CHECKED_OUT
    }
}
