package com.example.demo.repository;

import com.example.demo.entities.FolioItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Acceso a los cargos individuales registrados en cada folio. */
@Repository
public interface FolioItemRepository extends JpaRepository<FolioItem, Integer> {

    /* Lista los ítems asociados al folio indicado, ordenados por fecha de cargo. */
    List<FolioItem> findByFolioFolioIdOrderByChargedAtAsc(Integer folioId);
}
