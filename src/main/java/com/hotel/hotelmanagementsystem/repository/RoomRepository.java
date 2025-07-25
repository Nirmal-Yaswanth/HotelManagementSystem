package com.hotel.hotelmanagementsystem.repository;

import com.hotel.hotelmanagementsystem.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate; // Needed for availability checks

@Repository // Marks this interface as a Spring Data JPA repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // JpaRepository provides standard CRUD operations

    // Custom method to find a Room by its room number
    Optional<Room> findByRoomNumber(String roomNumber);

    // Custom method to find available rooms by type and capacity (simplified for now)
    List<Room> findByIsAvailableTrueAndRoomTypeAndCapacityGreaterThanEqual(String roomType, Integer capacity);

    // More complex availability checks would typically involve custom queries
    // or a dedicated service method that queries reservations.
    // For now, we'll rely on the 'isAvailable' flag and simple filters.
}
