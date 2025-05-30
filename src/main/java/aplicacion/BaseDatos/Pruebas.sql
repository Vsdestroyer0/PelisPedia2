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

