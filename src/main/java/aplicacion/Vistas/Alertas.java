package aplicacion.Vistas;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;

public class Alertas {

    // Rutas corregidas (agregar "aplicacion" al inicio)
        private static final String ICONO_EXITO = "/aplicacion/Imgs/exito_icon.png";
        private static final String ICONO_ERROR = "/aplicacion/Imgs/error_icon.png";
        private static final String ICONO_ADVERTENCIA = "/aplicacion/Imgs/advertencia_icon.png";


    // Alerta de Éxito con imagen
    public static void mostrarExito(String mensaje) {
        Alert alert = crearAlerta(AlertType.INFORMATION, "Éxito", mensaje, ICONO_EXITO);
        alert.showAndWait();
    }

    // Alerta de Error con imagen
    public static void mostrarError(String mensaje) {
        Alert alert = crearAlerta(AlertType.ERROR, "Error", mensaje, ICONO_ERROR);
        alert.showAndWait();
    }

    // Alerta de Advertencia con imagen
    public static void mostrarAdvertencia(String mensaje) {
        Alert alert = crearAlerta(AlertType.WARNING, "Advertencia", mensaje, ICONO_ADVERTENCIA);
        alert.showAndWait();
    }

    private static Alert crearAlerta(AlertType tipo, String titulo, String mensaje, String rutaIcono) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        try {
            // Cargar imagen de forma segura
            InputStream inputStream = Alertas.class.getResourceAsStream(rutaIcono);
            if (inputStream != null) {
                ImageView icono = new ImageView(new Image(inputStream));
                icono.setFitWidth(50);
                icono.setFitHeight(50);
                alert.setGraphic(icono);
            } else {
                System.err.println("Error: No se encontró el recurso " + rutaIcono);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }

        return alert;
    }
}