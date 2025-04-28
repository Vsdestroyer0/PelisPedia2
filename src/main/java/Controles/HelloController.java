package Controles;

import aplicacion.BaseDatos.DatabaseConnection;
import aplicacion.application.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;

    DatabaseConnection BD = new DatabaseConnection();


    @FXML
    void handleLogin(ActionEvent event){
        String username =txtUsername.getText();
        String password = txtPassword.getText();

    }

    @FXML
    void handleRegister(ActionEvent event){
        openRegistroWindow();
    }

    private void openAltaPlantasWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("AdministradorOpciones.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Alta de plantas");
            stage.setScene(new Scene(pane));
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();
            stage.close();
            stage.show();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    private void openUsuarioWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("PanelUsuario.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Panel de Usuario");

            // Cierra la ventana actual de login
            ((Stage) txtUsername.getScene().getWindow()).close();

            newStage.show();
        } catch (IOException e) {
            System.err.println("Error cargando FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openRegistroWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("CrearCuenta.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro usuarios");
            stage.setScene(new Scene(pane));
            stage.show();
            stage = (Stage) txtUsername.getScene().getWindow();
            stage.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }



}