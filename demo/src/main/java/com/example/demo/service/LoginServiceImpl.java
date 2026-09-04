package com.example.demo.service;

import com.example.demo.entities.Client;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la autenticación del portal.
 *
 * El administrador todavía no se guarda en el repositorio, así que sus
 * credenciales viven aquí como constantes; el día que exista una entidad
 * administrador solo cambia esta clase y no el controlador.
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final String USUARIO_ADMIN = "admin";
    private static final String PASSWORD_ADMIN = "admin";

    /** Mensaje único para usuario inexistente y contraseña incorrecta: así no se revela cuál de los dos falló. */
    private static final String CREDENCIALES_INVALIDAS = "Incorrect username or password.";

    @Autowired
    ClientService clientService;

    @Override
    public boolean isAdministrator(String user, String password) {
        return USUARIO_ADMIN.equals(user) && PASSWORD_ADMIN.equals(password);
    }

    @Override
    public Client authenticateClient(String user, String password) {
        Client client;

        try {
            client = clientService.findByEmail(user);
        } catch (NoSuchElementException profileNotFound) {
            throw new SecurityException(CREDENCIALES_INVALIDAS);
        }

        if (!client.getPassword().equals(password)) {
            throw new SecurityException(CREDENCIALES_INVALIDAS);
        }

        return client;
    }
}
