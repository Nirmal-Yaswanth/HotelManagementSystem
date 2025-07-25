package com.hotel.hotelmanagementsystem.service;

import com.hotel.hotelmanagementsystem.exception.DuplicateResourceException;
import com.hotel.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotel.hotelmanagementsystem.model.Room;
import com.hotel.hotelmanagementsystem.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Marks this class as a Spring Service component
public class RoomService {

    @Autowired // Injects RoomRepository dependency
    private RoomRepository roomRepository;

    // --- Create Room ---
    @Transactional
    public Room createRoom(Room room) {
        // Check if room with given room number already exists
        if (roomRepository.findByRoomNumber(room.getRoomNumber()).isPresent()) {
            throw new DuplicateResourceException("Room", "room number", room.getRoomNumber());
        }
        return roomRepository.save(room);
    }

    // --- Get All Rooms ---
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // --- Get Room by ID ---
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
    }

    // --- Get Room by Room Number ---
    public Room getRoomByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "room number", roomNumber));
    }

    // --- Get Available Rooms by Type and Capacity (simplified) ---
    public List<Room> getAvailableRooms(String roomType, Integer capacity) {
        return roomRepository.findByIsAvailableTrueAndRoomTypeAndCapacityGreaterThanEqual(roomType, capacity);
    }

    // --- Update Room ---
    @Transactional
    public Room updateRoom(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));

        // Check for duplicate room number for another room
        if (roomRepository.findByRoomNumber(roomDetails.getRoomNumber()).isPresent() &&
            !roomRepository.findByRoomNumber(roomDetails.getRoomNumber()).get().getId().equals(id)) {
            throw new DuplicateResourceException("Room", "room number", roomDetails.getRoomNumber());
        }

        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setRoomType(roomDetails.getRoomType());
        room.setPricePerNight(roomDetails.getPricePerNight());
        room.setCapacity(roomDetails.getCapacity());
        room.setAvailable(roomDetails.isAvailable());

        return roomRepository.save(room);
    }

    // --- Delete Room ---
    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room", "id", id);
        }
        roomRepository.deleteById(id);
    }
}
