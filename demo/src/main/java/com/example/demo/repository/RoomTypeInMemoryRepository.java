package com.example.demo.repository;

import com.example.demo.entities.RoomType;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RoomTypeInMemoryRepository {
    public List<RoomType> listAll() {
        return List.of();
    }

    public RoomType findById(int roomTypeId) {
        return null;
    }

    public void save(RoomType roomType) {
    }

    public boolean delete(int roomTypeId) {
        return false;
    }
}
