package com.example.demo.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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
    private String resumen;
    private String duracion;
    private String disponibilidad;
    private String ubicacion;
    private String imagenPrincipalUrl;
    private List<String> imagenesGaleriaUrls;
    private List<String> beneficios;

}
