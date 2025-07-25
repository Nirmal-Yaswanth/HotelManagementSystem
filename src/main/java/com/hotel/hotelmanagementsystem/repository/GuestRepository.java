package com.hotel.hotelmanagementsystem.repository;

import com.hotel.hotelmanagementsystem.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marks this interface as a Spring Data JPA repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    // JpaRepository provides standard CRUD operations (save, findById, findAll, delete, etc.)

    // Custom method to find a Guest by their email, useful for checking uniqueness
    Optional<Guest> findByEmail(String email);

    // Custom method to find guests by phone number
    Optional<Guest> findByPhoneNumber(String phoneNumber);
}
