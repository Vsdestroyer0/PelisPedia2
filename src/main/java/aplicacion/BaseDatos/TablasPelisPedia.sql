USE PelisPedia;

CREATE TABLE Usuario (
    nombre VARCHAR(50) NOT NULL,
    correo Varchar(50) not null,
    contraseña VARCHAR(30) NOT NULL,
    confirmarContraseña varchar(12) not null,
    preguntaSeguridad VARCHAR(40) NOT NULL,
    respuestaSeguridad VARCHAR(40) NOT NULL,
    direccion VARCHAR(30),
    esAdmin boolean default false,
    PRIMARY KEY(correo)
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

drop table peliculasGeneral;
drop table usuario_Cartelera;
drop table peliculas_renta;


USE PelisPedia;


-- Trigger 1: Actualizar stock al rentar
DELIMITER //
CREATE TRIGGER after_rental_insert
AFTER INSERT ON peliculasRentadas
FOR EACH ROW
BEGIN
    IF NEW.estado = 'rentada' THEN
        UPDATE peliculasGeneral
        SET stock = stock - 1
        WHERE id_pelicula = NEW.id_pelicula;
    END IF;
END//
DELIMITER ;

-- Trigger 2: Actualizar stock al devolver
DELIMITER //
CREATE TRIGGER after_rental_update
AFTER UPDATE ON peliculasRentadas
FOR EACH ROW
BEGIN
    IF NEW.estado = 'devuelta' AND OLD.estado = 'rentada' THEN
        UPDATE peliculasGeneral
        SET stock = stock + 1
        WHERE id_pelicula = NEW.id_pelicula;
    END IF;
END//
DELIMITER ;

-- Procedimiento almacenado para rentar una película
DELIMITER //
CREATE PROCEDURE rentar_pelicula(
    IN p_usuario VARCHAR(15),
    IN p_id_pelicula INT,
    OUT p_mensaje VARCHAR(100)
)
BEGIN
    DECLARE v_stock INT;

    -- Verificar el stock disponible
    SELECT stock INTO v_stock
    FROM peliculasGeneral
    WHERE id_pelicula = p_id_pelicula;

    -- Verificar si hay stock disponible
    IF v_stock > 0 THEN
        -- Insertar el registro de renta
        INSERT INTO peliculas_renta (usuario, id_pelicula, estado_renta)
        VALUES (p_usuario, p_id_pelicula, 'rentada');

        SET p_mensaje = 'Película rentada exitosamente';
    ELSE
        SET p_mensaje = 'No hay stock disponible para esta película';
    END IF;
END//
DELIMITER ;


drop procedure if exists mostrarPelis;
delimiter //
create procedure mostrarPelis()
begin
    select * from peliculasGeneral;
end //
delimiter ;

call mostrarPelis();

-- Procedimiento para autenticación
DROP PROCEDURE validacion_usuario;

DELIMITER //
CREATE PROCEDURE validacion_usuario(
    IN p_username VARCHAR(20),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT
        id,
        nombre,
        correo,
        preguntaSeguridad,
        respuestaSeguridad,
        direccion,
        esAdmin
    FROM Usuario
    WHERE nombre = p_username
    AND contraseña = p_password;
END //
DELIMITER ;

DROP PROCEDURE obtener_admin;

-- Procedimiento para verificar admin
DELIMITER //
CREATE PROCEDURE obtener_admin(IN correo_user VARCHAR(50), IN contraseña_user VARCHAR(30))
BEGIN
    SELECT esAdmin
    FROM Usuario
    WHERE correo = correo_user
    AND contraseña = contraseña_user;
END //
DELIMITER ;

USE PelisPedia;

/*Ingresar usuarios*/
insert into Usuario values ("a","a","12345678","12345678","Cuantos siglos tiene Daniel","300","Vicente Guerrero", true);

/*Ingresar Pelis*/
insert into peliculasGeneral values("Como entregar a tu dragon","","");

/*Mostrar tablas general*/
select * from Usuario;

select * from peliculasGeneral;

select * from peliculasRentadas;


drop table peliculasRentadas;
drop table Usuario;
