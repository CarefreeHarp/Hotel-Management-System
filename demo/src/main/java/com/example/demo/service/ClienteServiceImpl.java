package com.example.demo.service;

import com.example.demo.entitys.Cliente;
import com.example.demo.repository.ClienteRepositoryMemoria;
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
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    public ClienteRepositoryMemoria clienteRepository;

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.listarTodos();
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        Cliente cliente = buscarCuenta(correo);
        if (cliente == null) {
            throw new NoSuchElementException("No account is registered with the email " + correo + ".");
        }

        return cliente;
    }

    @Override
    public void registrar(Cliente cliente) {
        // El id lo genera el repositorio: el formulario del registro nunca lo envía.
        cliente.setIdCliente(0);

        validarDatosUnicos(cliente);
        clienteRepository.guardar(cliente);
    }

    @Override
    public void actualizarPerfil(String correoActual, Cliente cliente, String passwordActual) {
        Cliente cuentaRegistrada = buscarPorCorreo(correoActual);

        if (!cuentaRegistrada.getPassword().equals(passwordActual)) {
            throw new SecurityException("The current password does not match.");
        }

        // Keep the account identity and password unchanged during profile updates.
        cliente.setIdCliente(cuentaRegistrada.getIdCliente());
        cliente.setPassword(cuentaRegistrada.getPassword());

        validarDatosUnicos(cliente);
        clienteRepository.guardar(cliente);
    }

    @Override
    public void eliminarCuenta(String correo) {
        Cliente cuentaRegistrada = buscarPorCorreo(correo);
        clienteRepository.eliminar(cuentaRegistrada.getIdCliente());
    }

    /**
     * Búsqueda interna que sí puede devolver null, porque las validaciones
     * necesitan preguntar si existe una cuenta sin que eso sea un error.
     */
    private Cliente buscarCuenta(String correo) {
        return clienteRepository.listarTodos()
                .stream()
                .filter(cliente -> cliente.getCorreo().equalsIgnoreCase(correo))
                .findFirst()
                .orElse(null);
    }

    /** Busca por cédula para poder validar que no se repita. */
    private Cliente buscarPorCedula(String cedula) {
        return clienteRepository.listarTodos()
                .stream()
                .filter(cliente -> cliente.getCedula().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    /**
     * El correo y la cédula son campos únicos: ningún otro cliente puede tenerlos.
     * Se compara contra el id porque, al editar el perfil, el propio cliente sí
     * conserva su correo y su cédula y eso no debe contar como duplicado.
     *
     * @throws IllegalArgumentException con el mensaje del primer dato inválido.
     */
    private void validarDatosUnicos(Cliente cliente) {
        Cliente clienteConEseCorreo = buscarCuenta(cliente.getCorreo());
        if (clienteConEseCorreo != null && clienteConEseCorreo.getIdCliente() != cliente.getIdCliente()) {
            throw new IllegalArgumentException(
                    "An account is already registered with the email " + cliente.getCorreo() + ".");
        }

        Cliente clienteConEsaCedula = buscarPorCedula(cliente.getCedula());
        if (clienteConEsaCedula != null && clienteConEsaCedula.getIdCliente() != cliente.getIdCliente()) {
            throw new IllegalArgumentException(
                    "An account is already registered with the national ID " + cliente.getCedula() + ".");
        }

        if (!esUrlHttpValida(cliente.getFotoPerfil())) {
            throw new IllegalArgumentException("The profile photo must be a valid HTTP or HTTPS URL.");
        }
    }

    private boolean esUrlHttpValida(String url) {
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
