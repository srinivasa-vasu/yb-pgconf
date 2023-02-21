ALTER USER postgres WITH PASSWORD 'postgres';

CREATE DATABASE todo;

ALTER DATABASE todo SET pgaudit.log='all, -misc';
ALTER DATABASE todo SET pgaudit.log_parameter=on;
ALTER DATABASE todo SET pgaudit.log_relation=on;
ALTER DATABASE todo SET pgaudit.log_catalog=off;