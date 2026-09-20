package com.example.demo.service.interfaces;

import com.example.demo.entities.Operator;
import com.example.demo.errors.InvalidOperatorDataException;
import com.example.demo.errors.ResourceNotFoundException;
import java.util.List;

/**
 * CAPA DE SERVICIO: lógica de negocio de los operarios.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Expone la consulta de operarios y la edición del perfil.
 *
 * Un operario siempre está a cargo de un administrador, así que la edición
 * conserva el adminId del registro existente: el servicio busca ese
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
    List<Operator> listByAdministrator(Integer adminId);
    Operator findManagedBy(Integer operatorId, Integer adminId);
    void create(Operator operator, Integer adminId);
    void updateManagedBy(Integer operatorId, Operator operator, Integer adminId);
    void deleteManagedBy(Integer operatorId, Integer adminId);

    void update(Integer operatorId, Operator operator, Integer adminId);
}
