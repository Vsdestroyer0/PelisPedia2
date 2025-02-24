use Pvz;


/*Pruebas de arranque*/
select * from usuario;

-- Usuario admin
INSERT INTO Usuario (Nombre, Contraseña, Imagen, esAdmin)
VALUES ('User', '123', NULL, False);

INSERT INTO Usuario (Nombre, Contraseña, Imagen, esAdmin)
VALUES ('Kiwi', '123', NULL, TRUE);


/*Delete admins
delete from usuario where esAdmin = 1;*/