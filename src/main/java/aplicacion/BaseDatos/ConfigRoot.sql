show databases;

Create database PvZ;

create user 'Plantera'@'Localhost' identified by '1234';

Grant create, alter, drop, insert, update, delete, select, references on pvz.* to 'Plantera'@'Localhost';

Grant Create routine, alter routine on pvz.* to 'Plantera'@'LocalHost';
set global log_bin_trust_function_creators = 1;

grant trigger on pvz.* to 'Plantera'@'LocalHost';

grant index on pvz.* to 'Plantera'@'LocalHost';

GRANT ALL PRIVILEGES ON pvz.* TO 'Plantera'@'localhost';

FLUSH PRIVILEGES;


