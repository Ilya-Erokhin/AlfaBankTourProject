package ru.netology.sql;

import lombok.SneakyThrows;

import java.sql.*;

public class SqlHelperPayment {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass"
        );
    }

    @SneakyThrows
    public static void cleanDefaultData() {
        String deletePay = "DELETE FROM payment_entity ";
        String deleteOrder = "DELETE FROM order_entity";
        try (
                Connection conn = connect();
                Statement dataStmt = conn.createStatement()
        ) {
            dataStmt.executeUpdate(deletePay);
            dataStmt.executeUpdate(deleteOrder);
        }
    }

    @SneakyThrows
    public static String getCardIdPayment() {
        String cardIdPay = "SELECT transaction_id FROM payment_entity WHERE status = 'APPROVED'";
        Thread.sleep(500);
        try (
                Connection con = connect();
                Statement dataStmt = con.createStatement()
        ) {
            try (ResultSet rs = dataStmt.executeQuery(cardIdPay)) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "Error";
    }

    @SneakyThrows
    public static String getCardIdOrder() {
        String cardIdOrder = "SELECT payment_id FROM order_entity";
        try (
                Connection con = connect();
                Statement dataStmt = con.createStatement()
        ) {
            try (ResultSet rs = dataStmt.executeQuery(cardIdOrder)) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "Error";
    }

    @SneakyThrows
    public static String getCardStatusDeclined() {
        String cardStatusDeclined = "SELECT status FROM payment_entity";
        try (
                Connection con = connect();
                Statement dataStmt = con.createStatement()

        ) {
            try (ResultSet rs = dataStmt.executeQuery(cardStatusDeclined)) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "Error";
    }
}