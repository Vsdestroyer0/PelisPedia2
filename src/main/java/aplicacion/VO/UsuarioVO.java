package aplicacion.VO;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsuarioVO {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty correo = new SimpleStringProperty();
    private final StringProperty contraseña = new SimpleStringProperty();
    private final StringProperty preguntaSeguridad = new SimpleStringProperty();
    private final StringProperty respuestaSeguridad = new SimpleStringProperty();
    private final StringProperty direccion = new SimpleStringProperty();
    private final BooleanProperty admin = new SimpleBooleanProperty();
    private final ObjectProperty<byte[]> imagen = new SimpleObjectProperty<>();
    private final BooleanProperty activo = new SimpleBooleanProperty(true);

    // Constructor completo
    public UsuarioVO(int id, String nombre, String correo, String contraseña,
                     String preguntaSeguridad, String respuestaSeguridad,
                     String direccion, boolean admin, byte[] imagen, boolean activo) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.correo.set(correo);
        this.contraseña.set(contraseña);
        this.preguntaSeguridad.set(preguntaSeguridad);
        this.respuestaSeguridad.set(respuestaSeguridad);
        this.direccion.set(direccion);
        this.admin.set(admin);
        this.imagen.set(imagen);
        this.activo.set(activo);
    }
    
    // Constructor para nuevos usuarios (sin ID)
    public UsuarioVO(String nombre, String correo, String contraseña,
                     String preguntaSeguridad, String respuestaSeguridad,
                     String direccion, boolean admin) {
        this.nombre.set(nombre);
        this.correo.set(correo);
        this.contraseña.set(contraseña);
        this.preguntaSeguridad.set(preguntaSeguridad);
        this.respuestaSeguridad.set(respuestaSeguridad);
        this.direccion.set(direccion);
        this.admin.set(admin);
        this.activo.set(true); // Por defecto, los usuarios están activos
    }

    // Constructor vacío
    public UsuarioVO() {
    }

    // Getters y setters usando propiedades JavaFX
    public int getId() {
        return id.get();
    }
    
    public IntegerProperty idProperty() {
        return id;
    }
    
    public void setId(int id) {
        this.id.set(id);
    }

    public String getNombre() {
        return nombre.get();
    }
    
    public StringProperty nombreProperty() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getCorreo() {
        return correo.get();
    }
    
    public StringProperty correoProperty() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo.set(correo);
    }

    public String getContraseña() {
        return contraseña.get();
    }
    
    public StringProperty contraseñaProperty() {
        return contraseña;
    }
    
    public void setContraseña(String contraseña) {
        this.contraseña.set(contraseña);
    }

    public String getPreguntaSeguridad() {
        return preguntaSeguridad.get();
    }
    
    public StringProperty preguntaSeguridadProperty() {
        return preguntaSeguridad;
    }
    
    public void setPreguntaSeguridad(String preguntaSeguridad) {
        this.preguntaSeguridad.set(preguntaSeguridad);
    }

    public String getRespuestaSeguridad() {
        return respuestaSeguridad.get();
    }
    
    public StringProperty respuestaSeguridadProperty() {
        return respuestaSeguridad;
    }
    
    public void setRespuestaSeguridad(String respuestaSeguridad) {
        this.respuestaSeguridad.set(respuestaSeguridad);
    }

    public String getDireccion() {
        return direccion.get();
    }
    
    public StringProperty direccionProperty() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public boolean isAdmin() {
        return admin.get();
    }
    
    public BooleanProperty adminProperty() {
        return admin;
    }
    
    public void setAdmin(boolean admin) {
        this.admin.set(admin);
    }

    public byte[] getImagen() {
        return imagen.get();
    }
    
    public ObjectProperty<byte[]> imagenProperty() {
        return imagen;
    }
    
    public void setImagen(byte[] imagen) {
        this.imagen.set(imagen);
    }

    public boolean isActivo() {
        return activo.get();
    }
    
    public BooleanProperty activoProperty() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo.set(activo);
    }
}
