package com.example.demo.service.interfaces;
import com.example.demo.service.AuthenticatedAccount;

public interface LoginService {
    AuthenticatedAccount authenticate(String email, String password);
    void confirmPassword(AuthenticatedAccount account, String password);
    boolean exists(AuthenticatedAccount account);
}
