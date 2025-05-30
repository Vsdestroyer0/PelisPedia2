package Controles;

import aplicacion.DAO.FavoritoDAO;
import aplicacion.DAO.FavoritoDAOImp;
import aplicacion.DAO.PeliculaDAO;
import aplicacion.DAO.PeliculaDAOImp;
import aplicacion.VO.PeliculaVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import aplicacion.application.SesionUsuario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FavoritosController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private TableView<PeliculaVO> tablaFavoritos;
    @FXML private TableColumn<PeliculaVO, ImageView> colImagen;
    @FXML private TableColumn<PeliculaVO, String> colTitulo;
    @FXML private TableColumn<PeliculaVO, String> colClasificacion;
    @FXML private TableColumn<PeliculaVO, String> colDescripcion;
    @FXML private TableColumn<PeliculaVO, Double> colPrecio;
    
    @FXML private Button btnVerDetalles;
    @FXML private Button btnEliminarFavorito;
    @FXML private Button btnRentarPelicula;
    @FXML private Button btnVolverMenu;
    
    private ObservableList<PeliculaVO> listaFavoritos = FXCollections.observableArrayList();
    private PeliculaDAO peliculaDAO = new PeliculaDAOImp();
    private FavoritoDAO favoritoDAO = new FavoritoDAOImp();
    private Image defaultMovieImage;
    
    // Usar el usuario de la sesión actual
    private int idUsuarioActual;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Obtener el usuario actual de la sesión
        if (SesionUsuario.getInstancia().haySesionActiva()) {
            idUsuarioActual = SesionUsuario.getInstancia().getUsuarioActual().getId();
        } else {
            // Si no hay sesión activa, mostrar un mensaje y volver al login
            Alertas.mostrarError("No hay una sesión activa. Inicie sesión para ver sus favoritos.");
            volverAlMenu();
            return;
        }
        
        // Cargar imagen por defecto
        defaultMovieImage = new Image(getClass().getResourceAsStream("/aplicacion/Imgs/Claqueta.jpg"));
        
        // Configurar la tabla
        setupTable();
        
        // Cargar las películas favoritas
        cargarFavoritos();
    }
    
    private void setupTable() {
        // Configurar columnas de la tabla
        colImagen.setCellValueFactory(cellData -> {
            PeliculaVO pelicula = cellData.getValue();
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
        
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        // Personalizar la columna de precio para mostrar formato de moneda
        colPrecio.setCellFactory(tc -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", precio));
                }
            }
        });
    }
    
    private void cargarFavoritos() {
        try {
            // Obtener favoritos desde la base de datos usando el DAO
            List<PeliculaVO> peliculas = favoritoDAO.obtenerFavoritos(idUsuarioActual);
            
            listaFavoritos.clear();
            listaFavoritos.addAll(peliculas);
            tablaFavoritos.setItems(listaFavoritos);
            
            if (peliculas.isEmpty()) {
                Alertas.mostrarAdvertencia("No tienes películas favoritas. Agrega algunas desde el catálogo.");
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error al cargar favoritos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleVerDetalles() {
        PeliculaVO peliculaSeleccionada = tablaFavoritos.getSelectionModel().getSelectedItem();
        
        if (peliculaSeleccionada != null) {
            try {
                // Abrir ventana de detalles
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("DetallePelicula.fxml"));
                Parent root = loader.load();
                
                // Si hay un controlador para detalles, pasar la película seleccionada
                // DetallePeliculaController controller = loader.getController();
                // controller.setPelicula(peliculaSeleccionada);
                
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Detalles de Película - " + peliculaSeleccionada.getTitulo());
                stage.show();
            } catch (IOException e) {
                // Si no existe el FXML, mostrar una alerta con los detalles
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Detalles de Película");
                alert.setHeaderText(peliculaSeleccionada.getTitulo());
                alert.setContentText("Clasificación: " + peliculaSeleccionada.getClasificacion() + 
                                   "\nPrecio: $" + peliculaSeleccionada.getPrecio() + 
                                   "\n\nDescripción: " + peliculaSeleccionada.getDescripcion());
                alert.showAndWait();
            }
        } else {
            Alertas.mostrarAdvertencia("Selecciona una película para ver detalles.");
        }
    }
    
    @FXML
    private void handleEliminarFavorito() {
        PeliculaVO peliculaSeleccionada = tablaFavoritos.getSelectionModel().getSelectedItem();
        
        if (peliculaSeleccionada != null) {
            // Confirmar eliminación
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar de Favoritos");
            confirmacion.setHeaderText("¿Estás seguro de eliminar esta película de tus favoritos?");
            confirmacion.setContentText("Película: " + peliculaSeleccionada.getTitulo());
            
            Optional<ButtonType> result = confirmacion.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Eliminar de la base de datos
                boolean eliminado = favoritoDAO.eliminarFavorito(idUsuarioActual, peliculaSeleccionada.getIdPelicula());
                
                if (eliminado) {
                    listaFavoritos.remove(peliculaSeleccionada);
                    Alertas.mostrarExito("Película eliminada de favoritos.");
                } else {
                    Alertas.mostrarError("No se pudo eliminar la película de favoritos.");
                }
            }
        } else {
            Alertas.mostrarAdvertencia("Selecciona una película para eliminar de favoritos.");
        }
    }
    
    @FXML
    private void handleRentarPelicula() {
        PeliculaVO peliculaSeleccionada = tablaFavoritos.getSelectionModel().getSelectedItem();
        
        if (peliculaSeleccionada != null) {
            // Confirmar alquiler
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Rentar Película");
            confirmacion.setHeaderText("¿Deseas rentar esta película?");
            confirmacion.setContentText("Película: " + peliculaSeleccionada.getTitulo() + 
                                      "\nPrecio: $" + peliculaSeleccionada.getPrecio());
            
            Optional<ButtonType> result = confirmacion.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // En un sistema real, aquí se crearía un ticket de renta
                Alertas.mostrarExito("Película rentada con éxito. Disfruta de " + 
                                  peliculaSeleccionada.getTitulo() + "!");
            }
        } else {
            Alertas.mostrarAdvertencia("Selecciona una película para rentar.");
        }
    }
    
    @FXML
    private void handleVolverMenu() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
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
    
    private void volverAlMenu() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
            stage.close();
            
            // Cargar el menú de usuario
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Login - PelisPedia");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
