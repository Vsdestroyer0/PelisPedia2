package Controles;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import aplicacion.DAO.TicketDAO;
import aplicacion.DAO.TicketDAOImp;
import aplicacion.VO.TicketDetalleVO;
import aplicacion.VO.TicketVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ListaTicketsController implements Initializable {

    @FXML private TableView<TicketVO> tablaTickets;
    @FXML private TableColumn<TicketVO, Integer> colId;
    @FXML private TableColumn<TicketVO, String> colFecha;
    @FXML private TableColumn<TicketVO, String> colUsuario;
    @FXML private TableColumn<TicketVO, Double> colTotal;
    @FXML private TableColumn<TicketVO, String> colEstado;
    @FXML private TableColumn<TicketVO, String> colPeliculas;
    
    @FXML private TableView<TicketDetalleVO> tablaDetalles;
    @FXML private TableColumn<TicketDetalleVO, String> colDetallePelicula;
    @FXML private TableColumn<TicketDetalleVO, Double> colDetallePrecio;
    @FXML private TableColumn<TicketDetalleVO, Integer> colDetalleCantidad;
    @FXML private TableColumn<TicketDetalleVO, String> colDetalleSubtotal;
    @FXML private TableColumn<TicketDetalleVO, String> colDetalleFechaRenta;
    @FXML private TableColumn<TicketDetalleVO, String> colDetalleFechaDevolucion;
    
    @FXML private TextField txtBuscar;
    

    
    private TicketDAO ticketDAO;
    private ObservableList<TicketVO> listaTickets;
    private ObservableList<TicketDetalleVO> listaDetalles;
    private FilteredList<TicketVO> listaFiltrada;
    
    // Formateador para fechas
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar el DAO y las listas
        ticketDAO = new TicketDAOImp();
        listaTickets = FXCollections.observableArrayList();
        listaDetalles = FXCollections.observableArrayList();
        
        // Configurar columnas de la tabla de tickets
        colId.setCellValueFactory(new PropertyValueFactory<>("idTicket"));
        
        // Para la fecha, formateamos la fecha de compra
        colFecha.setCellValueFactory(data -> {
            if (data.getValue().getFechaCompra() != null) {
                return new SimpleStringProperty(data.getValue().getFechaCompra().format(formatter));
            } else {
                return new SimpleStringProperty("N/A");
            }
        });
        
        // Para la columna de usuario, mostramos el ID por ahora
        colUsuario.setCellValueFactory(data -> 
            new SimpleStringProperty("Usuario #" + data.getValue().getIdUsuario())
        );
        
        // Usar el monto total para la columna total
        colTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));
        
        // Estado del ticket
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        // Para las películas, mostramos la cantidad de elementos
        colPeliculas.setCellValueFactory(data -> {
            int cantidad = data.getValue().getDetalles().size();
            return new SimpleStringProperty(cantidad + " película(s)");
        });
        
        // Configurar columnas de la tabla de detalles
        colDetallePelicula.setCellValueFactory(data -> {
            if (data.getValue().getPelicula() != null) {
                return new SimpleStringProperty(data.getValue().getPelicula().getTitulo());
            } else {
                return new SimpleStringProperty("ID: " + data.getValue().getIdPelicula());
            }
        });
        
        // Precio unitario de cada detalle
        colDetallePrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        
        // Cantidad de elementos
        colDetalleCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        
        // Subtotal calculado
        colDetalleSubtotal.setCellValueFactory(data -> {
            // Usar el método de la clase para calcular
            double subtotal = data.getValue().calcularSubtotal();
            return new SimpleStringProperty(String.format("$%.2f", subtotal));
        });
        
        // Estas columnas no existen en el modelo actual
        colDetalleFechaRenta.setCellValueFactory(data -> new SimpleStringProperty("N/A"));
        colDetalleFechaDevolucion.setCellValueFactory(data -> new SimpleStringProperty("N/A"));
        
        // Listener para mostrar detalles cuando se selecciona un ticket
        tablaTickets.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetallesTicket(newSelection);
            } else {
                listaDetalles.clear();
            }
        });
        
        // Cargar los tickets iniciales
        cargarTickets();
        
        // Configurar filtrado
        listaFiltrada = new FilteredList<>(listaTickets, p -> true);
        tablaTickets.setItems(listaFiltrada);
    }
    
    private void cargarTickets() {
        try {
            // Obtener todos los tickets
            List<TicketVO> tickets = ticketDAO.obtenerTodosLosTickets();
            listaTickets.clear();
            if (tickets != null) {
                listaTickets.addAll(tickets);
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error al cargar tickets: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void mostrarDetallesTicket(TicketVO ticket) {
        listaDetalles.clear();
        if (ticket != null) {
            try {
                // Obtener detalles del ticket
                List<TicketDetalleVO> detalles = ticketDAO.obtenerDetallesTicket(ticket.getIdTicket());
                if (detalles != null && !detalles.isEmpty()) {
                    listaDetalles.addAll(detalles);
                    tablaDetalles.setItems(listaDetalles);
                    
                    // También actualizamos los detalles en el objeto ticket
                    for (TicketDetalleVO detalle : detalles) {
                        if (!ticket.getDetalles().contains(detalle)) {
                            ticket.agregarDetalle(detalle);
                        }
                    }
                }
            } catch (Exception e) {
                Alertas.mostrarError("Error al cargar detalles del ticket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleBuscar() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        
        if (textoBusqueda.isEmpty()) {
            listaFiltrada.setPredicate(p -> true);
        } else {
            listaFiltrada.setPredicate(ticket -> {
                // Buscar por ID de ticket
                if (String.valueOf(ticket.getIdTicket()).contains(textoBusqueda)) {
                    return true;
                }
                
                // Buscar por número de ticket
                if (ticket.getNumeroTicket() != null && 
                    ticket.getNumeroTicket().toLowerCase().contains(textoBusqueda)) {
                    return true;
                }
                
                // Buscar por ID de usuario
                if (String.valueOf(ticket.getIdUsuario()).contains(textoBusqueda)) {
                    return true;
                }
                
                return false;
            });
        }
        
        if (listaFiltrada.isEmpty()) {
            Alertas.mostrarAdvertencia("No se encontraron tickets con ese criterio.");
        }
    }
    
    @FXML
    private void handleMostrarTodos() {
        txtBuscar.clear();
        listaFiltrada.setPredicate(p -> true);
    }
    
    @FXML
    private void handleVolverMenu() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) tablaTickets.getScene().getWindow();
            stage.close();
            
            // Cargar el menú de usuario
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuAdministrador.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Menú Administrador - PelisPedia");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
