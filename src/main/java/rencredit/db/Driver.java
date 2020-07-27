package rencredit.db;

import org.testng.Assert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Driver {

    private static String IP = "localhost";
    private static String PORT = "1433";
    private static String DB = "DB";

    private static String login = "sa";
    private static String pass = "123456";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:sqlserver://" + IP + ":" + PORT + ";database=" + DB,
                login,
                pass);
    }

    public static void updateData(String value) {
        try {
            Statement statement = getConnection().createStatement();
            Assert.assertEquals(statement.executeUpdate(value), 1, "Не удалось выполнить пролитие в БД");
        } catch (SQLException throwables) {
            System.out.println("Не удалось подключиться к БД");
        } finally {
            try {
                getConnection().close();
            } catch (SQLException throwables) {
                System.out.println("Не удалось подключиться к БД");
            }
        }
    }

    public static void clearDB() {
        try {
            Statement statement = getConnection().createStatement();
            statement.executeUpdate("DELETE FROM RENES;");

        } catch (SQLException throwables) {
            System.out.println("Не удалось подключиться к БД");
        } finally {
            try {
                getConnection().close();
            } catch (SQLException throwables) {
                System.out.println("Не удалось подключиться к БД");
            }
        }
    }

}
