package Controles;

import aplicacion.BaseDatos.BaseDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
    void handleLogin(ActionEvent event){
        String username =txtUsername.getText();
        String password = txtPassword.getText();

        validarCredenciales(username, password);
    }

    @FXML
    void validarCredenciales(String username, String password){
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

    @FXML
    void handleRegister(ActionEvent event){
        openRegistroWindow();
    }

    private void openAltaPlantaWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdministradorOpciones.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PanelUsuario.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Panel usuario");
            stage.setScene(new Scene(pane));
            Stage currentStage =(Stage) txtUsername.getScene().getWindow();
            currentStage.close();
            stage.show();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    private void openRegistroWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearCuenta.fxml"));
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