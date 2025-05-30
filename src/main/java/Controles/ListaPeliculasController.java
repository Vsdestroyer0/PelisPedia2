package Controles;

import aplicacion.DAO.PeliculaDAO;
import aplicacion.DAO.PeliculaDAOImp;
import aplicacion.DAO.CarritoDAO;
import aplicacion.DAO.CarritoDAOImp;
import aplicacion.DAO.FavoritoDAOImp;
import aplicacion.VO.PeliculaVO;
import aplicacion.VO.CarritoVO;
import aplicacion.VO.CarritoItemVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import aplicacion.application.SesionUsuario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListaPeliculasController implements Initializable {

    public Button btnVolver;
    // Componentes de la interfaz
    @FXML private TextField txtTitulo;
    @FXML private ComboBox<String> cmbClasificacion;
    @FXML private TextField txtPrecio;
    @FXML private TextArea DescripcionArea;
    @FXML private ImageView imgPreview;
    @FXML private Button btnSeleccionarImagen;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnAgregar;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnAgregarAlCarrito;
    @FXML private Button btnAgregarAFavoritos;
    @FXML private Button btnVerCarrito;
    @FXML private AnchorPane rootPane;

    @FXML private TableView<PeliculaVO> tablaPeliculas;
    @FXML private TableColumn<PeliculaVO, ImageView> colImagen;
    @FXML private TableColumn<PeliculaVO, String> colTitulo;
    @FXML private TableColumn<PeliculaVO, String> colClasificacion;
    @FXML private TableColumn<PeliculaVO, String> colDescripcion;
    @FXML private TableColumn<PeliculaVO, Double> colPrecio;

    // Variables para gestión de datos
    private ObservableList<PeliculaVO> lista = FXCollections.observableArrayList();
    private PeliculaDAO peliculaDAO = new PeliculaDAOImp();
    private byte[] imagenBytes;
    
    // Imagen por defecto para películas sin imagen
    private final Image defaultMovieImage = new Image(getClass().getResourceAsStream("/aplicacion/Imgs/Claqueta.jpg"));


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar la tabla
        setupTable();
        
        // Cargar clasificaciones en el combo box
        setupClasificaciones();
        
        // Cargar películas
        loadPeliculas();
        
        // Configurar los listeners para la selección de la tabla
        setupTableSelectionListener();

        // Verificar si el usuario es administrador
        boolean esAdmin = esUsuarioAdministrador();
        
        // Configurar visibilidad de botones según el tipo de usuario (solo si existen los botones)
        // Para usuarios normales: mostrar botones de carrito y favoritos
        if (btnAgregarAlCarrito != null) btnAgregarAlCarrito.setVisible(!esAdmin);
        if (btnAgregarAFavoritos != null) btnAgregarAFavoritos.setVisible(!esAdmin);
        if (btnVerCarrito != null) btnVerCarrito.setVisible(!esAdmin);
        
        // Ajustar el formulario según el tipo de usuario
        ajustarFormulario(esAdmin);
    }

    
    
    private void setupClasificaciones() {
        ObservableList<String> clasificaciones = FXCollections.observableArrayList(
            "A", "AA", "B", "B15", "C", "D"
        );
        cmbClasificacion.setItems(clasificaciones);
        cmbClasificacion.getSelectionModel().selectFirst();
    }

    private void setupTable() {
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
    
        // Personalizar la celda de precio para mostrar formato de moneda
        colPrecio.setCellFactory(tc -> new TableCell<>() {
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

    private void loadPeliculas() {
        try {
            List<PeliculaVO> peliculas = peliculaDAO.obtenerTodasLasPeliculas();
            lista.clear();
            lista.addAll(peliculas);
            tablaPeliculas.setItems(lista);
        } catch (Exception e) {
            Alertas.mostrarError("Error al cargar películas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupTableSelectionListener() {
        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Mostrar datos de la película seleccionada en los campos
                txtTitulo.setText(newSelection.getTitulo());
                cmbClasificacion.setValue(newSelection.getClasificacion());
                DescripcionArea.setText(newSelection.getDescripcion());
                txtPrecio.setText(String.format("%.2f", newSelection.getPrecio()));
                
                // Mostrar imagen
                if (newSelection.getImagen() != null) {
                    Image image = new Image(new ByteArrayInputStream(newSelection.getImagen()));
                    imgPreview.setImage(image);
                    imagenBytes = newSelection.getImagen();
                } else {
                    imgPreview.setImage(defaultMovieImage);
                    imagenBytes = null;
                }
            }
        });
    }
    
    @FXML
    private void handleSeleccionarImagen(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        Stage stage = (Stage) rootPane.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            try {
                // Convertir archivo a bytes
                FileInputStream fis = new FileInputStream(selectedFile);
                imagenBytes = fis.readAllBytes();
                fis.close();
                
                // Mostrar vista previa
                Image image = new Image(selectedFile.toURI().toString());
                imgPreview.setImage(image);
            } catch (IOException ex) {
                Alertas.mostrarError("Error al cargar la imagen: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleAgregarPelicula(ActionEvent e) {
        if (validarCampos()) {
            try {
                String titulo = txtTitulo.getText().trim();
                String clasificacion = cmbClasificacion.getValue();
                String descripcion = DescripcionArea.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                
                PeliculaVO pelicula = new PeliculaVO(imagenBytes, titulo, clasificacion, descripcion, precio);
                
                if (peliculaDAO.agregarPelicula(pelicula)) {
                    Alertas.mostrarExito("Película agregada exitosamente");
                    limpiarCampos();
                    loadPeliculas();
                } else {
                    Alertas.mostrarError("Error al agregar película");
                }
            } catch (NumberFormatException ex) {
                Alertas.mostrarError("El precio debe ser un número válido");
            } catch (Exception ex) {
                Alertas.mostrarError("Error al agregar película: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleModificarPelicula(ActionEvent e) {
        PeliculaVO peliculaSeleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        
        if (peliculaSeleccionada != null) {
            if (validarCampos()) {
                try {
                    String titulo = txtTitulo.getText().trim();
                    String clasificacion = cmbClasificacion.getValue();
                    String descripcion = DescripcionArea.getText().trim();
                    double precio = Double.parseDouble(txtPrecio.getText().trim());
                    
                    peliculaSeleccionada.setTitulo(titulo);
                    peliculaSeleccionada.setClasificacion(clasificacion);
                    peliculaSeleccionada.setDescripcion(descripcion);
                    peliculaSeleccionada.setPrecio(precio);
                    
                    if (imagenBytes != null) {
                        peliculaSeleccionada.setImagen(imagenBytes);
                    }
                    
                    if (peliculaDAO.actualizarPelicula(peliculaSeleccionada)) {
                        Alertas.mostrarExito("Película actualizada exitosamente");
                        loadPeliculas();
                    } else {
                        Alertas.mostrarError("Error al actualizar película");
                    }
                } catch (NumberFormatException ex) {
                    Alertas.mostrarError("El precio debe ser un número válido");
                } catch (Exception ex) {
                    Alertas.mostrarError("Error al actualizar película: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else {
            Alertas.mostrarError("Seleccione una película para modificar");
        }
    }
    
    @FXML
    private void handleEliminarPelicula(ActionEvent e) {
        PeliculaVO peliculaSeleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        
        if (peliculaSeleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro de eliminar esta película?");
            confirmacion.setContentText("Esta acción no se puede deshacer y eliminará la película: " + 
                                       peliculaSeleccionada.getTitulo());
            
            Optional<ButtonType> result = confirmacion.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    if (peliculaDAO.eliminarPelicula(peliculaSeleccionada.getIdPelicula())) {
                        Alertas.mostrarExito("Película eliminada exitosamente");
                        limpiarCampos();
                        loadPeliculas();
                    } else {
                        Alertas.mostrarError("Error al eliminar película");
                    }
                } catch (Exception ex) {
                    Alertas.mostrarError("Error al eliminar película: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else {
            Alertas.mostrarError("Seleccione una película para eliminar");
        }
    }
    
    private boolean validarCampos() {
        if (txtTitulo.getText().trim().isEmpty()) {
            Alertas.mostrarError("Debe ingresar un título para la película");
            return false;
        }
        
        if (cmbClasificacion.getValue() == null) {
            Alertas.mostrarError("Debe seleccionar una clasificación");
            return false;
        }
        
        if (txtPrecio.getText().trim().isEmpty()) {
            Alertas.mostrarError("Debe ingresar un precio para la película");
            return false;
        } else {
            try {
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                if (precio <= 0) {
                    Alertas.mostrarError("El precio debe ser mayor a 0");
                    return false;
                }
            } catch (NumberFormatException e) {
                Alertas.mostrarError("El precio debe ser un número válido");
                return false;
            }
        }
        
        return true;
    }
    
    private void limpiarCampos() {
        txtTitulo.clear();
        cmbClasificacion.getSelectionModel().selectFirst();
        DescripcionArea.clear();
        txtPrecio.setText("200.00");
        imgPreview.setImage(defaultMovieImage);
        imagenBytes = null;
        tablaPeliculas.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void handleCerrarSesion(ActionEvent e) {
        try {
            // Aplicar efecto de desenfoque a toda la ventana
            BoxBlur blur = new BoxBlur(3, 3, 3);
            rootPane.setEffect(blur);
            
            // Confirmar cierre de sesión
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Cerrar Sesión");
            confirmacion.setHeaderText("¿Está seguro de cerrar sesión?");
            confirmacion.setContentText("Se perderán los cambios no guardados.");
            
            // Personalizar botones
            ButtonType btnSi = new ButtonType("Sí");
            ButtonType btnNo = new ButtonType("No");
            confirmacion.getButtonTypes().setAll(btnSi, btnNo);
            
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            
            // Quitar efecto de desenfoque
            rootPane.setEffect(null);
            
            if (resultado.isPresent() && resultado.get() == btnSi) {
                // Cargar pantalla de login
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                if (loader.getLocation() == null) {
                    throw new IOException("No se pudo encontrar el archivo FXML: hello-view.fxml en la ruta esperada");
                }

                AnchorPane pane = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.initStyle(StageStyle.DECORATED);
                stage.setScene(new Scene(pane));
                
                // Cerrar ventana actual
                Stage currentStage = (Stage) rootPane.getScene().getWindow();
                currentStage.close();
                
                stage.show();
            }
        } catch (IOException ex) {
            Alertas.mostrarError("Error al cerrar sesión: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Verifica si el usuario actual es administrador
     * @return true si es administrador, false en caso contrario
     */
    private boolean esUsuarioAdministrador() {
        // Usar la clase SesionUsuario para verificar si el usuario es administrador
        return SesionUsuario.getInstancia().esUsuarioAdministrador();
    }

    /**
     * Maneja el evento de agregar una película al carrito
     */
    @FXML
    private void handleAgregarAlCarrito() {
        PeliculaVO peliculaSeleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        
        if (peliculaSeleccionada == null) {
            Alertas.mostrarAdvertencia("Seleccione una película para agregar al carrito");
            return;
        }
        
        try {
            // Verificar si hay un usuario logueado
            if (!SesionUsuario.getInstancia().haySesionActiva()) {
                Alertas.mostrarError("Debe iniciar sesión para agregar películas al carrito");
                return;
            }
            
            // Obtener el carrito del usuario
            CarritoDAO carritoDAO = new CarritoDAOImp();
            CarritoVO carrito = carritoDAO.obtenerCarritoUsuario(SesionUsuario.getInstancia().getUsuarioActual().getId());
            
            if (carrito == null) {
                Alertas.mostrarError("No se pudo obtener el carrito. Intente nuevamente.");
                return;
            }
            
            // Verificar si la película ya está en el carrito
            CarritoItemVO itemExistente = carritoDAO.buscarItemEnCarrito(carrito.getIdCarrito(), peliculaSeleccionada.getIdPelicula());
            
            if (itemExistente != null) {
                // Si ya existe, incrementar la cantidad
                itemExistente.setCantidad(itemExistente.getCantidad() + 1);
                if (carritoDAO.actualizarItemCarrito(itemExistente)) {
                    Alertas.mostrarExito("Se ha incrementado la cantidad en el carrito");
                } else {
                    Alertas.mostrarError("Error al actualizar el carrito");
                }
            } else {
                // Si no existe, agregar nuevo item
                CarritoItemVO nuevoItem = new CarritoItemVO(
                    carrito.getIdCarrito(),
                    peliculaSeleccionada.getIdPelicula(),
                    1, // Cantidad inicial
                    peliculaSeleccionada.getPrecio()
                );
                
                if (carritoDAO.agregarItemAlCarrito(nuevoItem)) {
                    Alertas.mostrarExito("Película agregada al carrito exitosamente");
                } else {
                    Alertas.mostrarError("Error al agregar película al carrito");
                }
            }
            
        } catch (Exception e) {
            Alertas.mostrarError("Error al agregar al carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Maneja el evento de agregar una película a favoritos
     */
    @FXML
    private void handleAgregarAFavoritos() {
        PeliculaVO peliculaSeleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        
        if (peliculaSeleccionada == null) {
            Alertas.mostrarAdvertencia("Seleccione una película para agregar a favoritos");
            return;
        }
        
        try {
            // Verificar si hay un usuario logueado
            if (!SesionUsuario.getInstancia().haySesionActiva()) {
                Alertas.mostrarError("Debe iniciar sesión para agregar películas a favoritos");
                return;
            }
            
            int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId();
            int idPelicula = peliculaSeleccionada.getIdPelicula();
            
            // Usar la implementación real de FavoritoDAO
            FavoritoDAOImp favoritoDAO = new FavoritoDAOImp();
            
            // Verificar si ya es favorito
            if (favoritoDAO.esFavorito(idUsuario, idPelicula)) {
                Alertas.mostrarAdvertencia("Esta película ya está en tus favoritos");
                return;
            }
            
            // Agregar a favoritos usando el DAO
            boolean exito = favoritoDAO.agregarFavorito(idUsuario, idPelicula);
            
            if (exito) {
                Alertas.mostrarExito("Película agregada a favoritos exitosamente");
            } else {
                Alertas.mostrarError("Error al agregar película a favoritos");
            }
            
        } catch (Exception e) {
            Alertas.mostrarError("Error al agregar a favoritos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Maneja el evento de ver el carrito
     */
    @FXML
    private void handleVerCarrito() {
        try {
            // Verificar si hay un usuario logueado
            if (!SesionUsuario.getInstancia().haySesionActiva()) {
                Alertas.mostrarError("Debe iniciar sesión para ver el carrito");
                return;
            }
            
            // Cargar la vista del carrito - Corregido path
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Carrito.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: Carrito.fxml");
            }
            
            Parent root = loader.load();
            
            // Si existe un controlador para el carrito, inicializarlo
            // CarritoController controller = loader.getController();
            // controller.inicializar(HelloApplication.usuarioLogueado);
            
            // Abrir en la ventana actual
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            Alertas.mostrarError("Error al abrir la vista de carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Método que simula agregar una película a favoritos
     * En un sistema real, esto se haría con un DAO específico para favoritos
     */
    private boolean agregarPeliculaAFavoritos(int idUsuario, int idPelicula) {
        // Simulación de éxito
        // En una implementación real, esto insertaría en una tabla de favoritos
        return true;
    }
    private void ajustarFormulario(boolean esAdmin) {
        // Ajustar visibilidad de campos según el tipo de usuario
        if (esAdmin) {
            // Administrador puede editar todos los campos
            txtTitulo.setEditable(true);
            cmbClasificacion.setDisable(false);
            txtPrecio.setEditable(true);
            DescripcionArea.setEditable(true);
            btnSeleccionarImagen.setVisible(true);
            
            // Asegurar que los botones de administración estén visibles
            if (btnAgregar != null) btnAgregar.setVisible(true);
            if (btnModificar != null) btnModificar.setVisible(true);
            if (btnEliminar != null) btnEliminar.setVisible(true);
            
            // Botones de usuario normal no visibles
            if (btnAgregarAlCarrito != null) btnAgregarAlCarrito.setVisible(false);
            if (btnAgregarAFavoritos != null) btnAgregarAFavoritos.setVisible(false);
            if (btnVerCarrito != null) btnVerCarrito.setVisible(false);
        } else {
            // Usuario normal no puede editar los campos
            txtTitulo.setEditable(false);
            cmbClasificacion.setDisable(true);
            txtPrecio.setEditable(false);
            DescripcionArea.setEditable(false);
            btnSeleccionarImagen.setVisible(false);
            
            // Botones de administración no visibles
            if (btnAgregar != null) btnAgregar.setVisible(false);
            if (btnModificar != null) btnModificar.setVisible(false);
            if (btnEliminar != null) btnEliminar.setVisible(false);
            
            // Botones de usuario normal visibles
            if (btnAgregarAlCarrito != null) btnAgregarAlCarrito.setVisible(true);
            if (btnAgregarAFavoritos != null) btnAgregarAFavoritos.setVisible(true);
            if (btnVerCarrito != null) btnVerCarrito.setVisible(true);
        }
    }

    @FXML
    private void handleVolverMenu() {
        try {
            // Cambiamos la ruta para que apunte al archivo correcto
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MenuUsuario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menú Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alertas.mostrarError("No se pudo cargar el menú principal: " + e.getMessage());
        }
    }
}
