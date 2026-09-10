package com.example.demo.controller;

import com.example.demo.entities.Client;
import com.example.demo.errors.InvalidClientDataException;
import com.example.demo.errors.InvalidCurrentPasswordException;
import com.example.demo.service.ClientService;
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
 * ve y modifica su propia cuenta, identificada por su id autogenerado.
 *
 * El controlador NO valida nada: llama al servicio. Los errores de los datos
 * del formulario y de contraseña de confirmación vuelven al formulario con un
 * aviso; los demás errores de negocio se atienden centralizadamente.
 */
@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    ClientService clientService;

    /**
     * Muestra el formulario de registro vacío.
     * URL: http://localhost:8080/clients/create
     */
    // Full URL: http://localhost:8080/clients/create
    @GetMapping("/create")
    public String showFormRegistro(Model model) {
        prepareForm(model, Client.builder().build(), "Client registration", "/clients/create", false);
        return "clients/form";
    }

    /**
     * Registra al cliente que llenó el formulario.
     * Si el email o la cédula ya existen, el servicio lanza InvalidClientDataException
     * y se vuelve al formulario con ese mensaje.
     */
    // Full URL: http://localhost:8080/clients/create
    @PostMapping("/create")
    public String register(@ModelAttribute Client client, Model model) {
        try {
            clientService.register(client);
            return "redirect:/login";
        } catch (InvalidClientDataException exception) {
            prepareForm(model, client, "Client registration", "/clients/create", false);
            model.addAttribute("error", exception.getMessage());
            return "clients/form";
        }
    }

    /**
     * Perfil del cliente: sus datos personales.
     * URL: http://localhost:8080/clients/read/{clientId}
     */
    // Full URL: http://localhost:8080/clients/read/{clientId}
    @GetMapping("/read/{clientId}")
    public String verProfile(@PathVariable Integer clientId,
                            Model model) {
        model.addAttribute("cliente", clientService.findById(clientId));
        return "clients/details";
    }

    /**
     * Muestra el formulario con los datos actuales del cliente para modificarlos.
     * URL: http://localhost:8080/clients/update/{clientId}
     */
    // Full URL: http://localhost:8080/clients/update/{clientId}
    @GetMapping("/update/{clientId}")
    public String showFormEditing(@PathVariable Integer clientId,
                                           Model model) {
        Client client = clientService.findById(clientId);
        prepareForm(model, client, "Edit my details", "/clients/update/" + clientId, true);
        return "clients/form";
    }

    /**
     * Guarda los cambios del perfil. El id de la URL identifica la cuenta
     * incluso si el cliente cambia su email.
     */
    // Full URL: http://localhost:8080/clients/update/{clientId}
    @PostMapping("/update/{clientId}")
    public String updateProfile(@PathVariable Integer clientId,
                                   @ModelAttribute Client client,
                                   @RequestParam String passwordCurrent,
                                   Model model) {
        try {
            clientService.updateProfile(clientId, client, passwordCurrent);
            return "redirect:/clients/read/" + clientId;
        } catch (InvalidCurrentPasswordException | InvalidClientDataException exception) {
            prepareForm(model, client, "Edit my details", "/clients/update/" + clientId, true);
            model.addAttribute("error", exception.getMessage());
            return "clients/form";
        }
    }

    /**
     * Elimina la cuenta del cliente.
     * Se usa POST y no GET porque es una acción que modifica datos.
     * Al borrarse la cuenta ya no hay perfil que mostrar, así que se vuelve al login.
     */
    // Full URL: http://localhost:8080/clients/delete/{clientId}
    @PostMapping("/delete/{clientId}")
    public String deleteProfile(@PathVariable Integer clientId) {
        clientService.deleteProfile(clientId);
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
