package conexion;

import java.sql.*;

public class Actividad2_7 {
    public static void main(String[] args) {
        System.out.println("=== Actividad 2.7: Caso Práctico JDBC Avanzado ===");

        try (Connection con = ConexionPool.getConnection()) {

            // Reinicia auto-commit
            con.setAutoCommit(true);

            insertarEmpleadosYProyectos(con);

            // Buscar IDs reales
            int idEmpleadoAna = obtenerId(con, "empleados", "Ana");
            int idProyectoAlpha = obtenerId(con, "proyectos", "Proyecto Alpha");

            int idEmpleadoLuis = obtenerId(con, "empleados", "Luis");
            int idProyectoBeta = obtenerId(con, "proyectos", "Proyecto Beta");

            // Asignar empleados a proyectos válidos
            llamarProcedimiento(con, idEmpleadoAna, idProyectoAlpha);
            llamarProcedimiento(con, idEmpleadoLuis, idProyectoBeta);

            // Transacción (sube salario de Ana, baja presupuesto de Alpha)
            realizarTransaccion(con, idEmpleadoAna, 500.00, idProyectoAlpha);

            // Mostrar resultados
            mostrarEstadoFinal(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔍 Obtener ID de un empleado o proyecto por nombre
    private static int obtenerId(Connection con, String tabla, String nombre) throws SQLException {
        String sql = "SELECT id FROM " + tabla + " WHERE nombre = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        throw new SQLException("No se encontró el registro '" + nombre + "' en la tabla " + tabla);
    }


    // --- Inserta empleados y proyectos con PreparedStatement ---
    private static void insertarEmpleadosYProyectos(Connection con) throws SQLException {
        String sqlEmpleado = "INSERT INTO empleados (nombre, salario) VALUES (?, ?)";
        String sqlProyecto = "INSERT INTO proyectos (nombre, presupuesto) VALUES (?, ?)";

        try (PreparedStatement psE = con.prepareStatement(sqlEmpleado);
             PreparedStatement psP = con.prepareStatement(sqlProyecto)) {

            psE.setString(1, "Carlos");
            psE.setDouble(2, 2100.00);
            psE.executeUpdate();

            psP.setString(1, "Proyecto Gamma");
            psP.setDouble(2, 7000.00);
            psP.executeUpdate();

            System.out.println("Empleados y proyectos insertados correctamente.");
        }
    }

    // --- Llamar al procedimiento almacenado ---
    private static void llamarProcedimiento(Connection con, int idEmpleado, int idProyecto) throws SQLException {
        String call = "{ CALL asignar_empleado_a_proyecto(?, ?) }";
        try (CallableStatement cs = con.prepareCall(call)) {
            cs.setInt(1, idEmpleado);
            cs.setInt(2, idProyecto);
            cs.execute();
            System.out.println("Empleado " + idEmpleado + " asignado al proyecto " + idProyecto);
        }
    }

    // --- Transacción: incrementar salario y reducir presupuesto ---
    private static void realizarTransaccion(Connection con, int idEmpleado, double incremento, int idProyecto) {
        try {
            con.setAutoCommit(false);

            String sqlSalario = "UPDATE empleados SET salario = salario + ? WHERE id = ?";
            String sqlPresupuesto = "UPDATE proyectos SET presupuesto = presupuesto - ? WHERE id = ?";

            try (PreparedStatement ps1 = con.prepareStatement(sqlSalario);
                 PreparedStatement ps2 = con.prepareStatement(sqlPresupuesto)) {

                // Incrementar salario
                ps1.setDouble(1, incremento);
                ps1.setInt(2, idEmpleado);
                ps1.executeUpdate();

                // Reducir presupuesto del proyecto
                ps2.setDouble(1, incremento);
                ps2.setInt(2, idProyecto);
                ps2.executeUpdate();

                con.commit();
                System.out.println("Transacción completada correctamente: +" + incremento + "€ salario / -" + incremento + "€ presupuesto.");

            } catch (SQLException e) {
                con.rollback();
                System.err.println("Error en la transacción. Se hizo rollback.");
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Mostrar estado final de las tablas ---
    private static void mostrarEstadoFinal(Connection con) throws SQLException {
        System.out.println("\n=== ESTADO FINAL DE LAS TABLAS ===");

        mostrarTabla(con, "empleados");
        mostrarTabla(con, "proyectos");
        mostrarTabla(con, "asignaciones");
    }

    private static void mostrarTabla(Connection con, String tabla) throws SQLException {
        String sql = "SELECT * FROM " + tabla;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println(" Tabla: " + tabla);
            ResultSetMetaData meta = rs.getMetaData();
            int columnas = meta.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnas; i++) {
                    System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "  ");
                }
                System.out.println();
            }
        }
    }
}

