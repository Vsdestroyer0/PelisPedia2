USE PelisPedia;

CREATE TABLE Usuario (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    preguntaSeguridad VARCHAR(100),
    respuestaSeguridad VARCHAR(100),
    direccion VARCHAR(200),
    esAdmin BOOLEAN DEFAULT FALSE,
    Imagen LONGBLOB,
    Activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY(id),
    UNIQUE(correo)
);

CREATE TABLE peliculasGeneral (
    id_pelicula INT AUTO_INCREMENT, -- Identificador único para cada película
    imagen LONGBLOB,                -- Imagen de la película
    titulo VARCHAR(50) NOT NULL,    -- Título de la película
    clasificacion VARCHAR(10),      -- Clasificación (por ejemplo, PG, R, etc.)
    descripcion VARCHAR(200),       -- Descripción de la película
    precio DECIMAL(10,2) DEFAULT 200.00, -- Precio de la película (por defecto $200)
    PRIMARY KEY(id_pelicula),
    UNIQUE(titulo) -- Garantiza que no haya títulos duplicados
);

CREATE TABLE peliculasRentadas (
    id_renta INT AUTO_INCREMENT,
    id_pelicula INT,
    usuario VARCHAR(50),
    imagen LONGBLOB,
    fecha_renta DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega DATETIME,
    estado VARCHAR(20) DEFAULT 'rentada',
    PRIMARY KEY (id_renta),
    FOREIGN KEY (id_pelicula) REFERENCES peliculasGeneral(id_pelicula)
);

-- Tabla para el carrito de compras
CREATE TABLE carrito (
    id_carrito INT AUTO_INCREMENT,
    id_usuario INT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_carrito),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id)
);

-- Tabla para los items del carrito
CREATE TABLE carrito_items (
    id_item INT AUTO_INCREMENT,
    id_carrito INT,
    id_pelicula INT,
    cantidad INT DEFAULT 1,
    precio_unitario DECIMAL(10,2),
    PRIMARY KEY (id_item),
    FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito),
    FOREIGN KEY (id_pelicula) REFERENCES peliculasGeneral(id_pelicula)
);

-- Tabla para los tickets de compra
CREATE TABLE tickets (
    id_ticket INT AUTO_INCREMENT,
    numero_ticket VARCHAR(20) NOT NULL,
    id_usuario INT,
    monto_total DECIMAL(10,2) NOT NULL,
    fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'pagado',
    PRIMARY KEY (id_ticket),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id),
    UNIQUE(numero_ticket)
);

-- Tabla para los detalles del ticket (películas compradas)
CREATE TABLE ticket_detalle (
    id_detalle INT AUTO_INCREMENT,
    id_ticket INT,
    id_pelicula INT,
    cantidad INT,
    precio_unitario DECIMAL(10,2),
    PRIMARY KEY (id_detalle),
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_pelicula) REFERENCES peliculasGeneral(id_pelicula)
);

USE PelisPedia;

-- Crear la tabla de Favoritos si no existe
CREATE TABLE IF NOT EXISTS Favorito (
    idUsuario INT NOT NULL,
    idPelicula INT NOT NULL,
    fechaAgregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idUsuario, idPelicula),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (idPelicula) REFERENCES peliculasGeneral(id_pelicula) ON DELETE CASCADE
);