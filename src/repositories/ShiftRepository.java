package repositories;

import dataConfig.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ShiftRepository {
    public static void insertShift(int employeeID, long start, long end) {
        String sql = "INSERT INTO shifts (personID, startTime, endTime) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeID);
            ps.setLong(2, start);
            ps.setLong(3, end);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void addToUnpaidSalary(int employeeID, float amount) {
        String sql = "UPDATE people SET unpaidSalary = unpaidSalary + ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setFloat(1, amount);
            ps.setInt(2, employeeID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
