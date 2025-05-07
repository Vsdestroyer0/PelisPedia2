show databases;

    Create database PelisPedia;

    create user 'Cine'@'Localhost' identified by '1234';

    Grant create, alter, drop, insert, update, delete, select, references on PelisPedia.* to 'Cine'@'Localhost';

    Grant Create routine, alter routine on PelisPedia.* to 'Cine'@'LocalHost';
    set global log_bin_trust_function_creators = 1;

    grant trigger on PelisPedia.* to 'Cine'@'LocalHost';

    grant index on PelisPedia.* to 'Cine'@'LocalHost';

    SET GLOBAL log_bin_trust_function_creators = 1;

    GRANT ALL PRIVILEGES ON PelisPedia.* TO 'Cine'@'localhost';
    FLUSH PRIVILEGES;
