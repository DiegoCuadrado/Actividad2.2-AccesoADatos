package conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PruebaPool {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de pool de conexiones...");

        // Repetimos 3 veces para comprobar que el pool funciona correctamente
        for (int i = 1; i <= 3; i++) {
            try (Connection con = ConexionPool.getConnection();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT nombre FROM empleados")) {

                System.out.println("Conexión #" + i + " obtenida del pool.");
                while (rs.next()) {
                    System.out.println("Empleado: " + rs.getString("nombre"));
                }

            } catch (Exception e) {
                System.err.println("Error usando la conexión #" + i + ": " + e.getMessage());
            }
        }

        // Cerrar el pool opcionalmente
        ConexionPool.cerrarPool();
    }
}

