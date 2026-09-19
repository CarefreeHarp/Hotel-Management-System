package com.example.demo.service;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.OperatorRepository;
import org.springframework.stereotype.Service;

/** Evita que el mismo correo identifique cuentas de roles distintos. */
@Service
public class AccountEmailService {
    private final AdministratorRepository admins;
    private final OperatorRepository operators;
    private final ClientRepository clients;
    public AccountEmailService(AdministratorRepository admins, OperatorRepository operators, ClientRepository clients) {
        this.admins = admins;
        this.operators = operators;
        this.clients = clients;
    }
    public boolean usedByAnotherRole(String email, AuthenticatedAccount.Role role) {
        return (role != AuthenticatedAccount.Role.ADMINISTRATOR && admins.findByEmailIgnoreCase(email).isPresent())
                || (role != AuthenticatedAccount.Role.OPERATOR && operators.findByEmailIgnoreCase(email).isPresent())
                || (role != AuthenticatedAccount.Role.CLIENT && clients.findByEmailIgnoreCase(email).isPresent());
    }
}
