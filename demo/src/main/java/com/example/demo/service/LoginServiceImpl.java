package com.example.demo.service;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Client;
import com.example.demo.entities.Operator;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.OperatorRepository;
import com.example.demo.service.interfaces.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Consulta las cuentas y compara sus contraseñas en la capa de servicio. */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Administrator authenticateAdministrator(String email, String password) {
        if (email == null || password == null || password.isBlank()) return null;
        Administrator administrator = administratorRepository.findByEmailIgnoreCase(email.trim()).orElse(null);
        if (administrator != null && administrator.getPassword().equals(password)) return administrator;
        return null;
    }

    @Override
    public Operator authenticateOperator(String email, String password) {
        if (email == null || password == null || password.isBlank()) return null;
        Operator operator = operatorRepository.findByEmailIgnoreCase(email.trim()).orElse(null);
        if (operator != null && operator.getPassword().equals(password)) return operator;
        return null;
    }

    @Override
    public Client authenticateClient(String email, String password) {
        if (email == null || password == null || password.isBlank()) return null;
        Client client = clientRepository.findByEmailIgnoreCase(email.trim()).orElse(null);
        if (client != null && client.getPassword().equals(password)) return client;
        return null;
    }

    @Override
    public boolean isAdministrator(Integer adminId) {
        return adminId != null && administratorRepository.existsById(adminId);
    }

    @Override
    public boolean isOperator(Integer operatorId) {
        return operatorId != null && operatorRepository.existsById(operatorId);
    }

    @Override
    public boolean isClient(Integer clientId) {
        return clientId != null && clientRepository.existsById(clientId);
    }

    @Override
    public boolean isStaff(Integer adminId, Integer operatorId) {
        return isAdministrator(adminId) || isOperator(operatorId);
    }

    @Override
    public void confirmAdministratorPassword(Integer adminId, String password) {
        Administrator administrator = administratorRepository.findById(adminId).orElse(null);
        if (administrator == null || !administrator.getPassword().equals(password)) {
            throw new SecurityException("Enter your current password to confirm this change.");
        }
    }

    @Override
    public void confirmOperatorPassword(Integer operatorId, String password) {
        Operator operator = operatorRepository.findById(operatorId).orElse(null);
        if (operator == null || !operator.getPassword().equals(password)) {
            throw new SecurityException("Enter your current password to confirm this change.");
        }
    }

    /** Los textos del rol solo los envían los servicios, no el formulario. */
    @Override
    public boolean emailUsedByAnotherRole(String email, String role) {
        if (!"ADMINISTRATOR".equals(role) && administratorRepository.findByEmailIgnoreCase(email).isPresent()) return true;
        if (!"OPERATOR".equals(role) && operatorRepository.findByEmailIgnoreCase(email).isPresent()) return true;
        if (!"CLIENT".equals(role) && clientRepository.findByEmailIgnoreCase(email).isPresent()) return true;
        return false;
    }
}
