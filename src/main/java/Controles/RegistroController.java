package Controles;

import aplicacion.BaseDatos.BaseDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegistroController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextArea txtDescripcion;

    BaseDatos BD = new BaseDatos();

    @FXML
    public void initialize() {
    }

    @FXML
    public void handleRegister(ActionEvent event){
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        if (username.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de credenciales");
            alert.setContentText("No ha registrado los campos");
            alert.showAndWait();
        }
        else {
            agregarUsuario(username, password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro exitoso");
            alert.setContentText("Se ha registrado correctamente");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleRegresar(ActionEvent event){
        openLoginWindow();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void agregarUsuario(String username, String password){
        BD.AgregarUsuario(username, password, false);
    }

    private void openLoginWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hello-viwe.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(pane));
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
