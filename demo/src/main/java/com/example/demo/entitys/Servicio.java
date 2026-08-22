package com.example.demo.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Servicio {

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
    private boolean activo;

}
