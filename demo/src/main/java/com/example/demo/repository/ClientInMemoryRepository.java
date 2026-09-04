package com.example.demo.repository;

import com.example.demo.entities.Client;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ClientInMemoryRepository {
    public List<Client> listAll() {
        return List.of();
    }

    public void save(Client client) {
    }

    public boolean delete(Integer clientId) {
        return false;
    }
}
