package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.ApplicationConfig;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private void login() {
        LoginService service = new LoginService(this.username.getText(), this.password.getText());
        service.setOnFailed(ev -> {
            System.err.println("Failed Login");
            service.getException().printStackTrace();
        });
        service.start();
    }

    private static class LoginService extends Service<Void> {

        private String username;
        private String password;

        private LoginService(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {

                @Override
                protected Void call() throws Exception {
                    ApplicationConfig.getRepository().login(LoginService.this.username, LoginService.this.password);
                    return null;
                }

            };
        }

    }
}
