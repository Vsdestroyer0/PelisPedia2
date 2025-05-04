package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RecuperarContraseña2Controller {
    @FXML private Label lblPregunta;
    @FXML private TextField txtRespuesta;

    private String correoUsuario;

    // Recibe datos de la pantalla anterior
    public void setDatos(String pregunta, String correo) {
        lblPregunta.setText(pregunta);
        this.correoUsuario = correo;
    }

    @FXML
    private void handleValidarRespuesta() {
        String respuesta = txtRespuesta.getText().trim();

        if (respuesta.isEmpty()) {
            mostrarAlerta("Error", "Ingrese la respuesta de seguridad", Alert.AlertType.ERROR);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAOImp();
        UsuarioVO usuario = usuarioDAO.obtenerPorCorreo(correoUsuario);

        if (usuario != null && usuario.getRespuestaSeguridad().equals(respuesta)) {
            // Navegar a pantalla de nueva contraseña
            abrirNuevaContraseña3(correoUsuario);
        } else {
            mostrarAlerta("Error", "Respuesta incorrecta", Alert.AlertType.ERROR);
        }
    }

    private void abrirNuevaContraseña3(String correo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aplicacion/application/NuevaContraseña3.fxml"));
            Parent root = loader.load();

            // Pasar el correo a la siguiente pantalla
            NuevaContraseña3Controller controller = loader.getController();
            controller.setCorreoUsuario(correo);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar ventana actual
            Stage currentStage = (Stage) lblPregunta.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}