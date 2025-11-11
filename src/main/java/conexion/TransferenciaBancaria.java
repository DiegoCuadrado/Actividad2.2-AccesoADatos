package conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Savepoint;
import java.sql.SQLException;

public class TransferenciaBancaria {
    public static void main(String[] args) {
        System.out.println("=== Iniciando transferencia bancaria ===");

        try (Connection con = ConexionPool.getConnection()) {
            // Desactivar autocommit para manejar transacciones manualmente
            con.setAutoCommit(false);

            // --- Restar 500€ a la cuenta  ---
            String sqlDebito = "UPDATE cuentas SET saldo = saldo - 500 WHERE id = 1";
            try (PreparedStatement ps = con.prepareStatement(sqlDebito)) {
                ps.executeUpdate();
                registrarLog(con, "Debitado 500€ de la cuenta 1 (Ana)");
            }

            // Crear un SAVEPOINT después del primer paso
            Savepoint punto1 = con.setSavepoint("DebitoRealizado");

            // --- Sumar 500€ a la cuenta  ---
            String sqlCredito = "UPDATE cuentas SET saldo = saldo + 500 WHERE id = 2";
            try (PreparedStatement ps = con.prepareStatement(sqlCredito)) {
                ps.executeUpdate();
                registrarLog(con, "Acreditado 500€ en la cuenta 2 (Luis)");
            }

            // Si todo va bien → commit
            con.commit();
            System.out.println(" Transferencia completada con éxito.");

        } catch (SQLException e) {
            System.err.println(" Error en la transferencia: " + e.getMessage());
            try (Connection con = ConexionPool.getConnection()) {
                // rollback general por seguridad
                con.rollback();
                System.out.println(" Se ha hecho rollback completo de la transacción.");
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
        }
    }

    // Método auxiliar para registrar los pasos en la tabla logs
    private static void registrarLog(Connection con, String mensaje) throws SQLException {
        String sqlLog = "INSERT INTO logs (mensaje) VALUES (?)";
        try (PreparedStatement ps = con.prepareStatement(sqlLog)) {
            ps.setString(1, mensaje);
            ps.executeUpdate();
            System.out.println(" Log: " + mensaje);
        }
    }
}

