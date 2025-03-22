USE PelisPedia;

CREATE TABLE crearUsuario (
    usuario VARCHAR(15) NOT NULL,
    contraseña VARCHAR(12) NOT NULL,
    nombre VARCHAR(20) NOT NULL,
    apellidos VARCHAR(20) NOT NULL,
    direccion VARCHAR(30),
    esAdmin BOOLEAN DEFAULT FALSE
    PRIMARY KEY(usuario)
);


CREATE TABLE peliculasGeneral (
    id_pelicula INT AUTO_INCREMENT, -- Identificador único para cada película
    imagen LONGBLOB,                -- Imagen de la película
    titulo VARCHAR(50) NOT NULL,    -- Título de la película
    clasificacion VARCHAR(10),      -- Clasificación (por ejemplo, PG, R, etc.)
    stock int(10),
    PRIMARY KEY(id_pelicula),
    UNIQUE(titulo) -- Garantiza que no haya títulos duplicados
);

CREATE TABLE usuario_cartelera (
    id_usuario_cartelera INT AUTO_INCREMENT, -- Identificador único para esta relación
    usuario VARCHAR(15) NOT NULL,           -- Usuario que accede a la cartelera
    id_pelicula INT NOT NULL,               -- Película asociada al usuario
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP, -- Fecha en que la película fue añadida
    PRIMARY KEY(id_usuario_cartelera),
    FOREIGN KEY (usuario) REFERENCES crearUsuario(usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_pelicula) REFERENCES peliculasGeneral(id_pelicula) ON DELETE CASCADE
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

DELIMITER //
CREATE PROCEDURE validacion_usuario(Nombre_User varchar(20), Contraseña_user varchar(255))
BEGIN
SELECT *
FROM Usuario
WHERE Nombre = Nombre_User
  AND Contraseña = Contraseña_user;
END //
DELIMITER ;

DROP PROCEDURE obtener_admin;

/*Valida el usuario para el login*/
DELIMITER //
CREATE PROCEDURE obtener_admin(Nombre_User varchar(20),
 Contraseña_user varchar(255))
BEGIN
SELECT CASE WHEN esAdmin = 1 THEN 1 ELSE 0 END AS esAdmin
FROM Usuario
WHERE Nombre = Nombre_User
  AND Contraseña = Contraseña_user;
END //
DELIMITER ;

drop table peliculasGeneral;
drop table usuario_Cartelera;
drop table peliculas_renta;
