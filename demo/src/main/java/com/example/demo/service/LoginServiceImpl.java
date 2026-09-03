package com.example.demo.service;

import com.example.demo.entitys.Cliente;
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
    ClienteService clienteService;

    @Override
    public boolean esAdministrador(String usuario, String password) {
        return USUARIO_ADMIN.equals(usuario) && PASSWORD_ADMIN.equals(password);
    }

    @Override
    public Cliente autenticarCliente(String usuario, String password) {
        Cliente cliente;

        try {
            cliente = clienteService.buscarPorCorreo(usuario);
        } catch (NoSuchElementException cuentaInexistente) {
            throw new SecurityException(CREDENCIALES_INVALIDAS);
        }

        if (!cliente.getPassword().equals(password)) {
            throw new SecurityException(CREDENCIALES_INVALIDAS);
        }

        return cliente;
    }
}
