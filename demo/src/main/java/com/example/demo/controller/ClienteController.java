package com.example.demo.controller;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CAPA DE CONTROLADOR: pantallas del cliente.
 *
 * A diferencia del CRUD del administrador, aquí el cliente se crea a sí mismo:
 * no hay una pantalla de "crear cliente", hay un registro público al que llega
 * cualquier visitante. Por lo mismo tampoco hay edición de terceros: cada cliente
 * ve y modifica su propia cuenta, identificada por su correo.
 *
 * El controlador NO valida nada: llama al servicio y, según la excepción que
 * este lance, decide a qué pantalla se lleva al usuario.
 *
 * - NoSuchElementException  -> la cuenta no existe, se vuelve al listado.
 * - IllegalArgumentException / SecurityException -> los datos del formulario
 *   están mal, se vuelve al formulario mostrando el mensaje del servicio.
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
        prepararFormulario(model, new Cliente(), "Client registration", "/clientes/create", false);
        return "clientes/formulario";
    }

    /**
     * Registra al cliente que llenó el formulario.
     * Si el correo o la cédula ya existen, el servicio lanza IllegalArgumentException
     * y se vuelve al formulario con ese mensaje.
     */
    // Full URL: http://localhost:8080/clientes/create
    @PostMapping("/create")
    public String registrar(@ModelAttribute Cliente cliente, Model model) {
        try {
            clienteService.registrar(cliente);
            return "redirect:/login";
        } catch (IllegalArgumentException datosInvalidos) {
            prepararFormulario(model, cliente, "Client registration", "/clientes/create", false);
            model.addAttribute("error", datosInvalidos.getMessage());
            return "clientes/formulario";
        }
    }

    /**
     * Perfil del cliente: sus datos personales.
     * URL: http://localhost:8080/clientes/read/{correo}
     */
    // Full URL: http://localhost:8080/clientes/read/{correo}
    @GetMapping("/read/{correo}")
    public String verPerfil(@PathVariable("correo") String correo,
                            Model model,
                            RedirectAttributes redireccion) {
        try {
            model.addAttribute("cliente", clienteService.buscarPorCorreo(correo));
            return "clientes/detalle";
        } catch (NoSuchElementException cuentaInexistente) {
            redireccion.addFlashAttribute("error", cuentaInexistente.getMessage());
            return "redirect:/admin/clientes/read";
        }
    }

    /**
     * Muestra el formulario con los datos actuales del cliente para modificarlos.
     * URL: http://localhost:8080/clientes/update/{correo}
     */
    // Full URL: http://localhost:8080/clientes/update/{correo}
    @GetMapping("/update/{correo}")
    public String mostrarFormularioEdicion(@PathVariable("correo") String correo,
                                           Model model,
                                           RedirectAttributes redireccion) {
        try {
            Cliente cliente = clienteService.buscarPorCorreo(correo);
            prepararFormulario(model, cliente, "Edit my details", "/clientes/update/" + correo, true);
            return "clientes/formulario";
        } catch (NoSuchElementException cuentaInexistente) {
            redireccion.addFlashAttribute("error", cuentaInexistente.getMessage());
            return "redirect:/admin/clientes/read";
        }
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
                                   Model model,
                                   RedirectAttributes redireccion) {
        try {
            clienteService.actualizarPerfil(correoActual, cliente, passwordActual);
            return "redirect:/clientes/read/" + cliente.getCorreo();
        } catch (NoSuchElementException cuentaInexistente) {
            redireccion.addFlashAttribute("error", cuentaInexistente.getMessage());
            return "redirect:/admin/clientes/read";
        } catch (SecurityException | IllegalArgumentException datosInvalidos) {
            prepararFormulario(model, cliente, "Edit my details", "/clientes/update/" + correoActual, true);
            model.addAttribute("error", datosInvalidos.getMessage());
            return "clientes/formulario";
        }
    }

    /**
     * Elimina la cuenta del cliente.
     * Se usa POST y no GET porque es una acción que modifica datos.
     * Al borrarse la cuenta ya no hay perfil que mostrar, así que se vuelve al login.
     */
    // Full URL: http://localhost:8080/clientes/delete/{correo}
    @PostMapping("/delete/{correo}")
    public String eliminarCuenta(@PathVariable("correo") String correo, RedirectAttributes redireccion) {
        try {
            clienteService.eliminarCuenta(correo);
        } catch (NoSuchElementException cuentaInexistente) {
            redireccion.addFlashAttribute("error", cuentaInexistente.getMessage());
        }

        return "redirect:/login";
    }

    /** Atributos que necesita la vista del formulario, tanto al crear como al editar. */
    private void prepararFormulario(Model model, Cliente cliente, String titulo, String accion, boolean esEdicion) {
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", titulo);
        model.addAttribute("accion", accion);
        model.addAttribute("esEdicion", esEdicion);
    }
}
