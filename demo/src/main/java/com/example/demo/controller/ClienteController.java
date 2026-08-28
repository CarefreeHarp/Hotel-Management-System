package com.example.demo.controller;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CAPA DE CONTROLADOR: pantallas del cliente.
 *
 * A diferencia del CRUD del administrador, aquí el cliente se crea a sí mismo:
 * no hay una pantalla de "crear cliente", hay un registro público al que llega
 * cualquier visitante. Por lo mismo tampoco hay edición de terceros: cada cliente
 * ve y modifica su propia cuenta, identificada por su correo.
 */
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    /**
     * Listado de clientes. En la aplicación final esta pantalla no existiría para
     * el cliente; se deja para poder probar el CRUD durante este sprint.
     * URL: http://localhost:8080/clientes
     */
    @GetMapping()
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "clientes/lista";
    }

    /**
     * Muestra el formulario de registro vacío.
     * URL: http://localhost:8080/clientes/registro
     */
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Registro de cliente");
        model.addAttribute("accion", "/clientes/registro");
        return "clientes/formulario";
    }

    /**
     * Registra al cliente que llenó el formulario.
     * Si el correo o la cédula ya existen, vuelve al formulario con el error.
     */
    @PostMapping("/registro")
    public String registrar(@ModelAttribute Cliente cliente, Model model) {
        String error = clienteService.registrar(cliente);

        if (error != null) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("titulo", "Registro de cliente");
            model.addAttribute("accion", "/clientes/registro");
            model.addAttribute("error", error);
            return "clientes/formulario";
        }

        return "redirect:/clientes";
    }

    /**
     * Perfil del cliente: sus datos personales.
     * URL: http://localhost:8080/clientes/{correo}
     */
    @GetMapping("/{correo}")
    public String verPerfil(@PathVariable("correo") String correo, Model model) {
        Cliente cliente = clienteService.buscarPorCorreo(correo);
        if (cliente == null) {
            return "redirect:/clientes";
        }

        model.addAttribute("cliente", cliente);
        return "clientes/detalle";
    }

    /**
     * Muestra el formulario con los datos actuales del cliente para modificarlos.
     * URL: http://localhost:8080/clientes/{correo}/editar
     */
    @GetMapping("/{correo}/editar")
    public String mostrarFormularioEdicion(@PathVariable("correo") String correo, Model model) {
        Cliente cliente = clienteService.buscarPorCorreo(correo);
        if (cliente == null) {
            return "redirect:/clientes";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar mis datos");
        model.addAttribute("accion", "/clientes/" + correo + "/editar");
        return "clientes/formulario";
    }

    /**
     * Guarda los cambios del perfil. El correo de la URL es el que tenía la cuenta
     * antes de editarla, porque el cliente puede estar cambiando su correo.
     */
    @PostMapping("/{correo}/editar")
    public String actualizarPerfil(@PathVariable("correo") String correoActual,
                                   @ModelAttribute Cliente cliente,
                                   Model model) {
        String error = clienteService.actualizarPerfil(correoActual, cliente);

        if (error != null) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("titulo", "Editar mis datos");
            model.addAttribute("accion", "/clientes/" + correoActual + "/editar");
            model.addAttribute("error", error);
            return "clientes/formulario";
        }

        return "redirect:/clientes";
    }

    /**
     * Elimina la cuenta del cliente.
     * Se usa POST y no GET porque es una acción que modifica datos.
     */
    @PostMapping("/{correo}/eliminar")
    public String eliminarCuenta(@PathVariable("correo") String correo) {
        clienteService.eliminarCuenta(correo);
        return "redirect:/clientes";
    }
}
