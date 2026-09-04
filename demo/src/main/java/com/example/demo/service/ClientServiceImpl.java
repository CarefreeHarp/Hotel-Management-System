package com.example.demo.service;

import com.example.demo.entities.Client;
import com.example.demo.repository.ClientInMemoryRepository;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los clientes.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 *
 * Todas las validaciones y todos los mensajes de error viven aquí: cuando algo
 * no se cumple se lanza una excepción con el mensaje que verá el usuario.
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    public ClientInMemoryRepository clientRepository;

    @Override
    public List<Client> listClients() {
        return clientRepository.listAll();
    }

    @Override
    public Client findByEmail(String email) {
        Client client = findProfile(email);
        if (client == null) {
            throw new NoSuchElementException("No guest profile is registered with the email " + email + ".");
        }

        return client;
    }

    @Override
    public void register(Client client) {
        // El id lo genera el repositorio: el formulario del registro nunca lo envía.
        client.setClientId(0);

        validateDataUnique(client);
        clientRepository.save(client);
    }

    @Override
    public void updateProfile(String emailCurrent, Client client, String passwordCurrent) {
        Client profileRegistrada = findByEmail(emailCurrent);

        if (!profileRegistrada.getPassword().equals(passwordCurrent)) {
            throw new SecurityException("The current password does not match.");
        }

        // Keep the profile identity and password unchanged during profile updates.
        client.setClientId(profileRegistrada.getClientId());
        client.setPassword(profileRegistrada.getPassword());

        validateDataUnique(client);
        clientRepository.save(client);
    }

    @Override
    public void deleteProfile(String email) {
        Client profileRegistrada = findByEmail(email);
        clientRepository.delete(profileRegistrada.getClientId());
    }

    /**
     * Búsqueda interna que sí puede devolver null, porque las validaciones
     * necesitan preguntar si existe una cuenta sin que eso sea un error.
     */
    private Client findProfile(String email) {
        return clientRepository.listAll()
                .stream()
                .filter(client -> client.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /** Busca por cédula para poder validar que no se repita. */
    private Client findByNationalId(String nationalId) {
        return clientRepository.listAll()
                .stream()
                .filter(client -> client.getNationalId().equals(nationalId))
                .findFirst()
                .orElse(null);
    }

    /**
     * El email y la cédula son campos únicos: ningún otro cliente puede tenerlos.
     * Se compara contra el id porque, al editar el perfil, el propio cliente sí
     * conserva su email y su cédula y eso no debe contar como duplicado.
     *
     * @throws IllegalArgumentException con el mensaje del primer dato inválido.
     */
    private void validateDataUnique(Client client) {
        Client clientWithThatEmail = findProfile(client.getEmail());
        if (clientWithThatEmail != null && clientWithThatEmail.getClientId() != client.getClientId()) {
            throw new IllegalArgumentException(
                    "A guest profile is already registered with the email " + client.getEmail() + ".");
        }

        Client clientWithThatNationalId = findByNationalId(client.getNationalId());
        if (clientWithThatNationalId != null && clientWithThatNationalId.getClientId() != client.getClientId()) {
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
