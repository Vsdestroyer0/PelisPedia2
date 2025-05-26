package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
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
            if (usuario.isAdmin()) {
                openAdminWindows();
            } else {
                openUsuarioWindow();
            }
        } else {
            Alertas.mostrarError("Usuario o contraseña incorrectos");
        }
    } catch (Exception e) {
        Alertas.mostrarError("Error al conectar con la base de datos: " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("PanelUsuario.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Panel de Usuario");
            ((Stage) txtUsername.getScene().getWindow()).close();
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al cargar el fxml de usuario" + e.getMessage());
        }
    }

    @FXML
    private void openAdminWindows(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MenuAdministrador.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Panel de Usuario");
            ((Stage) txtUsername.getScene().getWindow()).close();
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al cargar el fxml de administrador" + e.getMessage());
        }
    }



    @FXML
    private void openRegistroWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("aplicacion/application/CrearCuenta.fxml"));

            if (loader.getLocation() == null) {
                loader = new FXMLLoader(HelloApplication.class.getResource("CrearCuenta.fxml"));
            }

            if (loader.getLocation() == null) {
                loader = new FXMLLoader(getClass().getClassLoader().getResource("/aplicacion/application/CrearCuenta.fxml"));
            }

            if (loader.getLocation() == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: CrearCuenta.fxml en la ruta esperada");
            }

            Alertas.mostrarAdvertencia("Archivo FXML encontrado en: " + loader.getLocation());

            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro usuarios");
            stage.setScene(new Scene(pane));

            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception e){
            Alertas.mostrarError("Error al cargar la ventana de recuperación" + e.getMessage());
        }
    }

    @FXML
    private void openRecuperarContraseña(){
        try{
            // Usar la ruta absoluta desde la raíz del classpath
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("aplicacion/application/RecuperarCuenta1.fxml"));

            if (loader.getLocation() == null) {
                // Si no funciona, intentar con una ruta relativa al paquete de HelloApplication
                loader = new FXMLLoader(HelloApplication.class.getResource("RecuperarCuenta1.fxml"));
            }

            if (loader.getLocation() == null) {
                // Si aún no funciona, intentar con una ruta absoluta desde la raíz
                loader = new FXMLLoader(getClass().getClassLoader().getResource("/RecuperarCuenta/application/RecuperarCuenta1.fxml"));
            }

            if (loader.getLocation() == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: RecuperarCuenta1.fxml en la ruta esperada");
            }

            Alertas.mostrarExito("Archivo FXML encontrado en: " +loader.getLocation());

            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Recuperar Contraseña");
            stage.setScene(new Scene(pane));

            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception e){
            Alertas.mostrarError("Error al abrir la ventana de recuperación" + e.getMessage());
        }
    }


}