package aplicacion.VO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PeliculaVO {
    private int idPelicula;
    private final SimpleObjectProperty<byte[]> imagen = new SimpleObjectProperty<>();
    private final SimpleStringProperty titulo = new SimpleStringProperty();
    private final SimpleStringProperty clasificacion = new SimpleStringProperty();
    private final SimpleStringProperty descripcion = new SimpleStringProperty();
    private final SimpleDoubleProperty precio = new SimpleDoubleProperty(200.00); // Precio por defecto

    // Constructor
    public PeliculaVO(int idPelicula, byte[] imagen, String titulo, String clasificacion, String descripcion, double precio) {
        this.idPelicula = idPelicula;
        this.imagen.set(imagen);
        this.titulo.set(titulo);
        this.clasificacion.set(clasificacion);
        this.descripcion.set(descripcion);
        this.precio.set(precio);
    }

    // Constructor sin ID (para nuevas películas)
    public PeliculaVO(byte[] imagen, String titulo, String clasificacion, String descripcion, double precio) {
        this.imagen.set(imagen);
        this.titulo.set(titulo);
        this.clasificacion.set(clasificacion);
        this.descripcion.set(descripcion);
        this.precio.set(precio);
    }

    // Getters y Setters
    public int getIdPelicula() { return idPelicula; }
    public void setIdPelicula(int idPelicula) { this.idPelicula = idPelicula; }
    
    public byte[] getImagen() { return imagen.get(); }
    public void setImagen(byte[] imagen) { this.imagen.set(imagen); }
    public ObjectProperty<byte[]> imagenProperty() { return imagen; }
    
    public String getTitulo() { return titulo.get(); }
    public void setTitulo(String titulo) { this.titulo.set(titulo); }
    public StringProperty tituloProperty() { return titulo; }
    
    public String getClasificacion() { return clasificacion.get(); }
    public void setClasificacion(String clasificacion) { this.clasificacion.set(clasificacion); }
    public StringProperty clasificacionProperty() { return clasificacion; }
    
    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }
    public StringProperty descripcionProperty() { return descripcion; }
    
    public double getPrecio() { return precio.get(); }
    public void setPrecio(double precio) { this.precio.set(precio); }
    public DoubleProperty precioProperty() { return precio; }

    @Override
    public String toString() {
        return "Película: " + getTitulo() + " (" + getClasificacion() + ")";
    }
}