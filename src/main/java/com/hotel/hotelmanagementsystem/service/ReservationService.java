package com.hotel.hotelmanagementsystem.service;

import com.hotel.hotelmanagementsystem.exception.DuplicateResourceException;
import com.hotel.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotel.hotelmanagementsystem.model.Guest;
import com.hotel.hotelmanagementsystem.model.Reservation;
import com.hotel.hotelmanagementsystem.model.Room;
import com.hotel.hotelmanagementsystem.repository.GuestRepository;
import com.hotel.hotelmanagementsystem.repository.ReservationRepository;
import com.hotel.hotelmanagementsystem.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service // Marks this class as a Spring Service component
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private GuestRepository guestRepository; // To fetch Guest by ID
    @Autowired
    private RoomRepository roomRepository; // To fetch Room by ID and manage availability

    // --- Create Reservation ---
    @Transactional
    public Reservation createReservation(Long guestId, Long roomId, Reservation reservationDetails) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", guestId));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));

        // Basic validation: Check if room is available and can accommodate guests
        if (!room.isAvailable()) {
            throw new IllegalArgumentException("Room " + room.getRoomNumber() + " is not available.");
        }
        if (reservationDetails.getNumberOfGuests() > room.getCapacity()) {
            throw new IllegalArgumentException("Room " + room.getRoomNumber() + " cannot accommodate " + reservationDetails.getNumberOfGuests() + " guests.");
        }

        // Check for date conflicts for the selected room
        List<Reservation> conflictingReservations = reservationRepository.findByRoomAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                room, reservationDetails.getCheckOutDate(), reservationDetails.getCheckInDate());

        if (!conflictingReservations.isEmpty()) {
            throw new DuplicateResourceException("Reservation", "room " + room.getRoomNumber() + " for dates",
                    reservationDetails.getCheckInDate() + " to " + reservationDetails.getCheckOutDate());
        }

        // Calculate total price
        long nights = ChronoUnit.DAYS.between(reservationDetails.getCheckInDate(), reservationDetails.getCheckOutDate());
        if (nights <= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        double totalPrice = nights * room.getPricePerNight();
        reservationDetails.setTotalPrice(totalPrice);

        reservationDetails.setGuest(guest);
        reservationDetails.setRoom(room);
        reservationDetails.setStatus(Reservation.ReservationStatus.CONFIRMED); // Default status

        return reservationRepository.save(reservationDetails);
    }

    // --- Get All Reservations ---
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // --- Get Reservation by ID ---
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
    }

    // --- Get Reservations by Guest Email ---
    public List<Reservation> getReservationsByGuestEmail(String guestEmail) {
        return reservationRepository.findByGuestEmail(guestEmail);
    }

    // --- Get Reservations by Room Number (can be added if needed, requires custom query or service logic) ---
    // public List<Reservation> getReservationsByRoomNumber(String roomNumber) { ... }

    // --- Update Reservation Status ---
    @Transactional
    public Reservation updateReservationStatus(Long id, Reservation.ReservationStatus newStatus) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        reservation.setStatus(newStatus);
        return reservationRepository.save(reservation);
    }

    // --- Update Full Reservation Details (e.g., changing dates, room, guest) ---
    @Transactional
    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        // Fetch updated guest and room if IDs are provided
        Guest newGuest = guestRepository.findById(reservationDetails.getGuest().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", reservationDetails.getGuest().getId()));
        Room newRoom = roomRepository.findById(reservationDetails.getRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", reservationDetails.getRoom().getId()));

        // Perform similar validation as createReservation for new dates/room
        // This is simplified and might need more robust conflict checking
        if (!newRoom.isAvailable() && !newRoom.getId().equals(existingReservation.getRoom().getId())) {
             throw new IllegalArgumentException("New room " + newRoom.getRoomNumber() + " is not available.");
        }
        if (reservationDetails.getNumberOfGuests() > newRoom.getCapacity()) {
            throw new IllegalArgumentException("New room " + newRoom.getRoomNumber() + " cannot accommodate " + reservationDetails.getNumberOfGuests() + " guests.");
        }
        long nights = ChronoUnit.DAYS.between(reservationDetails.getCheckInDate(), reservationDetails.getCheckOutDate());
        if (nights <= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        double totalPrice = nights * newRoom.getPricePerNight();


        existingReservation.setGuest(newGuest);
        existingReservation.setRoom(newRoom);
        existingReservation.setCheckInDate(reservationDetails.getCheckInDate());
        existingReservation.setCheckOutDate(reservationDetails.getCheckOutDate());
        existingReservation.setNumberOfGuests(reservationDetails.getNumberOfGuests());
        existingReservation.setTotalPrice(totalPrice);
        existingReservation.setStatus(reservationDetails.getStatus()); // Allow status update

        return reservationRepository.save(existingReservation);
    }


    // --- Delete Reservation ---
    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", "id", id);
        }
        reservationRepository.deleteById(id);
    }
}
