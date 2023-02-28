package ru.netology.sql;

import lombok.SneakyThrows;

import java.sql.*;

public class SqlHelperCredit {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass"
        );
    }

    @SneakyThrows
    public static void cleanDefaultData() {
        String deletePay = "DELETE FROM credit_request_entity ";
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
    public static String getCardIdCredit() {
        String cardIdPay = "SELECT bank_id FROM credit_request_entity WHERE status = 'APPROVED'";
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
                Statement dataStmt = con.createStatement();
                PreparedStatement cardsStmt = con.prepareStatement(cardIdOrder)
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
    public static String getCardStatusApproved() {
        String cardStatusApproved = "SELECT status FROM credit_request_entity";
        try (
                Connection con = connect();
                Statement dataStmt = con.createStatement();
                PreparedStatement cardsStmt = con.prepareStatement(cardStatusApproved)
        ) {
            try (ResultSet rs = dataStmt.executeQuery(cardStatusApproved)) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "Error";
    }

    @SneakyThrows
    public static String getCardStatusDeclined() {
        String cardStatusDeclined = "SELECT status FROM credit_request_entity";
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



