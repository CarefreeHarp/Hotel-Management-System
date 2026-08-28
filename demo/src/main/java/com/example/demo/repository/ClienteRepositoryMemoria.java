package com.example.demo.repository;

import com.example.demo.entitys.Cliente;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: simula la tabla CLIENTE de la base de datos con un mapa
 * en memoria, donde la llave es el id del cliente (la PK) y el valor es el cliente.
 *
 * Aquí solo se guarda y se consulta. Las búsquedas por otros campos y las
 * validaciones del negocio viven en la capa de servicio.
 *
 * Se usa LinkedHashMap para que los clientes se listen siempre en el mismo orden
 * en que se registraron.
 */
@Repository
public class ClienteRepositoryMemoria {

    private final Map<Integer, Cliente> clientes = new LinkedHashMap<>();

    /**
     * Último id entregado. Simula el AUTO_INCREMENT de la base de datos:
     * siempre crece, nunca reutiliza ids de registros eliminados.
     */
    private int ultimoId = 0;

    public ClienteRepositoryMemoria() {
        cargarDatosDePrueba();
    }

    /** Clientes de prueba para poder probar la página sin registrarse primero. */
    private void cargarDatosDePrueba() {
        guardar(new Cliente(0, "Ana", "Gómez", "1012345678", "3001234567", "ana.gomez@correo.com", "ana12345", ""));
        guardar(new Cliente(0, "Carlos", "Pérez", "1023456789", "3109876543", "carlos.perez@correo.com", "carlos12345", ""));
        guardar(new Cliente(0, "Laura", "Martínez", "1034567890", "3204567890", "laura.martinez@correo.com", "laura12345", ""));
    }

    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes.values());
    }

    /** Devuelve el cliente con ese id, o null si no existe. */
    public Cliente buscarPorId(int idCliente) {
        return clientes.get(idCliente);
    }

    /**
     * Guarda el cliente. Si llega sin id (en 0) es un registro nuevo y el id se
     * genera automáticamente; si ya trae id, se sobrescribe el registro existente.
     */
    public Cliente guardar(Cliente cliente) {
        if (cliente.getIdCliente() == 0) {
            ultimoId++;
            cliente.setIdCliente(ultimoId);
        }

        clientes.put(cliente.getIdCliente(), cliente);
        return cliente;
    }

    /** Devuelve true si el cliente existía y se eliminó. */
    public boolean eliminar(int idCliente) {
        return clientes.remove(idCliente) != null;
    }
}
