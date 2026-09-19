package com.example.demo.service;

import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.OperatorRepository;
import com.example.demo.service.interfaces.LoginService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import static com.example.demo.service.AuthenticatedAccount.Role.*;

/** Autenticación de las tres clases de cuenta guardadas en la base de datos. */
@Service
public class LoginServiceImpl implements LoginService {
    private final AdministratorRepository admins;
    private final OperatorRepository operators;
    private final ClientRepository clients;

    public LoginServiceImpl(AdministratorRepository admins, OperatorRepository operators, ClientRepository clients) {
        this.admins = admins;
        this.operators = operators;
        this.clients = clients;
    }

    @Override
    public AuthenticatedAccount authenticate(String email, String password) {
        if (email == null || password == null || password.isBlank()) {
            throw new SecurityException("Incorrect email or password.");
        }
        String normalized = email.trim();
        List<AuthenticatedAccount> matches = new ArrayList<>();
        admins.findByEmailIgnoreCase(normalized).filter(a -> password.equals(a.getPassword()))
                .ifPresent(a -> matches.add(new AuthenticatedAccount(a.getAdminId(), ADMINISTRATOR)));
        operators.findByEmailIgnoreCase(normalized).filter(o -> password.equals(o.getPassword()))
                .ifPresent(o -> matches.add(new AuthenticatedAccount(o.getOperatorId(), OPERATOR)));
        clients.findByEmailIgnoreCase(normalized).filter(c -> password.equals(c.getPassword()))
                .ifPresent(c -> matches.add(new AuthenticatedAccount(c.getClientId(), CLIENT)));
        if (matches.size() != 1) throw new SecurityException("Incorrect email or password.");
        return matches.get(0);
    }

    @Override
    public void confirmPassword(AuthenticatedAccount account, String password) {
        String stored = switch (account.role()) {
            case ADMINISTRATOR -> admins.findById(account.id()).map(a -> a.getPassword()).orElse(null);
            case OPERATOR -> operators.findById(account.id()).map(o -> o.getPassword()).orElse(null);
            case CLIENT -> clients.findById(account.id()).map(c -> c.getPassword()).orElse(null);
        };
        if (stored == null || !stored.equals(password)) {
            throw new SecurityException("Enter your current password to confirm this change.");
        }
    }

    @Override
    public boolean exists(AuthenticatedAccount account) {
        return switch (account.role()) {
            case ADMINISTRATOR -> admins.existsById(account.id());
            case OPERATOR -> operators.existsById(account.id());
            case CLIENT -> clients.existsById(account.id());
        };
    }
}
