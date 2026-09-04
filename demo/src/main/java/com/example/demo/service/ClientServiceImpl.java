package com.example.demo.service;

import com.example.demo.entities.Client;
import com.example.demo.repository.ClientRepository;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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

    @Autowired
    public ClientRepository clientRepository;

    @Override
    public List<Client> listClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NoSuchElementException(
                        "No guest profile is registered with the email " + email + "."));
    }

    @Override
    public void register(Client client) {
        // El id lo genera la base de datos (IDENTITY). Se envía en null para que
        // Hibernate haga un INSERT: el formulario del registro nunca lo manda.
        client.setClientId(null);

        validateDataUnique(client);
        clientRepository.save(client);
    }

    @Override
    public void updateProfile(String emailCurrent, Client client, String passwordCurrent) {
        Client profileRegistrada = findByEmail(emailCurrent);

        if (!profileRegistrada.getPassword().equals(passwordCurrent)) {
            throw new SecurityException("The current password does not match.");
        }

        // Se conserva el id para que save() actualice la fila que ya existe en vez
        // de insertar una nueva, y la contraseña porque el formulario no la edita.
        client.setClientId(profileRegistrada.getClientId());
        client.setPassword(profileRegistrada.getPassword());

        validateDataUnique(client);
        clientRepository.save(client);
    }

    @Override
    public void deleteProfile(String email) {
        Client profileRegistrada = findByEmail(email);
        clientRepository.deleteById(profileRegistrada.getClientId());
    }

    /**
     * El email y la cédula son campos únicos: ningún otro cliente puede tenerlos.
     * Se comparan los ids porque, al editar el perfil, el propio cliente sí
     * conserva su email y su cédula y eso no debe contar como duplicado.
     *
     * Los ids se comparan con Objects.equals y no con != porque son Integer (un
     * objeto), y en un cliente nuevo el id todavía viene en null.
     *
     * @throws IllegalArgumentException con el mensaje del primer dato inválido.
     */
    private void validateDataUnique(Client client) {
        Client clientWithThatEmail = clientRepository.findByEmailIgnoreCase(client.getEmail()).orElse(null);
        if (clientWithThatEmail != null
                && !Objects.equals(clientWithThatEmail.getClientId(), client.getClientId())) {
            throw new IllegalArgumentException(
                    "A guest profile is already registered with the email " + client.getEmail() + ".");
        }

        Client clientWithThatNationalId = clientRepository.findByNationalId(client.getNationalId()).orElse(null);
        if (clientWithThatNationalId != null
                && !Objects.equals(clientWithThatNationalId.getClientId(), client.getClientId())) {
            throw new IllegalArgumentException(
                    "A guest profile is already registered with the national ID " + client.getNationalId() + ".");
        }

        if (!isValidHttpUrl(client.getProfilePhoto())) {
            throw new IllegalArgumentException("The profile photo must be a valid HTTP or HTTPS URL.");
        }
    }

    private boolean isValidHttpUrl(String url) {
        if (url == null || url.isBlank()) {
            return true;
        }

        try {
            URI uri = URI.create(url.trim());
            return uri.isAbsolute() && ("http".equalsIgnoreCase(uri.getScheme())
                    || "https".equalsIgnoreCase(uri.getScheme()));
        } catch (IllegalArgumentException error) {
            return false;
        }
    }
}
