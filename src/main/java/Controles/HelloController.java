package Controles;

import aplicacion.BaseDatos.DatabaseConnection;
import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
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

    @FXML
    void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        UsuarioDAO usuarioDAO = new UsuarioDAOImp();
        UsuarioVO usuario = usuarioDAO.autenticarUsuario(username, password);

        if (usuario != null) {
            // Login exitoso
            if (usuarioDAO.esAdmin(username)) {
                // Abrir ventana de administrador
                //abrirVentanaAdmin();
            } else {
                // Abrir ventana de usuario normal
                //abrirVentanaUsuario();
            }
        } else {
            // Mostrar mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Login");
            alert.setHeaderText(null);
            alert.setContentText("Usuario o contraseña incorrectos");
            alert.showAndWait();
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
    private void openAltaPeliculas(){
        try{
            // Usar la ruta absoluta desde la raíz del classpath
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("aplicacion/application/CrearCuenta.fxml"));

            if (loader.getLocation() == null) {
                // Si no funciona, intentar con una ruta relativa al paquete de HelloApplication
                loader = new FXMLLoader(HelloApplication.class.getResource("CrearCuenta.fxml"));
            }

            if (loader.getLocation() == null) {
                // Si aún no funciona, intentar con una ruta absoluta desde la raíz
                loader = new FXMLLoader(getClass().getClassLoader().getResource("/aplicacion/application/CrearCuenta.fxml"));
            }

            if (loader.getLocation() == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: CrearCuenta.fxml en la ruta esperada");
            }

            System.out.println("Archivo FXML encontrado en: " + loader.getLocation());

            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro usuarios");
            stage.setScene(new Scene(pane));

            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception e){
            System.out.println("Error al abrir ventana de registro: " + e.getMessage());
            e.printStackTrace();

            // Mostrar diálogo de error con información detallada
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar la ventana de registro");
            alert.setContentText("Error: " + e.getMessage() +
                    "\n\nRuta del archivo esperada: aplicacion/application/CrearCuenta.fxml" +
                    "\nVerifique que el archivo existe y tiene los permisos correctos.");
            alert.showAndWait();
        }
    }


    @FXML
    private void openUsuarioWindow(){
        try{
            // Usar la ruta absoluta desde la raíz del classpath
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("aplicacion/application/CrearCuenta.fxml"));

            if (loader.getLocation() == null) {
                // Si no funciona, intentar con una ruta relativa al paquete de HelloApplication
                loader = new FXMLLoader(HelloApplication.class.getResource("CrearCuenta.fxml"));
            }

            if (loader.getLocation() == null) {
                // Si aún no funciona, intentar con una ruta absoluta desde la raíz
                loader = new FXMLLoader(getClass().getClassLoader().getResource("/aplicacion/application/CrearCuenta.fxml"));
            }

            if (loader.getLocation() == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: CrearCuenta.fxml en la ruta esperada");
            }

            System.out.println("Archivo FXML encontrado en: " + loader.getLocation());

            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro usuarios");
            stage.setScene(new Scene(pane));

            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception e){
            System.out.println("Error al abrir ventana de registro: " + e.getMessage());
            e.printStackTrace();

            // Mostrar diálogo de error con información detallada
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar la ventana de registro");
            alert.setContentText("Error: " + e.getMessage() +
                    "\n\nRuta del archivo esperada: aplicacion/application/CrearCuenta.fxml" +
                    "\nVerifique que el archivo existe y tiene los permisos correctos.");
            alert.showAndWait();
        }
    }

    @FXML
    private void openRegistroWindow(){
        try{
            // Usar la ruta absoluta desde la raíz del classpath
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("aplicacion/application/CrearCuenta.fxml"));

            if (loader.getLocation() == null) {
                // Si no funciona, intentar con una ruta relativa al paquete de HelloApplication
                loader = new FXMLLoader(HelloApplication.class.getResource("CrearCuenta.fxml"));
            }

            if (loader.getLocation() == null) {
                // Si aún no funciona, intentar con una ruta absoluta desde la raíz
                loader = new FXMLLoader(getClass().getClassLoader().getResource("/aplicacion/application/CrearCuenta.fxml"));
            }

            if (loader.getLocation() == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: CrearCuenta.fxml en la ruta esperada");
            }

            System.out.println("Archivo FXML encontrado en: " + loader.getLocation());

            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro usuarios");
            stage.setScene(new Scene(pane));

            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception e){
            System.out.println("Error al abrir ventana de registro: " + e.getMessage());
            e.printStackTrace();

            // Mostrar diálogo de error con información detallada
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar la ventana de registro");
            alert.setContentText("Error: " + e.getMessage() +
                    "\n\nRuta del archivo esperada: aplicacion/application/CrearCuenta.fxml" +
                    "\nVerifique que el archivo existe y tiene los permisos correctos.");
            alert.showAndWait();
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

            System.out.println("Archivo FXML encontrado en: " + loader.getLocation());

            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Recuperar Contraseña");
            stage.setScene(new Scene(pane));

            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception e){
            System.out.println("Error al abrir ventana de recuperación: " + e.getMessage());
            e.printStackTrace();

            // Mostrar diálogo de error con información detallada
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar la ventana de recuperación");
            alert.setContentText("Error: " + e.getMessage() +
                    "\n\nRuta del archivo esperada: aplicacion/application/RecuperarCuenta1.fxml" +
                    "\nVerifique que el archivo existe y tiene los permisos correctos.");
            alert.showAndWait();
        }
    }


}