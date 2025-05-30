package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import aplicacion.application.SesionUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Alertas.mostrarError("Por favor ingrese usuario y contraseña");
            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAOImp();
            UsuarioVO usuario = usuarioDAO.autenticarUsuario(username, password);

            if (usuario != null) {
                // Guardar el usuario en la sesión
                SesionUsuario.getInstancia().iniciarSesion(usuario);
                
                // Mostrar mensaje de bienvenida
                Alertas.mostrarExito("¡Bienvenido/a " + usuario.getNombre() + "!");
                
                if (usuario.isAdmin()) {
                    openAdminWindows();
                } else {
                    openUsuarioWindow();
                }
            } else {
                Alertas.mostrarError("Usuario o contraseña incorrectos");
                // Limpiar el campo de contraseña para facilitar un nuevo intento
                txtPassword.clear();
                txtPassword.requestFocus();
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleRegister(ActionEvent event){
        openRegistroWindow();
    }

    @FXML
    void handleRecuperacion(ActionEvent event){
        openRecuperarContraseña();
    }

    @FXML
    private void openUsuarioWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MenuUsuario.fxml"));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Panel de Usuario - PelisPedia");
            
            // Cerrar la ventana actual
            ((Stage) txtUsername.getScene().getWindow()).close();
            
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al cargar la pantalla de usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openAdminWindows(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MenuAdministrador.fxml"));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Panel de Administrador - PelisPedia");
            
            // Cerrar la ventana actual
            ((Stage) txtUsername.getScene().getWindow()).close();
            
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al cargar la pantalla de administrador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openRegistroWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("CrearCuenta.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Registro de Usuario - PelisPedia");
            stage.setScene(new Scene(root));
            
            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();
            
            stage.show();
        } catch (IOException e){
            Alertas.mostrarError("Error al cargar la pantalla de registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openRecuperarContraseña(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("RecuperarCuenta1.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Recuperar Contraseña - PelisPedia");
            stage.setScene(new Scene(root));
            
            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();
            
            stage.show();
        } catch (IOException e){
            Alertas.mostrarError("Error al cargar la pantalla de recuperación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}