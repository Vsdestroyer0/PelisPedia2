USE PelisPedia;

/*Ingresar admins*/
insert into Usuario values ("Admin","Admin","12345678","12345678","Cuantos siglos tiene Daniel","300","Vicente Guerrero", true, "aa",true);

/*Ingresar Pelis*/
insert into peliculasGeneral values("Como entregar a tu dragon","","");

/*Mostrar tablas general*/
select * from Usuario;

select * from peliculasGeneral;

select * from peliculasRentadas;


drop table peliculasRentadas;
drop table Usuario;
