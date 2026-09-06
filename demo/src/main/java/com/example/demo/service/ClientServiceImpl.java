package com.example.demo.service;

import com.example.demo.entities.Client;
import com.example.demo.errors.ClientNotFoundException;
import com.example.demo.errors.InvalidClientDataException;
import com.example.demo.errors.InvalidCurrentPasswordException;
import com.example.demo.repository.ClientRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los clientes.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 *
 * Ahora el repositorio es un ClientRepository de Spring Data JPA, así que los
 * datos viven en la base de datos H2 y no en una lista en memoria. Lo que no
 * cambia es el reparto de responsabilidades: todas las validaciones y todos los
 * mensajes de error siguen viviendo aquí.
 */
@Service
public class ClientServiceImpl implements ClientService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Autowired
    public ClientRepository clientRepository;

    @Override
    public List<Client> listClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ClientNotFoundException(email));
    }

    @Override
    public Client findByEmailForLogin(String email) {
        return clientRepository.findByEmailIgnoreCase(email)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void register(Client client) {
        // El id lo genera la base de datos (IDENTITY). Se envía en null para que
        // Hibernate haga un INSERT: el formulario del registro nunca lo manda.
        client.setClientId(null);

        validateData(client, true);
        clientRepository.save(client);
    }

    @Override
    public void updateProfile(String emailCurrent, Client client, String passwordCurrent) {
        Client profileRegistrada = findByEmail(emailCurrent);

        if (!profileRegistrada.getPassword().equals(passwordCurrent)) {
            throw new InvalidCurrentPasswordException(emailCurrent);
        }

        // Se conserva el id para que save() actualice la fila que ya existe en vez
        // de insertar una nueva, y la contraseña porque el formulario no la edita.
        client.setClientId(profileRegistrada.getClientId());
        client.setPassword(profileRegistrada.getPassword());

        validateData(client, false);
        clientRepository.save(client);
    }

    @Override
    public void deleteProfile(String email) {
        Client profileRegistrada = findByEmail(email);
        clientRepository.deleteById(profileRegistrada.getClientId());
    }

    /**
     * Las reglas del formulario se comprueban en el servidor para que no dependan
     * de las restricciones del navegador. El email y la cédula son únicos; al
     * editar se comparan los ids para no contar el propio perfil como duplicado.
     *
     * Los ids se comparan con Objects.equals y no con != porque son Integer (un
     * objeto), y en un cliente nuevo el id todavía viene en null.
     *
     * @throws InvalidClientDataException con el mensaje del primer dato inválido.
     */
    private void validateData(Client client, boolean isRegistration) {
        if (client.getName() == null || client.getName().isBlank()) {
            throw new InvalidClientDataException(InvalidClientDataException.Reason.NAME_REQUIRED, client.getName());
        }

        if (client.getName().length() > 50) {
            throw new InvalidClientDataException(InvalidClientDataException.Reason.NAME_TOO_LONG, client.getName());
        }

        if (client.getLastName() == null || client.getLastName().isBlank()) {
            throw new InvalidClientDataException(InvalidClientDataException.Reason.LAST_NAME_REQUIRED, client.getLastName());
        }

        if (client.getLastName().length() > 50) {
            throw new InvalidClientDataException(InvalidClientDataException.Reason.LAST_NAME_TOO_LONG, client.getLastName());
        }

        if (client.getNationalId() == null || !client.getNationalId().matches("\\d{6,15}")) {
            throw new InvalidClientDataException(InvalidClientDataException.Reason.NATIONAL_ID_INVALID, client.getNationalId());
        }

        if (client.getPhone() == null || !client.getPhone().matches("\\d{7,15}")) {
            throw new InvalidClientDataException(InvalidClientDataException.Reason.PHONE_INVALID, client.getPhone());
        }

        if (client.getEmail() == null || client.getEmail().length() > 80
                || !EMAIL_PATTERN.matcher(client.getEmail()).matches()) {
            throw new InvalidClientDataException(InvalidClientDataException.Reason.EMAIL_INVALID, client.getEmail());
        }

        if (isRegistration && (client.getPassword() == null || client.getPassword().length() < 8)) {
            throw new InvalidClientDataException(InvalidClientDataException.Reason.PASSWORD_TOO_SHORT, client.getPassword());
        }

        Client clientWithThatEmail = clientRepository.findByEmailIgnoreCase(client.getEmail()).orElse(null);
        if (clientWithThatEmail != null
                && !Objects.equals(clientWithThatEmail.getClientId(), client.getClientId())) {
            throw new InvalidClientDataException(
                    InvalidClientDataException.Reason.EMAIL_ALREADY_REGISTERED, client.getEmail());
        }

        Client clientWithThatNationalId = clientRepository.findByNationalId(client.getNationalId()).orElse(null);
        if (clientWithThatNationalId != null
                && !Objects.equals(clientWithThatNationalId.getClientId(), client.getClientId())) {
            throw new InvalidClientDataException(
                    InvalidClientDataException.Reason.NATIONAL_ID_ALREADY_REGISTERED, client.getNationalId());
        }
    }
}
