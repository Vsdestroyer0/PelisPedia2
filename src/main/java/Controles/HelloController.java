package Controles;

import aplicacion.BaseDatos.BaseDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;

    BaseDatos BD = new BaseDatos();

    //@Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    @FXML
    private void handleLogin(ActionEvent event){
        String username =txtUsername.getText();
        String password = txtPassword.getText();

        validarCredenciales(username, password);
    }

    @FXML
    private void validarCredenciales(String username, String password){
        Boolean exito = BD.validar_Usuario(username, password);

        if(!exito){
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al ingresar");
            alert.setContentText("Las credenciales no fueron válidas");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ingreso exitoso");
        boolean esAdmin = BD.esAdmin(username, password);
        if (esAdmin){
            alert.setContentText("Bienvenido administrador");
        }else {
            alert.setContentText("Bienvenido usuario");
        }
        alert.showAndWait();
    }




}