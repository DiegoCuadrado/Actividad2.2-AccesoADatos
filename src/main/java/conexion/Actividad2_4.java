package conexion;

import java.sql.*;

public class Actividad2_4 {
    public static void main(String[] args) {
        System.out.println("=== ACTIVIDAD 2.4: Uso de ResultSet, ResultSetMetaData y DatabaseMetaData ===");

        try (Connection con = ConexionPool.getConnection()) {
            System.out.println("Conexión establecida correctamente.");

            // Ejecutar consulta SELECT
            String sql = "SELECT * FROM empleados";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                // Mostrar los datos de los empleados
                System.out.println("=== DATOS DE EMPLEADOS ===");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double salario = rs.getDouble("salario");

                    System.out.printf("ID: %d | Nombre: %-10s | Salario: %.2f €%n",
                            id, nombre, salario);
                }

                // Analizar ResultSetMetaData
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnas = rsmd.getColumnCount();
                System.out.println("=== METADATOS DEL RESULTSET ===");
                System.out.println("Número de columnas: " + columnas);

                for (int i = 1; i <= columnas; i++) {
                    System.out.printf("Columna %d: %s (%s)%n",
                            i, rsmd.getColumnName(i), rsmd.getColumnTypeName(i));
                }
            }

            // Obtener información del DatabaseMetaData
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("=== METADATOS DE LA BASE DE DATOS ===");
            System.out.println("Nombre del producto: " + meta.getDatabaseProductName());
            System.out.println("Versión del motor: " + meta.getDatabaseProductVersion());
            System.out.println("Driver JDBC: " + meta.getDriverName());
            System.out.println("Versión del driver: " + meta.getDriverVersion());
            System.out.println("Usuario conectado: " + meta.getUserName());
            System.out.println("URL de conexión: " + meta.getURL());

        } catch (SQLException e) {
            System.err.println("Error al acceder a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FIN DE LA ACTIVIDAD 2.4 ===");
    }
}

