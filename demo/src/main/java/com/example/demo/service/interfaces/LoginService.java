package com.example.demo.service.interfaces;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Client;
import com.example.demo.entities.Operator;

/** Autenticación y comprobaciones de cuentas utilizadas por los controladores. */
public interface LoginService {
    Administrator authenticateAdministrator(String email, String password);
    Operator authenticateOperator(String email, String password);
    Client authenticateClient(String email, String password);
    boolean isAdministrator(Integer adminId);
    boolean isOperator(Integer operatorId);
    boolean isClient(Integer clientId);
    boolean isStaff(Integer adminId, Integer operatorId);
    void confirmAdministratorPassword(Integer adminId, String password);
    void confirmOperatorPassword(Integer operatorId, String password);
    boolean emailUsedByAnotherRole(String email, String role);
}
