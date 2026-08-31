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
import org.springframework.web.bind.annotation.RequestParam;
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
     * Muestra el formulario de registro vacío.
     * URL: http://localhost:8080/clientes/create
     */
    // Full URL: http://localhost:8080/clientes/create
    @GetMapping("/create")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Registro de cliente");
        model.addAttribute("accion", "/clientes/create");
        model.addAttribute("esEdicion", false);
        return "clientes/formulario";
    }

    /**
     * Registra al cliente que llenó el formulario.
     * Si el correo o la cédula ya existen, vuelve al formulario con el error.
     */
    // Full URL: http://localhost:8080/clientes/create
    @PostMapping("/create")
    public String registrar(@ModelAttribute Cliente cliente, Model model) {
        String error = clienteService.registrar(cliente);

        if (error != null) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("titulo", "Registro de cliente");
            model.addAttribute("accion", "/clientes/create");
            model.addAttribute("esEdicion", false);
            model.addAttribute("error", error);
            return "clientes/formulario";
        }

        return "redirect:/login";
    }

    /**
     * Perfil del cliente: sus datos personales.
     * URL: http://localhost:8080/clientes/read/{correo}
     */
    // Full URL: http://localhost:8080/clientes/read/{correo}
    @GetMapping("/read/{correo}")
    public String verPerfil(@PathVariable("correo") String correo, Model model) {
        Cliente cliente = clienteService.buscarPorCorreo(correo);
        if (cliente == null) {
            return "redirect:/admin/clientes/read";
        }

        model.addAttribute("cliente", cliente);
        return "clientes/detalle";
    }

    /**
     * Muestra el formulario con los datos actuales del cliente para modificarlos.
     * URL: http://localhost:8080/clientes/update/{correo}
     */
    // Full URL: http://localhost:8080/clientes/update/{correo}
    @GetMapping("/update/{correo}")
    public String mostrarFormularioEdicion(@PathVariable("correo") String correo, Model model) {
        Cliente cliente = clienteService.buscarPorCorreo(correo);
        if (cliente == null) {
            return "redirect:/admin/clientes/read";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar mis datos");
        model.addAttribute("accion", "/clientes/update/" + correo);
        model.addAttribute("esEdicion", true);
        return "clientes/formulario";
    }

    /**
     * Guarda los cambios del perfil. El correo de la URL es el que tenía la cuenta
     * antes de editarla, porque el cliente puede estar cambiando su correo.
     */
    // Full URL: http://localhost:8080/clientes/update/{correo}
    @PostMapping("/update/{correo}")
    public String actualizarPerfil(@PathVariable("correo") String correoActual,
                                   @ModelAttribute Cliente cliente,
                                   @RequestParam String passwordActual,
                                   Model model) {
        String error = clienteService.actualizarPerfil(correoActual, cliente, passwordActual);

        if (error != null) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("titulo", "Editar mis datos");
            model.addAttribute("accion", "/clientes/update/" + correoActual);
            model.addAttribute("esEdicion", true);
            model.addAttribute("error", error);
            return "clientes/formulario";
        }

        return "redirect:/clientes/read/" + cliente.getCorreo();
    }

    /**
     * Elimina la cuenta del cliente.
     * Se usa POST y no GET porque es una acción que modifica datos.
     */
    // Full URL: http://localhost:8080/clientes/delete/{correo}
    @PostMapping("/delete/{correo}")
    public String eliminarCuenta(@PathVariable("correo") String correo) {
        clienteService.eliminarCuenta(correo);
        return "redirect:/admin/clientes/read";
    }
}
