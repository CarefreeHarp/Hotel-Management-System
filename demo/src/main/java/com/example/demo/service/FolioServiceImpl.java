package com.example.demo.service;

import com.example.demo.entities.Folio;
import com.example.demo.entities.FolioItem;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.repository.FolioItemRepository;
import com.example.demo.repository.FolioRepository;
import com.example.demo.service.interfaces.FolioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Implementa las consultas de lectura para los folios y sus ítems. */
@Service
public class FolioServiceImpl implements FolioService {

    @Autowired
    private FolioRepository folioRepository;

    @Autowired
    private FolioItemRepository folioItemRepository;

    /** Busca el folio solicitado y reporta un error si no existe. */
    @Override
    public Folio findById(Integer folioId) {
        return folioRepository.findById(folioId)
                .orElseThrow(() -> new ResourceNotFoundException("The folio does not exist."));
    }

    /** Busca el folio único que se generó a partir de una reserva. */
    @Override
    public Folio findByReservationId(Integer reservationId) {
        return folioRepository.findByReservationReservationId(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("The reservation folio does not exist."));
    }

    /** Verifica el folio y devuelve sus cargos ordenados cronológicamente. */
    @Override
    public List<FolioItem> listItemsByFolioId(Integer folioId) {
        findById(folioId);
        return folioItemRepository.findByFolioFolioIdOrderByChargedAtAsc(folioId);
    }
}
