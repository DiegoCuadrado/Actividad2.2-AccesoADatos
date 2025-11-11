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

-- Procedimiento para obtener un empleado por ID
DELIMITER //
CREATE PROCEDURE obtener_empleado(IN p_id INT)
BEGIN
SELECT * FROM empleados WHERE id = p_id;
END //
DELIMITER ;

USE empresa;

DELIMITER //

CREATE PROCEDURE incrementar_salario (
    IN p_id INT,
    IN p_incremento DOUBLE,
    OUT p_nuevo_salario DOUBLE
)
BEGIN
    -- Actualiza el salario del empleado
UPDATE empleados
SET salario = salario + p_incremento
WHERE id = p_id;

-- Devuelve el nuevo salario
SELECT salario INTO p_nuevo_salario
FROM empleados
WHERE id = p_id;
END //

DELIMITER ;



CREATE TABLE IF NOT EXISTS cuentas (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       titular VARCHAR(100),
    saldo DECIMAL(10,2)
    );

INSERT INTO cuentas (titular, saldo) VALUES
                                         ('Ana', 2000.00),
                                         ('Luis', 1500.00);

CREATE TABLE IF NOT EXISTS logs (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    mensaje VARCHAR(255),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );