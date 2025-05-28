package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
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

public class ListaUsuariosController implements Initializable {

    // Componentes de la interfaz
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;
    @FXML private ImageView imgPreview;
    @FXML private Button btnSeleccionarImagen;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnAgregar;
    @FXML private Button btnCerrarSesion;
    @FXML private AnchorPane rootPane;
    @FXML private CheckBox chkActivo;

    @FXML private TableView<UsuarioVO> tablaUsuarios;
    @FXML private TableColumn<UsuarioVO, ImageView> colImagen;
    @FXML private TableColumn<UsuarioVO, String> colNombre;
    @FXML private TableColumn<UsuarioVO, String> colCorreo;
    @FXML private TableColumn<UsuarioVO, String> colDireccion;
    @FXML private TableColumn<UsuarioVO, Boolean> colActivo;

    // Variables para gestión de datos
    private ObservableList<UsuarioVO> lista = FXCollections.observableArrayList();
    private UsuarioDAO usuarioDAO = new UsuarioDAOImp();
    private byte[] imagenBytes;
    
    // Imagen por defecto para usuarios sin imagen
    private final Image defaultUserImage = new Image(getClass().getResourceAsStream("/aplicacion/Imgs/LogoInicio.png"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar la tabla
        setupTable();

        // Cargar usuarios (excluyendo administradores)
        loadUsuarios();
        
        // Manejar la selección de un usuario en la tabla
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Mostrar datos del usuario seleccionado en los campos
                txtNombre.setText(newSelection.getNombre());
                txtCorreo.setText(newSelection.getCorreo());
                txtDireccion.setText(newSelection.getDireccion());
                chkActivo.setSelected(newSelection.isActivo());
                
                // Cargar imagen si existe
                if (newSelection.getImagen() != null) {
                    try {
                        imagenBytes = newSelection.getImagen();
                        Image image = new Image(new ByteArrayInputStream(imagenBytes));
                        imgPreview.setImage(image);
                    } catch (Exception e) {
                        System.err.println("Error al cargar imagen: " + e.getMessage());
                        imgPreview.setImage(defaultUserImage);
                        imagenBytes = null;
                    }
                } else {
                    imgPreview.setImage(defaultUserImage);
                    imagenBytes = null;
                }
            }
        });
    }

    private void setupTable() {
        // Configurar columnas de la tabla
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        
        // Formatear cómo se muestra la columna de activo
        colActivo.setCellFactory(col -> new TableCell<UsuarioVO, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Activo" : "Inactivo");
                }
            }
        });
        
        // Configurar la columna de imagen
        colImagen.setCellValueFactory(cellData -> {
            UsuarioVO usuario = cellData.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            
            if (usuario.getImagen() != null) {
                try {
                    Image img = new Image(new ByteArrayInputStream(usuario.getImagen()), 50, 50, true, true);
                    imageView.setImage(img);
                } catch (Exception e) {
                    System.err.println("Error cargando imagen: " + e.getMessage());
                    imageView.setImage(defaultUserImage);
                }
            } else {
                // Imagen por defecto si no hay datos de imagen
                imageView.setImage(defaultUserImage);
            }
            
            return new SimpleObjectProperty<>(imageView);
        });
        
        // Asignar lista de datos a la tabla
        tablaUsuarios.setItems(lista);
    }

    private void loadUsuarios() {
        try {
            lista.clear();
            List<UsuarioVO> datos = usuarioDAO.listarUsuarios();
            System.out.println("Usuarios obtenidos de la BD: " + datos.size());
            lista.addAll(datos);
        } catch (SQLException e) {
            Alertas.mostrarError("No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    @FXML
    private void handleAgregarUsuario(ActionEvent event) {
        try {
            // Cargar la vista de CrearCuenta.fxml
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("CrearCuenta.fxml"));
            Parent root = loader.load();
            
            // Configurar la escena
            Stage stage = (Stage) btnAgregar.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Crear Nuevo Usuario - PelisPedia");
            stage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al abrir la pantalla de creación de usuario: " + e.getMessage());
        }
    }

    @FXML
    private void handleSeleccionarImagen(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File f = fc.showOpenDialog(btnSeleccionarImagen.getScene().getWindow());
        if (f != null) {
            try {
                // Leer el archivo como bytes para almacenar en LONGBLOB
                try (FileInputStream fis = new FileInputStream(f)) {
                    imagenBytes = new byte[(int) f.length()];
                    fis.read(imagenBytes);
                }
                
                // Mostrar la imagen seleccionada
                imgPreview.setImage(new Image(f.toURI().toString()));
            } catch (IOException ex) {
                Alertas.mostrarError("Error al leer la imagen: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void handleModificarUsuario(ActionEvent e) {
        UsuarioVO sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel != null) {
            try {
                // Validar datos con manejo de nulos
                String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
                String correo = txtCorreo.getText() != null ? txtCorreo.getText().trim() : "";
                String direccion = txtDireccion.getText() != null ? txtDireccion.getText().trim() : "";
                boolean activo = chkActivo.isSelected();
                
                if (nombre.isEmpty() || correo.isEmpty() || direccion.isEmpty()) {
                    Alertas.mostrarError("Todos los campos son obligatorios. Por favor complete la información.");
                    return;
                }
                
                // Confirmar modificación
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar modificación");
                confirmacion.setHeaderText("¿Está seguro de modificar este usuario?");
                confirmacion.setContentText("Esta acción actualizará los datos del usuario " + sel.getNombre());
                
                Optional<ButtonType> result = confirmacion.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    sel.setNombre(nombre);
                    sel.setCorreo(correo);
                    sel.setDireccion(direccion);
                    sel.setActivo(activo);
                    
                    // Solo actualizar la imagen si se ha seleccionado una nueva
                    if (imagenBytes != null) {
                        sel.setImagen(imagenBytes);
                    }
                    
                    if (usuarioDAO.actualizarUsuario(sel)) {
                        tablaUsuarios.refresh();
                        clearForm();
                        Alertas.mostrarExito("Usuario modificado correctamente.");
                        // Recargar la lista para reflejar los cambios
                        loadUsuarios();
                    } else {
                        Alertas.mostrarError("Error al actualizar usuario. Verifique la conexión a la base de datos.");
                    }
                }
            } catch (Exception ex) {
                Alertas.mostrarError("Error durante la modificación: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            Alertas.mostrarError("Selecciona un usuario para modificar.");
        }
    }

    @FXML
    private void handleEliminarUsuario(ActionEvent e) {
        UsuarioVO sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel != null) {
            // Confirmar eliminación
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro de eliminar este usuario?");
            confirmacion.setContentText("Esta acción eliminará permanentemente el usuario " + sel.getNombre() + 
                                      " con correo " + sel.getCorreo() + ".\nEsta operación no se puede deshacer.");
            
            Optional<ButtonType> result = confirmacion.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    if (usuarioDAO.eliminarUsuario(sel.getId())) {
                        lista.remove(sel);
                        clearForm();
                        Alertas.mostrarExito("Usuario eliminado correctamente.");
                    } else {
                        Alertas.mostrarError("Error al eliminar el usuario. Verifique la conexión a la base de datos.");
                    }
                } catch (Exception ex) {
                    Alertas.mostrarError("Error durante la eliminación: " + ex.getMessage());
                }
            }
        } else {
            Alertas.mostrarError("Selecciona un usuario válido para eliminar.");
        }
    }

    @FXML
    private void handleActivarDesactivarUsuario(ActionEvent e) {
        UsuarioVO usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            try {
                // Cambiar el estado actual del usuario (activar/desactivar)
                boolean nuevoEstado = !usuarioSeleccionado.isActivo();
                
                // Confirmar la acción con el usuario
                String accion = nuevoEstado ? "activar" : "desactivar";
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar cambio de estado");
                confirmacion.setHeaderText("¿Está seguro de " + accion + " este usuario?");
                confirmacion.setContentText("Esta acción " + accion + "á al usuario " + usuarioSeleccionado.getNombre() + 
                                          " con correo " + usuarioSeleccionado.getCorreo());
                
                Optional<ButtonType> result = confirmacion.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Actualizar el estado del usuario
                    usuarioSeleccionado.setActivo(nuevoEstado);
                    
                    // Guardar el cambio en la base de datos
                    if (usuarioDAO.actualizarUsuario(usuarioSeleccionado)) {
                        // Actualizar la tabla
                        tablaUsuarios.refresh();
                        
                        // Actualizar el CheckBox si el usuario que se está modificando está seleccionado
                        if (chkActivo != null) {
                            chkActivo.setSelected(nuevoEstado);
                        }
                        
                        Alertas.mostrarExito("Usuario " + (nuevoEstado ? "activado" : "desactivado") + " correctamente.");
                        
                        // Recargar la lista para reflejar los cambios
                        loadUsuarios();
                    } else {
                        Alertas.mostrarError("Error al " + accion + " el usuario. Verifique la conexión a la base de datos.");
                    }
                }
            } catch (Exception ex) {
                Alertas.mostrarError("Error durante la modificación: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            Alertas.mostrarError("Selecciona un usuario para " + (usuarioSeleccionado != null && usuarioSeleccionado.isActivo() ? "desactivar" : "activar") + ".");
        }
    }

    @FXML
    private void handleCerrarSesion(ActionEvent e) {
        try {
            // Aplicar efecto de desenfoque a toda la ventana
            BoxBlur blur = new BoxBlur();
            blur.setWidth(10);
            blur.setHeight(10);
            blur.setIterations(3);
            
            // Guardar la referencia a la escena actual
            Scene currentScene = btnCerrarSesion.getScene();
            
            // Aplicar el efecto a la raíz de la escena
            currentScene.getRoot().setEffect(blur);
            
            // Cargar el diálogo de confirmación de cierre de sesión
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("LogoutDialog.fxml"));
            Parent root = loader.load();
            LogoutDialogController controller = loader.getController();
            
            // Configurar y mostrar el diálogo como una ventana modal
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.initOwner(((Button) e.getSource()).getScene().getWindow());
            
            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            
            // Quitar el efecto de desenfoque
            currentScene.getRoot().setEffect(null);
            
            // Verificar la respuesta del diálogo
            if (controller.isConfirmed()) {
                // Si el usuario confirmó, cerrar la sesión y volver a la pantalla de inicio
                abrirPantallaInicio();
            }
        } catch (IOException ex) {
            Alertas.mostrarError("Error al abrir el diálogo de cierre de sesión: " + ex.getMessage());
        }
    }
    
    private void abrirPantallaInicio() {
        try {
            // Cargar la pantalla de inicio
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Parent root = loader.load();
            
            // Configurar la escena y la ventana
            Stage stage = (Stage) tablaUsuarios.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            stage.setScene(scene);
            stage.setTitle("Inicio - PelisPedia");
            stage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al abrir la pantalla de inicio: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtNombre.clear();
        txtCorreo.clear();
        txtDireccion.clear();
        imgPreview.setImage(defaultUserImage);
        imagenBytes = null;
        chkActivo.setSelected(true);
        tablaUsuarios.getSelectionModel().clearSelection();
    }
}
