package conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Actividad 2.3 - Ejecución de sentencias DDL y DML con JDBC.
 * Reutiliza el pool de conexiones (HikariCP) de la Actividad 2.2.
 */
public class Actividad2_3 {
    public static void main(String[] args) {
        System.out.println("=== ACTIVIDAD 2.3: DDL y DML con JDBC (usando HikariCP) ===");

        try (Connection con = ConexionPool.getConnection()) {

            // Crear tabla "proyectos" (DDL)
            String crearTabla = """
                    CREATE TABLE IF NOT EXISTS proyectos (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL,
                        presupuesto DECIMAL(10,2)
                    )
                    """;
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(crearTabla);
                System.out.println("Tabla 'proyectos' creada o ya existente.");
            }

            // Insertar tres proyectos (DML)
            String insertarSQL = "INSERT INTO proyectos (nombre, presupuesto) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertarSQL)) {
                ps.setString(1, "Proyecto A");
                ps.setDouble(2, 15000.00);
                ps.executeUpdate();

                ps.setString(1, "Proyecto B");
                ps.setDouble(2, 23000.00);
                ps.executeUpdate();

                ps.setString(1, "Proyecto C");
                ps.setDouble(2, 18000.00);
                ps.executeUpdate();

                System.out.println("Se insertaron 3 proyectos correctamente.");
            }

            // Actualizar el presupuesto de un proyecto
            String actualizarSQL = "UPDATE proyectos SET presupuesto = ? WHERE nombre = ?";
            try (PreparedStatement ps = con.prepareStatement(actualizarSQL)) {
                ps.setDouble(1, 25000.00);
                ps.setString(2, "Proyecto B");
                int filas = ps.executeUpdate();
                System.out.println("Presupuesto actualizado en " + filas + " proyecto(s).");
            }

            // Eliminar un proyecto por id
            String eliminarSQL = "DELETE FROM proyectos WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(eliminarSQL)) {
                ps.setInt(1, 1); // elimina Proyecto A
                int filas = ps.executeUpdate();
                System.out.println("Proyecto eliminado (filas afectadas: " + filas + ").");
            }

            // Listar todos los proyectos
            String listarSQL = "SELECT * FROM proyectos";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(listarSQL)) {

                System.out.println("=== LISTADO DE PROYECTOS ===");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double presupuesto = rs.getDouble("presupuesto");
                    System.out.printf("ID: %d | Nombre: %-15s | Presupuesto: %.2f €%n",
                            id, nombre, presupuesto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al ejecutar operaciones JDBC: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FIN DE LA ACTIVIDAD 2.3 ===");
    }
}
