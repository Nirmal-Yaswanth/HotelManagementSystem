package com.hotel.hotelmanagementsystem.service;

import com.hotel.hotelmanagementsystem.exception.DuplicateResourceException;
import com.hotel.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotel.hotelmanagementsystem.model.Guest;
import com.hotel.hotelmanagementsystem.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Marks this class as a Spring Service component
public class GuestService {

    @Autowired // Injects GuestRepository dependency
    private GuestRepository guestRepository;

    // --- Create Guest ---
    @Transactional // Ensures the entire method runs as a single transaction
    public Guest createGuest(Guest guest) {
        // Check if guest with given email already exists
        if (guestRepository.findByEmail(guest.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Guest", "email", guest.getEmail());
        }
        // Check if guest with given phone number already exists
        if (guestRepository.findByPhoneNumber(guest.getPhoneNumber()).isPresent()) {
            throw new DuplicateResourceException("Guest", "phone number", guest.getPhoneNumber());
        }
        return guestRepository.save(guest); // Save the new guest
    }

    // --- Get All Guests ---
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    // --- Get Guest by ID ---
    public Guest getGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));
    }

    // --- Get Guest by Email ---
    public Guest getGuestByEmail(String email) {
        return guestRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "email", email));
    }

    // --- Update Guest ---
    @Transactional
    public Guest updateGuest(Long id, Guest guestDetails) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));

        // Check for duplicate email for another guest
        if (guestRepository.findByEmail(guestDetails.getEmail()).isPresent() &&
            !guestRepository.findByEmail(guestDetails.getEmail()).get().getId().equals(id)) {
            throw new DuplicateResourceException("Guest", "email", guestDetails.getEmail());
        }
        // Check for duplicate phone number for another guest
        if (guestRepository.findByPhoneNumber(guestDetails.getPhoneNumber()).isPresent() &&
            !guestRepository.findByPhoneNumber(guestDetails.getPhoneNumber()).get().getId().equals(id)) {
            throw new DuplicateResourceException("Guest", "phone number", guestDetails.getPhoneNumber());
        }

        guest.setFirstName(guestDetails.getFirstName());
        guest.setLastName(guestDetails.getLastName());
        guest.setEmail(guestDetails.getEmail());
        guest.setPhoneNumber(guestDetails.getPhoneNumber());
        guest.setAddress(guestDetails.getAddress());

        return guestRepository.save(guest);
    }

    // --- Delete Guest ---
    @Transactional
    public void deleteGuest(Long id) {
        if (!guestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Guest", "id", id);
        }
        guestRepository.deleteById(id);
    }
}
