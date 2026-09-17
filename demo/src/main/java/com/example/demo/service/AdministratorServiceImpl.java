package com.example.demo.service;

import com.example.demo.entities.Administrator;
import com.example.demo.errors.InvalidAdministratorDataException;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.service.interfaces.AdministratorService;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los administradores.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 *
 * Los datos viven en la base de datos H2 a través de AdministratorRepository.
 * Todas las validaciones y todos los mensajes de error viven aquí, no en el
 * controlador ni en la vista.
 */
@Service
public class AdministratorServiceImpl implements AdministratorService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Autowired
    public AdministratorRepository administratorRepository;

    @Override
    public List<Administrator> listAdministrators() {
        return administratorRepository.findAll();
    }

    @Override
    public Administrator findById(Integer adminId) {
        return administratorRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No administrator is registered with the identifier " + adminId + "."));
    }

    @Override
    public void update(Integer adminId, Administrator administrator) {
        Administrator registeredAdministrator = findById(adminId);

        // Se conserva el id para que save() actualice la fila que ya existe en vez
        // de insertar una nueva, y la contraseña porque el formulario no la edita.
        administrator.setAdminId(registeredAdministrator.getAdminId());
        administrator.setPassword(registeredAdministrator.getPassword());

        validateData(administrator);
        administratorRepository.save(administrator);
    }

    /**
     * Las reglas del formulario se comprueban en el servidor para que no dependan
     * de las restricciones del navegador. El correo es único; al editar se
     * comparan los ids para no contar el propio registro como duplicado.
     *
     * Los ids se comparan con Objects.equals y no con != porque son Integer, es
     * decir objetos y no valores primitivos.
     *
     * @throws InvalidAdministratorDataException con el mensaje del primer dato inválido.
     */
    private void validateData(Administrator administrator) {
        if (administrator.getName() == null || administrator.getName().isBlank()) {
            throw new InvalidAdministratorDataException(
                    InvalidAdministratorDataException.Reason.NAME_REQUIRED, administrator.getName());
        }

        if (administrator.getName().length() > 50) {
            throw new InvalidAdministratorDataException(
                    InvalidAdministratorDataException.Reason.NAME_TOO_LONG, administrator.getName());
        }

        if (administrator.getEmail() == null || administrator.getEmail().length() > 80
                || !EMAIL_PATTERN.matcher(administrator.getEmail()).matches()) {
            throw new InvalidAdministratorDataException(
                    InvalidAdministratorDataException.Reason.EMAIL_INVALID, administrator.getEmail());
        }

        Administrator administratorWithThatEmail =
                administratorRepository.findByEmailIgnoreCase(administrator.getEmail()).orElse(null);
        if (administratorWithThatEmail != null
                && !Objects.equals(administratorWithThatEmail.getAdminId(), administrator.getAdminId())) {
            throw new InvalidAdministratorDataException(
                    InvalidAdministratorDataException.Reason.EMAIL_ALREADY_REGISTERED, administrator.getEmail());
        }
    }
}
