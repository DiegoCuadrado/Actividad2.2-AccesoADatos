CREATE DATABASE IF NOT EXISTS empresa;
USE empresa;

CREATE TABLE empleados (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(50),
                           puesto VARCHAR(50),
                           salario DOUBLE
);

INSERT INTO empleados (nombre, puesto, salario) VALUES
                                                    ('Ana', 'Analista', 25000),
                                                    ('Luis', 'Programador', 28000),
                                                    ('Marta', 'Gerente', 32000),
                                                    ('Carlos', 'Diseñador', 27000);