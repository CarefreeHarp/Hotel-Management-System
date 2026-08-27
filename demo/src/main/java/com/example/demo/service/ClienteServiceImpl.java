package com.example.demo.service;

import com.example.demo.entitys.Cliente;
import com.example.demo.repository.ClienteRepositoryMemoria;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los clientes.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
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
        return clienteRepository.listarTodos()
                .stream()
                .filter(cliente -> cliente.getCorreo().equalsIgnoreCase(correo))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String registrar(Cliente cliente) {
        // El id lo genera el repositorio: el formulario del registro nunca lo envía.
        cliente.setIdCliente(0);

        String error = validarDatosUnicos(cliente);
        if (error != null) {
            return error;
        }

        clienteRepository.guardar(cliente);
        return null;
    }

    @Override
    public String actualizarPerfil(String correoActual, Cliente cliente) {
        Cliente cuentaRegistrada = buscarPorCorreo(correoActual);
        if (cuentaRegistrada == null) {
            return "No existe una cuenta registrada con el correo " + correoActual + ".";
        }

        // Se conserva el id que ya tenía la cuenta para no crear un registro nuevo.
        cliente.setIdCliente(cuentaRegistrada.getIdCliente());

        // Por seguridad el formulario nunca muestra la contraseña guardada: si el
        // cliente no escribe una nueva, se conserva la que ya tenía.
        if (cliente.getPassword() == null || cliente.getPassword().isBlank()) {
            cliente.setPassword(cuentaRegistrada.getPassword());
        }

        String error = validarDatosUnicos(cliente);
        if (error != null) {
            return error;
        }

        clienteRepository.guardar(cliente);
        return null;
    }

    @Override
    public String eliminarCuenta(String correo) {
        Cliente cuentaRegistrada = buscarPorCorreo(correo);
        if (cuentaRegistrada == null) {
            return "No existe una cuenta registrada con el correo " + correo + ".";
        }

        clienteRepository.eliminar(cuentaRegistrada.getIdCliente());
        return null;
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
     */
    private String validarDatosUnicos(Cliente cliente) {
        Cliente clienteConEseCorreo = buscarPorCorreo(cliente.getCorreo());
        if (clienteConEseCorreo != null && clienteConEseCorreo.getIdCliente() != cliente.getIdCliente()) {
            return "Ya existe una cuenta registrada con el correo " + cliente.getCorreo() + ".";
        }

        Cliente clienteConEsaCedula = buscarPorCedula(cliente.getCedula());
        if (clienteConEsaCedula != null && clienteConEsaCedula.getIdCliente() != cliente.getIdCliente()) {
            return "Ya existe una cuenta registrada con la cédula " + cliente.getCedula() + ".";
        }

        return null;
    }
}
