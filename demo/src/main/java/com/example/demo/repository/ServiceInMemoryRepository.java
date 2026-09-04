package com.example.demo.repository;

import com.example.demo.entities.Service;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceInMemoryRepository {
    public List<Service> listAll() {
        return List.of();
    }

    public Service findByUrlName(String urlName) {
        return null;
    }
}
