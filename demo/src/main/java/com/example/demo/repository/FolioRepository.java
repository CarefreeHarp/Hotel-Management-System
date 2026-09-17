package com.example.demo.repository;

import com.example.demo.entities.Folio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Acceso a los folios almacenados en la base de datos. */
@Repository
public interface FolioRepository extends JpaRepository<Folio, Integer> {
}
