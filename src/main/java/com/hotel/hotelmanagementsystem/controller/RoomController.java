package com.hotel.hotelmanagementsystem.controller;

import com.hotel.hotelmanagementsystem.model.Room;
import com.hotel.hotelmanagementsystem.service.RoomService;
import jakarta.validation.Valid; // For validating request body
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST Controller
@RequestMapping("/api/rooms") // Base path for all endpoints in this controller
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}) // Allow requests from your frontend
public class RoomController {

    @Autowired // Injects RoomService dependency
    private RoomService roomService;

    // Endpoint to create a new room
    // POST http://localhost:8080/api/rooms
    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) {
        Room createdRoom = roomService.createRoom(room);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED); // Returns 201 Created
    }

    // Endpoint to get all rooms
    // GET http://localhost:8080/api/rooms
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    // Endpoint to get a room by ID
    // GET http://localhost:8080/api/rooms/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    // Endpoint to get a room by room number
    // GET http://localhost:8080/api/rooms/by-number?roomNumber=101
    @GetMapping("/by-number")
    public ResponseEntity<Room> getRoomByRoomNumber(@RequestParam String roomNumber) {
        Room room = roomService.getRoomByRoomNumber(roomNumber);
        return ResponseEntity.ok(room);
    }

    // Endpoint to get available rooms by type and capacity
    // GET http://localhost:8080/api/rooms/available?roomType=Deluxe&capacity=2
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(
            @RequestParam String roomType,
            @RequestParam Integer capacity) {
        List<Room> rooms = roomService.getAvailableRooms(roomType, capacity);
        return ResponseEntity.ok(rooms);
    }

    // Endpoint to update a room by ID
    // PUT http://localhost:8080/api/rooms/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @Valid @RequestBody Room roomDetails) {
        Room updatedRoom = roomService.updateRoom(id, roomDetails);
        return ResponseEntity.ok(updatedRoom);
    }

    // Endpoint to delete a room by ID
    // DELETE http://localhost:8080/api/rooms/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Returns 204 No Content
    }
}
