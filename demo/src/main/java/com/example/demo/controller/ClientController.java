package com.example.demo.controller;

import com.example.demo.entities.Client;
import com.example.demo.service.ClientService;
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
 * ve y modifica su propia cuenta, identificada por su email.
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
public class ClientController {

    @Autowired
    ClientService clientService;

    /**
     * Muestra el formulario de registro vacío.
     * URL: http://localhost:8080/clientes/create
     */
    // Full URL: http://localhost:8080/clientes/create
    @GetMapping("/create")
    public String showFormRegistro(Model model) {
        prepareForm(model, new Client(), "Client registration", "/clientes/create", false);
        return "clientes/formulario";
    }

    /**
     * Registra al cliente que llenó el formulario.
     * Si el email o la cédula ya existen, el servicio lanza IllegalArgumentException
     * y se vuelve al formulario con ese mensaje.
     */
    // Full URL: http://localhost:8080/clientes/create
    @PostMapping("/create")
    public String register(@ModelAttribute Client client, Model model) {
        try {
            clientService.register(client);
            return "redirect:/login";
        } catch (IllegalArgumentException dataInvalidos) {
            prepareForm(model, client, "Client registration", "/clientes/create", false);
            model.addAttribute("error", dataInvalidos.getMessage());
            return "clientes/formulario";
        }
    }

    /**
     * Perfil del cliente: sus datos personales.
     * URL: http://localhost:8080/clientes/read/{email}
     */
    // Full URL: http://localhost:8080/clientes/read/{email}
    @GetMapping("/read/{email}")
    public String verProfile(@PathVariable("email") String email,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("cliente", clientService.findByEmail(email));
            return "clientes/detalle";
        } catch (NoSuchElementException profileNotFound) {
            redirectAttributes.addFlashAttribute("error", profileNotFound.getMessage());
            return "redirect:/admin/clientes/read";
        }
    }

    /**
     * Muestra el formulario con los datos actuales del cliente para modificarlos.
     * URL: http://localhost:8080/clientes/update/{email}
     */
    // Full URL: http://localhost:8080/clientes/update/{email}
    @GetMapping("/update/{email}")
    public String showFormEditing(@PathVariable("email") String email,
                                           Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            Client client = clientService.findByEmail(email);
            prepareForm(model, client, "Edit my details", "/clientes/update/" + email, true);
            return "clientes/formulario";
        } catch (NoSuchElementException profileNotFound) {
            redirectAttributes.addFlashAttribute("error", profileNotFound.getMessage());
            return "redirect:/admin/clientes/read";
        }
    }

    /**
     * Guarda los cambios del perfil. El email de la URL es el que tenía la cuenta
     * antes de editarla, porque el cliente puede estar cambiando su email.
     */
    // Full URL: http://localhost:8080/clientes/update/{email}
    @PostMapping("/update/{email}")
    public String updateProfile(@PathVariable("email") String emailCurrent,
                                   @ModelAttribute Client client,
                                   @RequestParam String passwordCurrent,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            clientService.updateProfile(emailCurrent, client, passwordCurrent);
            return "redirect:/clientes/read/" + client.getEmail();
        } catch (NoSuchElementException profileNotFound) {
            redirectAttributes.addFlashAttribute("error", profileNotFound.getMessage());
            return "redirect:/admin/clientes/read";
        } catch (SecurityException | IllegalArgumentException dataInvalidos) {
            prepareForm(model, client, "Edit my details", "/clientes/update/" + emailCurrent, true);
            model.addAttribute("error", dataInvalidos.getMessage());
            return "clientes/formulario";
        }
    }

    /**
     * Elimina la cuenta del cliente.
     * Se usa POST y no GET porque es una acción que modifica datos.
     * Al borrarse la cuenta ya no hay perfil que mostrar, así que se vuelve al login.
     */
    // Full URL: http://localhost:8080/clientes/delete/{email}
    @PostMapping("/delete/{email}")
    public String deleteProfile(@PathVariable("email") String email, RedirectAttributes redirectAttributes) {
        try {
            clientService.deleteProfile(email);
        } catch (NoSuchElementException profileNotFound) {
            redirectAttributes.addFlashAttribute("error", profileNotFound.getMessage());
        }

        return "redirect:/login";
    }

    /** Atributos que necesita la vista del formulario, tanto al crear como al editar. */
    private void prepareForm(Model model, Client client, String title, String action, boolean esEditing) {
        model.addAttribute("cliente", client);
        model.addAttribute("titulo", title);
        model.addAttribute("accion", action);
        model.addAttribute("esEdicion", esEditing);
    }
}
