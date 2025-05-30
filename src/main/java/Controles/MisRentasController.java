package Controles;

import aplicacion.DAO.RentaDAO;
import aplicacion.DAO.RentaDAOImp;
import aplicacion.VO.PeliculaRentadaVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import aplicacion.application.SesionUsuario;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MisRentasController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private StackPane emptyMessage;
    @FXML private TableView<PeliculaRentadaVO> tablaRentas;
    @FXML private TableColumn<PeliculaRentadaVO, ImageView> colImagen;
    @FXML private TableColumn<PeliculaRentadaVO, String> colTitulo;
    @FXML private TableColumn<PeliculaRentadaVO, String> colClasificacion;
    @FXML private TableColumn<PeliculaRentadaVO, String> colFechaRenta;
    @FXML private TableColumn<PeliculaRentadaVO, String> colTiempoRestante;
    @FXML private TableColumn<PeliculaRentadaVO, String> colEstado;
    @FXML private Button btnVerPelicula;
    @FXML private Button btnActualizar;
    @FXML private Button btnVolverMenu;
    @FXML private Label lblProximaActualizacion;
    
    private RentaDAO rentaDAO;
    private ObservableList<PeliculaRentadaVO> listaRentas;
    private Timeline actualizacionAutomatica;
    private int segundosParaActualizacion = 60;
    private Image defaultMovieImage;
    private DateTimeFormatter formateadorFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar DAO y lista
        rentaDAO = new RentaDAOImp();
        listaRentas = FXCollections.observableArrayList();
        
        // Cargar imagen por defecto
        defaultMovieImage = new Image(getClass().getResourceAsStream("/aplicacion/Imgs/Claqueta.jpg"));
        
        // Configurar tabla
        setupTable();
        
        // Cargar datos iniciales
        cargarRentas();
        
        // Iniciar temporizador de actualización automática
        iniciarActualizacionAutomatica();
        
        // Configurar botones
        btnVerPelicula.setDisable(true);
        
        // Listener para habilitar/deshabilitar botones según la selección
        tablaRentas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnVerPelicula.setDisable(newSelection == null);
        });
    }
    
    private void setupTable() {
        // Columna de imagen
        colImagen.setCellValueFactory(cellData -> {
            PeliculaRentadaVO pelicula = cellData.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            
            if (pelicula.getImagen() != null) {
                Image image = new Image(new ByteArrayInputStream(pelicula.getImagen()));
                imageView.setImage(image);
            } else {
                imageView.setImage(defaultMovieImage);
            }
            
            return new SimpleObjectProperty<>(imageView);
        });
        
        // Columna de título
        colTitulo.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTitulo()));
        
        // Columna de clasificación
        colClasificacion.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getClasificacion()));
        
        // Columna de fecha de renta
        colFechaRenta.setCellValueFactory(cellData -> {
            LocalDateTime fechaRenta = cellData.getValue().getFechaRenta();
            if (fechaRenta != null) {
                return new SimpleStringProperty(fechaRenta.format(formateadorFecha));
            }
            return new SimpleStringProperty("N/A");
        });
        
        // Columna de tiempo restante
        colTiempoRestante.setCellValueFactory(cellData -> {
            PeliculaRentadaVO renta = cellData.getValue();
            if ("rentada".equals(renta.getEstado())) {
                long minutos = renta.getTiempoRestanteMinutos();
                if (minutos <= 0) {
                    return new SimpleStringProperty("Expirada");
                }
                long horas = minutos / 60;
                long minutosRestantes = minutos % 60;
                return new SimpleStringProperty(String.format("%d:%02d", horas, minutosRestantes));
            }
            return new SimpleStringProperty("N/A");
        });
        
        // Columna de estado
        colEstado.setCellValueFactory(cellData -> {
            String estado = cellData.getValue().getEstado();
            String textoEstado;
            
            switch (estado) {
                case "rentada":
                    textoEstado = "Activa";
                    break;
                case "devuelta":
                    textoEstado = "Devuelta";
                    break;
                case "expirada":
                    textoEstado = "Expirada";
                    break;
                default:
                    textoEstado = estado;
            }
            
            return new SimpleStringProperty(textoEstado);
        });
    }
    
    private void cargarRentas() {
        // Verificar si hay usuario logueado
        if (!SesionUsuario.getInstancia().haySesionActiva()) {
            Alertas.mostrarError("No hay una sesión activa. Inicie sesión para ver sus películas rentadas.");
            handleVolverMenu();
            return;
        }
        
        // Actualizar las rentas expiradas en la base de datos
        int rentasActualizadas = rentaDAO.actualizarRentasExpiradas();
        if (rentasActualizadas > 0) {
            System.out.println(rentasActualizadas + " rentas han sido marcadas como expiradas");
        }
        
        // Obtener el ID del usuario actual
        int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId();
        
        // Cargar rentas desde la base de datos
        List<PeliculaRentadaVO> rentas = rentaDAO.obtenerRentasPorUsuario(idUsuario);
        
        // Actualizar la lista observable
        listaRentas.clear();
        listaRentas.addAll(rentas);
        
        // Asignar la lista a la tabla
        tablaRentas.setItems(listaRentas);
        
        // Mostrar mensaje si no hay rentas
        emptyMessage.setVisible(listaRentas.isEmpty());
        tablaRentas.setVisible(!listaRentas.isEmpty());
    }
    
    private void iniciarActualizacionAutomatica() {
        // Detener cualquier temporizador anterior
        if (actualizacionAutomatica != null) {
            actualizacionAutomatica.stop();
        }
        
        // Reiniciar contador
        segundosParaActualizacion = 60;
        actualizarLabelContador();
        
        // Crear nuevo temporizador
        actualizacionAutomatica = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                segundosParaActualizacion--;
                
                // Actualizar el contador en la interfaz
                actualizarLabelContador();
                
                // Si llega a cero, recargar datos y reiniciar contador
                if (segundosParaActualizacion <= 0) {
                    cargarRentas();
                    segundosParaActualizacion = 60;
                }
                
                // También actualizar la tabla para refrescar el tiempo restante
                tablaRentas.refresh();
            })
        );
        
        // Configurar para que se ejecute indefinidamente
        actualizacionAutomatica.setCycleCount(Animation.INDEFINITE);
        actualizacionAutomatica.play();
    }
    
    private void actualizarLabelContador() {
        Platform.runLater(() -> {
            lblProximaActualizacion.setText("Próxima actualización: " + segundosParaActualizacion + " segundos");
        });
    }
    
    @FXML
    private void handleVerPelicula() {
        PeliculaRentadaVO rentaSeleccionada = tablaRentas.getSelectionModel().getSelectedItem();
        
        if (rentaSeleccionada != null) {
            if (!"rentada".equals(rentaSeleccionada.getEstado()) || rentaSeleccionada.isExpirado()) {
                Alertas.mostrarAdvertencia("Esta película ya no está disponible para visualización.");
                return;
            }
            
            try {
                // En un sistema real, aquí se abriría el reproductor de la película
                // Por ahora, solo mostraremos información detallada
                
                // Crear una ventana con detalles de la película
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Reproducción - " + rentaSeleccionada.getTitulo());
                
                // Crear contenido
                AnchorPane root = new AnchorPane();
                root.setPrefSize(600, 400);
                
                // Imagen
                ImageView imageView = new ImageView();
                if (rentaSeleccionada.getImagen() != null) {
                    imageView.setImage(new Image(new ByteArrayInputStream(rentaSeleccionada.getImagen())));
                } else {
                    imageView.setImage(defaultMovieImage);
                }
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setLayoutX(30);
                imageView.setLayoutY(30);
                
                // Información
                Label titulo = new Label("Título: " + rentaSeleccionada.getTitulo());
                titulo.setLayoutX(250);
                titulo.setLayoutY(30);
                titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
                
                Label clasificacion = new Label("Clasificación: " + rentaSeleccionada.getClasificacion());
                clasificacion.setLayoutX(250);
                clasificacion.setLayoutY(70);
                clasificacion.setStyle("-fx-font-size: 18px;");
                
                LocalDateTime fechaRenta = rentaSeleccionada.getFechaRenta();
                String fechaStr = fechaRenta != null ? fechaRenta.format(formateadorFecha) : "N/A";
                Label fecha = new Label("Rentada el: " + fechaStr);
                fecha.setLayoutX(250);
                fecha.setLayoutY(110);
                fecha.setStyle("-fx-font-size: 16px;");
                
                long minutos = rentaSeleccionada.getTiempoRestanteMinutos();
                long horas = minutos / 60;
                long minutosRestantes = minutos % 60;
                Label tiempoRestante = new Label(
                    "Tiempo restante: " + String.format("%d horas, %02d minutos", horas, minutosRestantes));
                tiempoRestante.setLayoutX(250);
                tiempoRestante.setLayoutY(150);
                tiempoRestante.setStyle("-fx-font-size: 16px;");
                
                // Mensaje de reproducción
                Label reproduccion = new Label("Reproduciendo película...");
                reproduccion.setLayoutX(30);
                reproduccion.setLayoutY(250);
                reproduccion.setStyle("-fx-font-size: 20px; -fx-font-style: italic;");
                
                // Botón para cerrar
                Button btnCerrar = new Button("Cerrar");
                btnCerrar.setLayoutX(250);
                btnCerrar.setLayoutY(300);
                btnCerrar.setPrefSize(100, 30);
                btnCerrar.setOnAction(e -> stage.close());
                
                // Añadir componentes
                root.getChildren().addAll(imageView, titulo, clasificacion, fecha, tiempoRestante, reproduccion, btnCerrar);
                
                // Configurar escena
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();
                
            } catch (Exception e) {
                Alertas.mostrarError("Error al reproducir la película: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Alertas.mostrarAdvertencia("Seleccione una película para ver");
        }
    }
    
    @FXML
    private void handleActualizar() {
        cargarRentas();
        iniciarActualizacionAutomatica();
    }
    
    @FXML
    private void handleVolverMenu() {
        try {
            // Detener el temporizador
            if (actualizacionAutomatica != null) {
                actualizacionAutomatica.stop();
            }
            
            // Cerrar la ventana actual
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
            
            // Cargar el menú de usuario
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MenuUsuario.fxml"));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Menú Usuario - PelisPedia");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método para detener los temporizadores cuando se cierra la ventana
    public void detenerTemporizadores() {
        if (actualizacionAutomatica != null) {
            actualizacionAutomatica.stop();
        }
    }
}
