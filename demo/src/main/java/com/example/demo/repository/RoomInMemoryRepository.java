package com.example.demo.repository;

import com.example.demo.entities.Room;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RoomInMemoryRepository {
    public List<Room> listAll() {
        return List.of();
    }

    public Room findByNumber(int number) {
        return null;
    }

    public void save(Room room) {
    }

    public boolean delete(int number) {
        return false;
    }
}
