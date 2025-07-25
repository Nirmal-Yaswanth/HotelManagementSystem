package com.hotel.hotelmanagementsystem.controller;

import com.hotel.hotelmanagementsystem.model.Reservation;
import com.hotel.hotelmanagementsystem.service.ReservationService;
import jakarta.validation.Valid; // For validating request body
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST Controller
@RequestMapping("/api/reservations") // Base path for all endpoints in this controller
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}) // Allow requests from your frontend
public class ReservationController {

    @Autowired // Injects ReservationService dependency
    private ReservationService reservationService;

    // Endpoint to create a new reservation
    // POST http://localhost:8080/api/reservations?guestId=1&roomId=1
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestParam Long guestId,
            @RequestParam Long roomId,
            @Valid @RequestBody Reservation reservation) {
        Reservation createdReservation = reservationService.createReservation(guestId, roomId, reservation);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED); // Returns 201 Created
    }

    // Endpoint to get all reservations
    // GET http://localhost:8080/api/reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // Endpoint to get a reservation by ID
    // GET http://localhost:8080/api/reservations/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    // Endpoint to get reservations by guest email
    // GET http://localhost:8080/api/reservations/by-guest-email?guestEmail=guest@example.com
    @GetMapping("/by-guest-email")
    public ResponseEntity<List<Reservation>> getReservationsByGuestEmail(@RequestParam String guestEmail) {
        List<Reservation> reservations = reservationService.getReservationsByGuestEmail(guestEmail);
        return ResponseEntity.ok(reservations);
    }

    // Endpoint to update reservation status
    // PUT http://localhost:8080/api/reservations/{id}/status?newStatus=CHECKED_IN
    @PutMapping("/{id}/status")
    public ResponseEntity<Reservation> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam Reservation.ReservationStatus newStatus) {
        Reservation updatedReservation = reservationService.updateReservationStatus(id, newStatus);
        return ResponseEntity.ok(updatedReservation);
    }

    // Endpoint to update full reservation details
    // PUT http://localhost:8080/api/reservations/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody Reservation reservationDetails) {
        Reservation updatedReservation = reservationService.updateReservation(id, reservationDetails);
        return ResponseEntity.ok(updatedReservation);
    }

    // Endpoint to delete a reservation by ID
    // DELETE http://localhost:8080/api/reservations/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Returns 204 No Content
    }
}
