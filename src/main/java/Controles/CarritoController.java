package Controles;

import aplicacion.DAO.CarritoDAO;
import aplicacion.DAO.CarritoDAOImp;
import aplicacion.DAO.RentaDAO;
import aplicacion.DAO.RentaDAOImp;
import aplicacion.DAO.TicketDAO;
import aplicacion.DAO.TicketDAOImp;
import aplicacion.VO.CarritoItemVO;
import aplicacion.VO.CarritoVO;
import aplicacion.VO.PeliculaRentadaVO;
import aplicacion.VO.PeliculaVO;
import aplicacion.VO.TicketVO;
import aplicacion.VO.TicketDetalleVO;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import aplicacion.application.SesionUsuario;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.File;

/**
 * Controlador para la vista del carrito de compras.
 */
public class CarritoController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private TableView<CarritoItemVO> tablaCarrito;
    @FXML private TableColumn<CarritoItemVO, ImageView> colImagen;
    @FXML private TableColumn<CarritoItemVO, String> colTitulo;
    @FXML private TableColumn<CarritoItemVO, String> colClasificacion;
    @FXML private TableColumn<CarritoItemVO, Integer> colCantidad;
    @FXML private TableColumn<CarritoItemVO, Double> colPrecioUnitario;
    @FXML private TableColumn<CarritoItemVO, Double> colSubtotal;
    @FXML private TableColumn<CarritoItemVO, HBox> colAcciones;
    
    @FXML private Label lblTotalArticulos;
    @FXML private Label lblTotalPagar;
    @FXML private Button btnPagar;
    @FXML private Button btnVaciarCarrito;
    @FXML private Button btnVolver;
    @FXML private VBox emptyCartMessage;

    @FXML private Button btnVolverMenu;
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
    
    private CarritoDAO carritoDAO = new CarritoDAOImp();
    private TicketDAO ticketDAO = new TicketDAOImp();
    private ObservableList<CarritoItemVO> listaItems = FXCollections.observableArrayList();
    private CarritoVO carritoActual;
    private final Image defaultMovieImage = new Image(getClass().getResourceAsStream("/aplicacion/Imgs/Claqueta.jpg"));
    
    // Usuario ficticio para desarrollo (eliminar en producción)
    private final UsuarioVO usuarioPrueba = new UsuarioVO(1, "Usuario Prueba", "correo@example.com", "123456", 
                                                        "¿Cuál es tu color favorito?", "Azul", 
                                                        "Calle Principal 123", false, null, true);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar la tabla
        setupTable();
        
        // Para pruebas, asegurar que haya un usuario
        if (!SesionUsuario.getInstancia().haySesionActiva()) {
            SesionUsuario.getInstancia().iniciarSesion(usuarioPrueba);
        }
        
        // Cargar items del carrito
        cargarItemsCarrito();
    }
    
    private void setupTable() {
        // Configurar columnas de la tabla
        colImagen.setCellValueFactory(cellData -> {
            CarritoItemVO item = cellData.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            
            if (item.getPelicula() != null && item.getPelicula().getImagen() != null) {
                Image image = new Image(new ByteArrayInputStream(item.getPelicula().getImagen()));
                imageView.setImage(image);
            } else {
                imageView.setImage(defaultMovieImage);
            }
            
            return new SimpleObjectProperty<>(imageView);
        });
        
        colTitulo.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPelicula() != null) {
                return new SimpleStringProperty(cellData.getValue().getPelicula().getTitulo());
            }
            return new SimpleStringProperty("Desconocido");
        });
        
        colClasificacion.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPelicula() != null) {
                return new SimpleStringProperty(cellData.getValue().getPelicula().getClasificacion());
            }
            return new SimpleStringProperty("N/A");
        });
        
        colCantidad.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cantidad"));
        
        colPrecioUnitario.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("precioUnitario"));
        colPrecioUnitario.setCellFactory(tc -> new TableCell<>() {
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
        
        colSubtotal.setCellValueFactory(cellData -> {
            CarritoItemVO item = cellData.getValue();
            double subtotal = item.getCantidad() * item.getPrecioUnitario();
            return new ReadOnlyObjectWrapper<>(subtotal);
        });
        
        colSubtotal.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double subtotal, boolean empty) {
                super.updateItem(subtotal, empty);
                if (empty || subtotal == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", subtotal));
                }
            }
        });
        
        colAcciones.setCellValueFactory(cellData -> {
            CarritoItemVO item = cellData.getValue();
            
            Button btnSumar = new Button("+");
            btnSumar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 15;");
            btnSumar.setPrefSize(30, 30);
            btnSumar.setOnAction(e -> incrementarCantidad(item));
            
            Button btnRestar = new Button("-");
            btnRestar.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-background-radius: 15;");
            btnRestar.setPrefSize(30, 30);
            btnRestar.setOnAction(e -> decrementarCantidad(item));
            
            Button btnEliminar = new Button("X");
            btnEliminar.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 15;");
            btnEliminar.setPrefSize(30, 30);
            btnEliminar.setOnAction(e -> eliminarItem(item));
            
            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER);
            hbox.getChildren().addAll(btnRestar, btnSumar, btnEliminar);
            
            return new SimpleObjectProperty<>(hbox);
        });
    }
    
    private void cargarItemsCarrito() {
        try {
            // Obtener el usuario de la sesión o usar el usuario de prueba
            UsuarioVO usuario = SesionUsuario.getInstancia().getUsuarioActual();
            if (usuario == null) {
                usuario = usuarioPrueba;
                SesionUsuario.getInstancia().iniciarSesion(usuario);
            }
            
            // Obtener el carrito del usuario
            carritoActual = carritoDAO.obtenerCarritoUsuario(usuario.getId());
            
            if (carritoActual == null) {
                mostrarCarritoVacio();
                return;
            }
            
            // Obtener los items del carrito
            List<CarritoItemVO> items = carritoDAO.obtenerItemsCarrito(carritoActual.getIdCarrito());
            
            if (items.isEmpty()) {
                mostrarCarritoVacio();
                return;
            }
            
            // Actualizar la lista observable
            listaItems.clear();
            listaItems.addAll(items);
            tablaCarrito.setItems(listaItems);
            
            // Actualizar totales
            actualizarTotales();
            
            // Ocultar mensaje de carrito vacío
            emptyCartMessage.setVisible(false);
            tablaCarrito.setVisible(true);
            
        } catch (Exception e) {
            Alertas.mostrarError("Error al cargar el carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void mostrarCarritoVacio() {
        listaItems.clear();
        tablaCarrito.setVisible(false);
        emptyCartMessage.setVisible(true);
        lblTotalArticulos.setText("0");
        lblTotalPagar.setText("$0.00");
        btnPagar.setDisable(true);
        btnVaciarCarrito.setDisable(true);
    }
    
    private void actualizarTotales() {
        int totalArticulos = 0;
        double totalPagar = 0.0;
        
        for (CarritoItemVO item : listaItems) {
            totalArticulos += item.getCantidad();
            totalPagar += item.getCantidad() * item.getPrecioUnitario();
        }
        
        lblTotalArticulos.setText(String.valueOf(totalArticulos));
        lblTotalPagar.setText(String.format("$%.2f", totalPagar));
        
        // Deshabilitar botones si no hay items
        boolean hayItems = !listaItems.isEmpty();
        btnPagar.setDisable(!hayItems);
        btnVaciarCarrito.setDisable(!hayItems);
    }
    
    private void incrementarCantidad(CarritoItemVO item) {
        item.setCantidad(item.getCantidad() + 1);
        
        try {
            if (carritoDAO.actualizarItemCarrito(item)) {
                actualizarTotales();
                tablaCarrito.refresh();
            } else {
                Alertas.mostrarError("Error al actualizar cantidad");
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error al incrementar cantidad: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void decrementarCantidad(CarritoItemVO item) {
        if (item.getCantidad() <= 1) {
            eliminarItem(item);
            return;
        }
        
        item.setCantidad(item.getCantidad() - 1);
        
        try {
            if (carritoDAO.actualizarItemCarrito(item)) {
                actualizarTotales();
                tablaCarrito.refresh();
            } else {
                Alertas.mostrarError("Error al actualizar cantidad");
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error al decrementar cantidad: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void eliminarItem(CarritoItemVO item) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar del Carrito");
        confirmacion.setHeaderText("¿Está seguro de eliminar este artículo del carrito?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        
        Optional<ButtonType> result = confirmacion.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (carritoDAO.eliminarItemCarrito(item.getIdItem())) {
                    listaItems.remove(item);
                    actualizarTotales();
                    
                    if (listaItems.isEmpty()) {
                        mostrarCarritoVacio();
                    }
                    
                    Alertas.mostrarExito("Artículo eliminado del carrito");
                } else {
                    Alertas.mostrarError("Error al eliminar artículo del carrito");
                }
            } catch (Exception e) {
                Alertas.mostrarError("Error al eliminar artículo: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleVaciarCarrito() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Vaciar Carrito");
        confirmacion.setHeaderText("¿Está seguro de vaciar todo el carrito?");
        confirmacion.setContentText("Esta acción eliminará todos los artículos y no se puede deshacer.");
        
        Optional<ButtonType> result = confirmacion.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (carritoDAO.vaciarCarrito(carritoActual.getIdCarrito())) {
                    listaItems.clear();
                    mostrarCarritoVacio();
                    Alertas.mostrarExito("Carrito vaciado exitosamente");
                } else {
                    Alertas.mostrarError("Error al vaciar el carrito");
                }
            } catch (Exception e) {
                Alertas.mostrarError("Error al vaciar el carrito: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handlePagar() {
        if (listaItems.isEmpty()) {
            Alertas.mostrarAdvertencia("No hay artículos en el carrito para realizar el pago");
            return;
        }
        
        // Obtener el usuario de la sesión o usar el usuario de prueba
        UsuarioVO usuario = SesionUsuario.getInstancia().getUsuarioActual();
        if (usuario == null) {
            usuario = usuarioPrueba;
            SesionUsuario.getInstancia().iniciarSesion(usuario);
        }
        
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Pago");
        confirmacion.setHeaderText("¿Está seguro de realizar el pago?");
        
        // Calcular el total
        double totalPagar = 0.0;
        for (CarritoItemVO item : listaItems) {
            totalPagar += item.getCantidad() * item.getPrecioUnitario();
        }
        
        confirmacion.setContentText("Total a pagar: " + String.format("$%.2f", totalPagar));
        
        Optional<ButtonType> result = confirmacion.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Crear el ticket
                TicketVO nuevoTicket = new TicketVO();
                nuevoTicket.setIdUsuario(usuario.getId());
                nuevoTicket.setFechaCompra(LocalDateTime.now());
                nuevoTicket.setTotal(totalPagar);
                nuevoTicket.setEstado("Pagado");
                
                // Lista de detalles
                List<TicketDetalleVO> detalles = new ArrayList<>();
                
                // Convertir items del carrito a detalles del ticket
                for (CarritoItemVO item : listaItems) {
                    TicketDetalleVO detalle = new TicketDetalleVO();
                    detalle.setIdPelicula(item.getIdPelicula());
                    detalle.setCantidad(item.getCantidad());
                    detalle.setPrecioUnitario(item.getPrecioUnitario());
                    detalle.setSubtotal(item.getCantidad() * item.getPrecioUnitario());
                    
                    detalles.add(detalle);
                }
                
                // Guardar el ticket y sus detalles
                if (ticketDAO.crearTicket(nuevoTicket, detalles)) {
                    // Después de crear el ticket, crear las rentas correspondientes
                    RentaDAO rentaDAO = new RentaDAOImp();
                    boolean todasLasRentasCreadas = true;
                    
                    for (CarritoItemVO item : listaItems) {
                        // Por cada película en el carrito, crear una renta
                        PeliculaRentadaVO nuevaRenta = new PeliculaRentadaVO();
                        nuevaRenta.setIdPelicula(item.getIdPelicula());
                        nuevaRenta.setIdUsuario(usuario.getId());
                        nuevaRenta.setImagen(item.getPelicula().getImagen());
                        nuevaRenta.setEstado("rentada");
                        
                        // Intentar agregar la renta
                        if (!rentaDAO.agregarRenta(nuevaRenta)) {
                            todasLasRentasCreadas = false;
                            System.err.println("Error al rentar la película ID: " + item.getIdPelicula());
                        } else {
                            // Si la renta fue exitosa, crear un archivo txt con el nombre de la película
                            try {
                                // Crear el directorio Pelistxt si no existe
                                File pelistxtDir = new File("Pelistxt");
                                if (!pelistxtDir.exists()) {
                                    pelistxtDir.mkdir();
                                }
                                
                                // Obtener el título de la película y limpiar caracteres no válidos para nombre de archivo
                                String titulo = item.getPelicula().getTitulo();
                                String tituloArchivo = titulo.replaceAll("[\\\\/:*?\"<>|]", "_");
                                
                                // Crear el archivo de texto
                                String nombreArchivo = "Pelistxt/" + tituloArchivo + ".txt";
                                File archivo = new File(nombreArchivo);
                                
                                // Escribir información en el archivo
                                try (FileWriter writer = new FileWriter(archivo)) {
                                    writer.write("Película: " + titulo + "\n");
                                    writer.write("Fecha de renta: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
                                    writer.write("Usuario: " + usuario.getNombre() + " (" + usuario.getCorreo() + ")\n");
                                    writer.write("ID de Ticket: " + nuevoTicket.getIdTicket() + "\n");
                                    writer.write("Precio: $" + String.format("%.2f", item.getPrecioUnitario()) + "\n");
                                }
                                
                                System.out.println("Archivo creado: " + nombreArchivo);
                            } catch (Exception e) {
                                System.err.println("Error al crear archivo txt para la película: " + e.getMessage());
                                // No interrumpimos el proceso si falla la creación del archivo
                            }
                        }
                    }
                    
                    // Vaciar el carrito después de la compra
                    carritoDAO.vaciarCarrito(carritoActual.getIdCarrito());
                    
                    // Mostrar confirmación de compra
                    Alert exito = new Alert(Alert.AlertType.INFORMATION);
                    exito.setTitle("Compra Realizada");
                    exito.setHeaderText("¡Compra exitosa!");
                    String mensaje = "Su compra ha sido procesada correctamente.\n\n" +
                                   "Número de Ticket: " + nuevoTicket.getIdTicket() + "\n" +
                                   "Total: " + String.format("$%.2f", totalPagar) + "\n\n";
                                   
                    if (todasLasRentasCreadas) {
                        mensaje += "Las películas han sido rentadas y están disponibles en 'Mis Rentas'.\n" +
                                  "Recuerde que las rentas expiran después de 2 horas.";
                    } else {
                        mensaje += "Algunas películas no pudieron ser rentadas. Por favor contacte a soporte.";
                    }
                    
                    exito.setContentText(mensaje);
                    exito.showAndWait();
                    
                    // Regresar al catálogo
                    handleVolver();
                } else {
                    Alertas.mostrarError("Error al procesar el pago. Intente nuevamente.");
                }
                
            } catch (Exception e) {
                Alertas.mostrarError("Error al procesar el pago: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleVolver() {
        try {
            // Volver a la pantalla de catálogo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aplicacion/application/ListaPeliculas.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            Alertas.mostrarError("Error al volver al catálogo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
