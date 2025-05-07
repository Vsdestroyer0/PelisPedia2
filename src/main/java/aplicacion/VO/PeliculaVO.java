package aplicacion.VO;

public class PeliculaVO {
    private int idPelicula;
    private byte[] imagen;
    private String titulo;
    private String clasificacion;
    private int stock;

    // Constructor
    public PeliculaVO(int idPelicula, byte[] imagen, String titulo, String clasificacion, int stock) {
        this.idPelicula = idPelicula;
        this.imagen = imagen;
        this.titulo = titulo;
        this.clasificacion = clasificacion;
        this.stock = stock;
    }

    // Getters y Setters
    public int getIdPelicula() { return idPelicula; }
    public byte[] getImagen() { return imagen; }
    public String getTitulo() { return titulo; }
    public String getClasificacion() { return clasificacion; }
    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = stock; }
}