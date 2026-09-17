package com.example.demo.service.interfaces;

import com.example.demo.entities.Operator;
import com.example.demo.errors.InvalidOperatorDataException;
import com.example.demo.errors.ResourceNotFoundException;
import java.util.List;

/**
 * CAPA DE SERVICIO: lógica de negocio de los operarios.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Igual que el módulo de administradores, por ahora solo expone READ y UPDATE.
 *
 * Un operario siempre está a cargo de un administrador, así que la edición
 * recibe aparte el adminId elegido en el formulario: el servicio busca ese
 * administrador y lo asocia, y avisa si no se seleccionó ninguno.
 *
 * MANEJO DE ERRORES:
 *
 * - ResourceNotFoundException     -> el operario (o el administrador) no existe.
 * - InvalidOperatorDataException  -> los datos del formulario no son válidos.
 */
public interface OperatorService {

    /** Lista todos los operarios registrados. */
    List<Operator> listOperators();

    /**
     * Devuelve el operario con ese identificador autogenerado.
     *
     * @throws ResourceNotFoundException si no existe un operario con ese id.
     */
    Operator findById(Integer operatorId);

    /**
     * Actualiza los datos de un operario registrado y el administrador que lo
     * tiene a cargo. operatorId identifica la cuenta que se está editando,
     * incluso si el operario cambia su correo.
     *
     * @throws ResourceNotFoundException    si no existe el operario o el administrador.
     * @throws InvalidOperatorDataException si los datos nuevos no son válidos.
     */
    void update(Integer operatorId, Operator operator, Integer adminId);
}
