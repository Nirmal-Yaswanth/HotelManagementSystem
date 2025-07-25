package com.hotel.hotelmanagementsystem.controller;

import com.hotel.hotelmanagementsystem.model.Guest;
import com.hotel.hotelmanagementsystem.service.GuestService;
import jakarta.validation.Valid; // For validating request body
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST Controller
@RequestMapping("/api/guests") // Base path for all endpoints in this controller
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}) // Allow requests from your frontend
public class GuestController {

    @Autowired // Injects GuestService dependency
    private GuestService guestService;

    // Endpoint to create a new guest
    // POST http://localhost:8080/api/guests
    @PostMapping
    public ResponseEntity<Guest> createGuest(@Valid @RequestBody Guest guest) {
        // @Valid triggers validation annotations on the Guest object
        Guest createdGuest = guestService.createGuest(guest);
        return new ResponseEntity<>(createdGuest, HttpStatus.CREATED); // Returns 201 Created status
    }

    // Endpoint to get all guests
    // GET http://localhost:8080/api/guests
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        List<Guest> guests = guestService.getAllGuests();
        return ResponseEntity.ok(guests);
    }

    // Endpoint to get a guest by ID
    // GET http://localhost:8080/api/guests/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable Long id) {
        Guest guest = guestService.getGuestById(id);
        return ResponseEntity.ok(guest);
    }

    // Endpoint to get a guest by email
    // GET http://localhost:8080/api/guests/by-email?email=test@example.com
    @GetMapping("/by-email")
    public ResponseEntity<Guest> getGuestByEmail(@RequestParam String email) {
        Guest guest = guestService.getGuestByEmail(email);
        return ResponseEntity.ok(guest);
    }

    // Endpoint to update a guest by ID
    // PUT http://localhost:8080/api/guests/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable Long id, @Valid @RequestBody Guest guestDetails) {
        Guest updatedGuest = guestService.updateGuest(id, guestDetails);
        return ResponseEntity.ok(updatedGuest);
    }

    // Endpoint to delete a guest by ID
    // DELETE http://localhost:8080/api/guests/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Returns 204 No Content status
    }
}
