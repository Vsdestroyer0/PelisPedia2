package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        lblPregunta.setText(pregunta); // Asignar texto al Label
        this.correoUsuario = correo;
    }


    @FXML
    private void handleValidarRespuesta() {
        String respuesta = txtRespuesta.getText().trim();

        if (respuesta.isEmpty()) {
            Alertas.mostrarError("Error, ingrese la respuesta de seguridad");
            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAOImp();
            UsuarioVO usuario = usuarioDAO.obtenerPorCorreo(correoUsuario);

            if (usuario != null && usuario.getRespuestaSeguridad().equals(respuesta)) {
                // Mostrar mensaje de éxito
                Alertas.mostrarExito("Respuesta correcta. Ahora puede crear una nueva contraseña.");
                
                // Navegar a pantalla de nueva contraseña
                abrirNuevaContraseña3(correoUsuario);
            } else {
                Alertas.mostrarError("Error, la respuesta de seguridad no coincide");
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error al validar la respuesta: " + e.getMessage());
            e.printStackTrace();
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
            stage.setTitle("Recuperación de cuenta - Nueva contraseña");
            stage.show();

            // Cerrar ventana actual
            Stage currentStage = (Stage) lblPregunta.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            Alertas.mostrarError("Error al abrir la pantalla de nueva contraseña: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegresar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aplicacion/application/RecuperarCuenta1.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Recuperación de cuenta - Correo");
            stage.show();

            Stage currentStage = (Stage) lblPregunta.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            Alertas.mostrarError("Error al regresar a la pantalla anterior: " + e.getMessage());
            e.printStackTrace();
        }
    }
}