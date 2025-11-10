package conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

/**
 * Actividad 2.5 - Invocación de procedimientos almacenados con parámetros IN y OUT.
 */
public class Actividad2_5 {
    public static void main(String[] args) {
        System.out.println("=== ACTIVIDAD 2.5: CallableStatement (procedimiento almacenado) ===");

        try (Connection con = ConexionPool.getConnection()) {
            System.out.println("Conectado a la base de datos empresa.");

            // Parámetros de ejemplo
            int empleadoId = 2;       // ID del empleado a actualizar
            double incremento = 250.00; // incremento salarial

            // Llamada al procedimiento almacenado
            String sql = "{CALL incrementar_salario(?, ?, ?)}";
            try (CallableStatement cs = con.prepareCall(sql)) {

                // Parámetros IN
                cs.setInt(1, empleadoId);
                cs.setDouble(2, incremento);

                // Parámetro OUT
                cs.registerOutParameter(3, Types.DOUBLE);

                // Ejecutar procedimiento
                cs.execute();

                // Obtener el valor OUT
                double nuevoSalario = cs.getDouble(3);
                System.out.printf("Nuevo salario del empleado ID %d: %.2f €%n",
                        empleadoId, nuevoSalario);
            }

        } catch (Exception e) {
            System.err.println("Error al ejecutar CallableStatement: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FIN DE LA ACTIVIDAD 2.5 ===");
    }
}

