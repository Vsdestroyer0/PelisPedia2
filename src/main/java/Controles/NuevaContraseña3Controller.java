package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class NuevaContraseña3Controller {
    @FXML private PasswordField txtNuevaContraseña;
    @FXML private PasswordField txtConfirmarContraseña;

    private String correoUsuario;

    // Recibe el correo de la pantalla anterior
    public void setCorreoUsuario(String correo) {
        this.correoUsuario = correo;
    }

    @FXML
    private void handleCambiarContraseña() {
        String nuevaContraseña = txtNuevaContraseña.getText().trim();
        String confirmacion = txtConfirmarContraseña.getText().trim();

        // Validaciones
        if (nuevaContraseña.isEmpty() || confirmacion.isEmpty()) {
            mostrarAlerta("Error", "Complete todos los campos", Alert.AlertType.ERROR);
            return;
        }

        if (!nuevaContraseña.equals(confirmacion)) {
            mostrarAlerta("Error", "Las contraseñas no coinciden", Alert.AlertType.ERROR);
            return;
        }

        if (nuevaContraseña.length() < 8) {
            mostrarAlerta("Error", "Mínimo 8 caracteres", Alert.AlertType.ERROR);
            return;
        }

        // Actualizar en la base de datos
        UsuarioDAO usuarioDAO = new UsuarioDAOImp();
        UsuarioVO usuario = usuarioDAO.obtenerPorCorreo(correoUsuario);

        if (usuario != null) {
            usuario.setContraseña(nuevaContraseña);
            usuario.setConfirmarContraseña(confirmacion);

            if (usuarioDAO.actualizarUsuario(usuario)) {
                mostrarAlerta("Éxito", "Contraseña actualizada", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                mostrarAlerta("Error", "Error al guardar", Alert.AlertType.ERROR);
            }
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNuevaContraseña.getScene().getWindow();
        stage.close();
        handleRegresar();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @FXML
    private void handleRegresar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aplicacion/application/hello-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) txtNuevaContraseña.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}