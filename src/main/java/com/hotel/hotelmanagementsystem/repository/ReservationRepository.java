package com.hotel.hotelmanagementsystem.repository;

import com.hotel.hotelmanagementsystem.model.Reservation;
import com.hotel.hotelmanagementsystem.model.Guest;
import com.hotel.hotelmanagementsystem.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository // Marks this interface as a Spring Data JPA repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // JpaRepository provides standard CRUD operations

    // Custom methods to find reservations by guest or room
    List<Reservation> findByGuest(Guest guest);
    List<Reservation> findByRoom(Room room);

    // Custom method to find reservations within a date range for a specific room
    List<Reservation> findByRoomAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            Room room, LocalDate checkOutDate, LocalDate checkInDate);

    // Custom method to find reservations by guest email (useful for guest dashboards)
    List<Reservation> findByGuestEmail(String guestEmail);

    // Custom method to find reservations by status
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
}
