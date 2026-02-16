package dataConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:data/shifts.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void init(){
        String createPeople = """
            CREATE TABLE IF NOT EXISTS people (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                birthdate DATETIME,
                wage REAL,
                unpaidSalary REAL DEFAULT 0,
                password TEXT,
                isAdmin INTEGER DEFAULT 0
            );
        """;

        String createShifts = """
            CREATE TABLE IF NOT EXISTS shifts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                personID INTEGER NOT NULL,
                startTime INTEGER,
                endTime INTEGER,
                FOREIGN KEY (personID) REFERENCES people(id)
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createPeople);
            stmt.execute(createShifts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
