package aplicacion.VO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PeliculaVO {
    private int idPelicula;
    private final SimpleObjectProperty<byte[]> imagen = new SimpleObjectProperty<>();
    private String titulo;
    private String clasificacion;
    private final String descripcion;
    private int stock;

    // Constructor
    public PeliculaVO(int idPelicula, byte[] imagen, String titulo, String clasificacion, String descripción, int stock) {
        this.idPelicula = idPelicula;
        this.imagen.set(imagen);
        this.titulo = titulo;
        this.clasificacion = clasificacion;
        this.stock = stock;
        this.descripcion = descripción;
    }

    // Getters y Setters
    public int getIdPelicula() { return idPelicula; }
    public ObjectProperty<byte[]> imagenProperty() {
        return imagen;
    }    public String getTitulo() { return titulo; }
    public String getClasificacion() { return clasificacion; }
    public int getStock() { return stock; }
    public String getDescripcion() { return descripcion;}

    public void setStock(int stock) { this.stock = stock; }


}