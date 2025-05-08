package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RecuperarCuenta1Controller {
    @FXML private TextField txtCorreo;

    @FXML
    private void handleComprobarCorreo() {
        String correo = txtCorreo.getText().trim();

        // Validar formato de correo
        if (!correo.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            Alertas.mostrarError("Error, formato de correo inválido");
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAOImp();
        UsuarioVO usuario = usuarioDAO.obtenerPorCorreo(correo);

        if (usuario != null) {
            // Navegar a la siguiente pantalla y pasar la pregunta de seguridad
            abrirRecuperarContraseña2(usuario.getPreguntaSeguridad(), correo);
        } else {
            Alertas.mostrarError("Error, correo no registrado");
        }
    }

    private void abrirRecuperarContraseña2(String pregunta, String correo) {
        try {
            // Cargar la siguiente pantalla
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aplicacion/application/RecuperarContraseña2.fxml"));
            Parent root = loader.load();

            // Pasar datos al controlador de la siguiente pantalla
            RecuperarContraseña2Controller controller = loader.getController();
            controller.setDatos(pregunta, correo);

            // Mostrar la nueva ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar ventana actual
            Stage currentStage = (Stage) txtCorreo.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegresar() {
        try {
            // Cargar hello-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aplicacion/application/hello-view.fxml"));
            Parent root = loader.load();

            // Crear nueva escena
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar ventana actual
            Stage currentStage = (Stage) txtCorreo.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}