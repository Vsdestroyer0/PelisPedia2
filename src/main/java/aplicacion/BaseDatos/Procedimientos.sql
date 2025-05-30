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
