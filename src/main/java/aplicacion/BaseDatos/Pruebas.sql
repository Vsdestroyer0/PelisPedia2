USE PelisPedia;

/*Ingresar usuarios*/
insert into Usuario values (2,"Admin","Admin","12345678","Cuantos siglos tiene Daniel","300","Vicente Guerrero", true, "aa",1);
/*Ingresar Pelis*/
insert into peliculasGeneral values("Como entregar a tu dragon","","");

/*Mostrar tablas general*/
select * from Usuario;

select * from peliculasGeneral;

select * from peliculasRentadas;


drop table peliculasRentadas;
drop table Usuario;

UPDATE Usuario SET Activo = true WHERE id = "1";
UPDATE Usuario SET Activo = false WHERE id = "1";

SELECT
    pg.titulo,
    COUNT(pr.id_pelicula) AS veces_rentada
FROM
    peliculasRentadas pr
JOIN
    peliculasGeneral pg ON pr.id_pelicula = pg.id_pelicula
GROUP BY
    pr.id_pelicula
ORDER BY
    veces_rentada DESC;

INSERT INTO peliculasGeneral (imagen, titulo, clasificacion, descripcion, precio) VALUES
(NULL, 'El Padrino', 'R', 'Película de crimen y drama', 250.00),
(NULL, 'Matrix', 'PG-13', 'Película de ciencia ficción y acción', 300.00),
(NULL, 'Toy Story', 'G', 'Película animada familiar', 180.00),
(NULL, 'Inception', 'PG-13', 'Thriller de ciencia ficción', 280.00);

SELECT p.titulo, COUNT(r.id_pelicula) AS cantidad
FROM peliculasGeneral p
JOIN peliculasRentadas r ON p.id_pelicula = r.id_pelicula
GROUP BY p.id_pelicula, p.titulo
ORDER BY cantidad DESC;

SELECT COUNT(*) FROM peliculasGeneral;
SELECT COUNT(*) FROM peliculasRentadas;

-- Inserta algunas rentas para varias películas
INSERT INTO peliculasRentadas (id_pelicula) VALUES (1);
INSERT INTO peliculasRentadas (id_pelicula) VALUES (1);
INSERT INTO peliculasRentadas (id_pelicula) VALUES (2);
INSERT INTO peliculasRentadas (id_pelicula) VALUES (3);
INSERT INTO peliculasRentadas (id_pelicula) VALUES (3);
INSERT INTO peliculasRentadas (id_pelicula) VALUES (3);
INSERT INTO peliculasRentadas (id_pelicula) VALUES (4);

SELECT
    usuario,
    COUNT(*) AS total_rentas
FROM
    peliculasRentadas
GROUP BY
    usuario
ORDER BY
    total_rentas DESC;


INSERT INTO peliculasRentadas (id_pelicula, usuario, fecha_renta)
VALUES
  (1, 'juan123', '2024-06-01'),
  (1, 'maria456', '2024-06-02'),
  (2, 'juan123', '2024-06-03'),
  (3, 'pedro789', '2024-06-03'),
  (1, 'juan123', '2024-06-04'),
  (4, 'maria456', '2024-06-05'),
  (5, 'juan123', '2024-06-05'),
  (3, 'ana321', '2024-06-06'),
  (2, 'juan123', '2024-06-07'),
  (2, 'juan123', '2024-06-08');

  SELECT p.titulo, COUNT(f.idPelicula) AS cantidad
FROM peliculasGeneral p
JOIN Favorito f ON p.id_pelicula = f.idPelicula
GROUP BY p.id_pelicula, p.titulo
ORDER BY cantidad DESC;

-- Insertar usuarios válidos (ejemplo mínimo)
INSERT INTO Usuario (nombre, correo, contraseña, preguntaSeguridad, respuestaSeguridad, direccion, esAdmin, Imagen, Activo) VALUES
('Juan Perez', 'juan@example.com', 'pass123', '¿Color favorito?', 'Azul', 'Calle Falsa 123', FALSE, NULL, TRUE),
('Maria Lopez', 'maria@example.com', 'pass456', '¿Ciudad natal?', 'Madrid', 'Av. Siempre Viva 456', FALSE, NULL, TRUE),
('Admin', 'admin@example.com', 'adminpass', '¿Mascota?', 'Perro', 'Oficina Central', TRUE, NULL, TRUE);

-- Insertar películas válidas
INSERT INTO peliculasGeneral (imagen, titulo, clasificacion, descripcion, precio) VALUES
(NULL, 'El Padrino II', 'R', 'Secuela de la película de crimen y drama', 260.00),
(NULL, 'Matrix Reloaded', 'PG-13', 'Segunda parte de ciencia ficción y acción', 310.00),
(NULL, 'Toy Story 2', 'G', 'Secuela de película animada familiar', 190.00),
(NULL, 'Inception: Dreams', 'PG-13', 'Thriller de ciencia ficción - expansión', 290.00);

INSERT INTO Favorito (idUsuario, idPelicula) VALUES
(2, 1),
(3, 3),
(7, 2),
(10, 4);