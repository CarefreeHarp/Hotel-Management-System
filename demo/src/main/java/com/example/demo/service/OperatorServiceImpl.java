package com.example.demo.service;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Operator;
import com.example.demo.errors.InvalidOperatorDataException;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.repository.OperatorRepository;
import com.example.demo.service.interfaces.AdministratorService;
import com.example.demo.service.interfaces.OperatorService;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los operarios.
 *
 * Además del repositorio se inyecta AdministratorService: para asociar un
 * operario a su administrador se reutiliza la búsqueda que ya existe en ese
 * servicio en lugar de repetirla aquí. Un servicio puede apoyarse en otro
 * servicio, pero nunca en el repositorio de otro módulo.
 */
@Service
public class OperatorServiceImpl implements OperatorService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Autowired
    public OperatorRepository operatorRepository;

    @Autowired
    public AdministratorService administratorService;

    @Override
    public List<Operator> listOperators() {
        return operatorRepository.findAll();
    }

    @Override
    public Operator findById(Integer operatorId) {
        return operatorRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No operator is registered with the identifier " + operatorId + "."));
    }

    @Override
    public void update(Integer operatorId, Operator operator, Integer adminId) {
        Operator registeredOperator = findById(operatorId);

        // Si el formulario no trajo administrador se avisa antes de ir a la base
        // de datos, porque la columna admin_id de la tabla OPERATOR es NOT NULL.
        if (adminId == null) {
            throw new InvalidOperatorDataException(
                    InvalidOperatorDataException.Reason.ADMIN_REQUIRED, null);
        }

        // Si el administrador elegido no existe, findById lanza ResourceNotFoundException.
        Administrator adminInCharge = administratorService.findById(adminId);

        // Se conserva el id para que save() actualice la fila que ya existe en vez
        // de insertar una nueva, y la contraseña porque el formulario no la edita.
        operator.setOperatorId(registeredOperator.getOperatorId());
        operator.setPassword(registeredOperator.getPassword());
        operator.setAdmin(adminInCharge);

        validateData(operator);
        operatorRepository.save(operator);
    }

    /**
     * Las reglas del formulario se comprueban en el servidor para que no dependan
     * de las restricciones del navegador. El correo es único; al editar se
     * comparan los ids para no contar el propio registro como duplicado.
     *
     * @throws InvalidOperatorDataException con el mensaje del primer dato inválido.
     */
    private void validateData(Operator operator) {
        if (operator.getName() == null || operator.getName().isBlank()) {
            throw new InvalidOperatorDataException(
                    InvalidOperatorDataException.Reason.NAME_REQUIRED, operator.getName());
        }

        if (operator.getName().length() > 50) {
            throw new InvalidOperatorDataException(
                    InvalidOperatorDataException.Reason.NAME_TOO_LONG, operator.getName());
        }

        if (operator.getLastName() == null || operator.getLastName().isBlank()) {
            throw new InvalidOperatorDataException(
                    InvalidOperatorDataException.Reason.LAST_NAME_REQUIRED, operator.getLastName());
        }

        if (operator.getLastName().length() > 50) {
            throw new InvalidOperatorDataException(
                    InvalidOperatorDataException.Reason.LAST_NAME_TOO_LONG, operator.getLastName());
        }

        if (operator.getEmail() == null || operator.getEmail().length() > 80
                || !EMAIL_PATTERN.matcher(operator.getEmail()).matches()) {
            throw new InvalidOperatorDataException(
                    InvalidOperatorDataException.Reason.EMAIL_INVALID, operator.getEmail());
        }

        Operator operatorWithThatEmail =
                operatorRepository.findByEmailIgnoreCase(operator.getEmail()).orElse(null);
        if (operatorWithThatEmail != null
                && !Objects.equals(operatorWithThatEmail.getOperatorId(), operator.getOperatorId())) {
            throw new InvalidOperatorDataException(
                    InvalidOperatorDataException.Reason.EMAIL_ALREADY_REGISTERED, operator.getEmail());
        }
    }
}
