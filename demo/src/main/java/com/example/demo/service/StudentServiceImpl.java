package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entitys.Servicio;
import com.example.demo.repository.ServicioRepositoryMemoria;

@Service
public class StudentServiceImpl implements ServicioService {

    private final ServicioRepositoryMemoria repository;

    public StudentServiceImpl(ServicioRepositoryMemoria repository) {
        this.repository = repository;
    }

    @Override
    public Servicio getServiceByNombreUrl(String nombreUrl) {
        return repository.obtenerPorNombreUrl(nombreUrl);
    }

}
