package repositories;

import dataConfig.Database;
import utils.InputUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class EmployeeRepository {

    public static int getPersonId(String name, String password) {
        int personId = -1;

        String passwordHash = Integer.toString(password.hashCode());

        String sql = "SELECT id FROM people WHERE name = ? AND password = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name.trim().toUpperCase()); // normalize name
            ps.setString(2, passwordHash);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                personId = rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return personId;
    }

    public static float getWage(int employeeID) {
        String sql = "SELECT wage FROM people WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getFloat("wage");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean personExists(String name) {
        name = name.toUpperCase();
        String sql = "SELECT 1 FROM people WHERE name = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            return false;
        }
    }

    public static String getName(int employeeID) {
        String name = null;
        String sql = "SELECT name FROM people WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                name =  rs.getString("name");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public static void editingInformation(Scanner input) {
        int chosenEmployeeID;
        while (true) {
            System.out.print("Enter the name of the employee you want to edit: ");
            String name = input.nextLine().trim().toUpperCase();

            String lookupSql = "SELECT id, name, birthdate FROM people WHERE name = ?";

            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement(lookupSql)) {

                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();

                boolean found = false;

                System.out.println("Matching employees:");
                while (rs.next()) {
                    found = true;
                    System.out.printf(
                            "ID: %d | Name: %s | Birthdate: %s%n",
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("birthdate")
                    );
                }
                if (!found) {
                    System.out.println("No employees found with that name. Would you like to: \n1) Retry \n2) Exit");
                    int choice = InputUtilities.readInt(input, 2);
                    if  (choice == 2) {
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }
                    else{
                        continue;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }


            System.out.print("Enter the ID of the employee you want to edit: ");
            chosenEmployeeID = InputUtilities.readInt(input, 1000000);

            //If user exists, break out and continue editing
            break;
        }

        System.out.println("What information would you like to edit? \n1) Wage \n2) Birthdate \n3) Unpaid Salary \n4) Admin Privileges");

        String choice = input.nextLine();

        String sql;
        try (Connection conn = Database.getConnection()) {

            switch (choice) {

                case "1" -> {
                    float wage;
                    while (true) {
                        System.out.print("Enter new hourly wage: ");
                        String line = input.nextLine().trim();
                        try {
                            wage = Float.parseFloat(line);
                            if (wage < 0) {
                                System.out.println("Wage cannot be negative. Try again.");
                                continue;
                            }
                            break; // valid input
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Please enter a valid wage (e.g., 15.75).");
                        }
                    }

                    sql = "UPDATE people SET wage = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setFloat(1, wage);
                        ps.setInt(2, chosenEmployeeID);
                        printResult(ps.executeUpdate(), "Wage");
                    }
                }

                case "2" -> {
                    String birthdate;
                    while (true) {
                        System.out.print("Enter birthdate (YYYY-MM-DD): ");
                        birthdate = input.nextLine().trim();
                        if (birthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            break; // valid format
                        } else {
                            System.out.println("Invalid format. Please use YYYY-MM-DD.");
                        }
                    }

                    sql = "UPDATE people SET birthdate = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, birthdate);
                        ps.setInt(2, chosenEmployeeID);
                        printResult(ps.executeUpdate(), "Birthdate");
                    }
                }

                case "3" -> {
                    float unpaid;
                    while (true) {
                        System.out.print("Enter unpaid salary amount: ");
                        String line = input.nextLine().trim();
                        try {
                            unpaid = Float.parseFloat(line);
                            if (unpaid < 0) {
                                System.out.println("Unpaid salary cannot be negative. Try again.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Please enter a valid amount (e.g., 250.75).");
                        }
                    }

                    sql = "UPDATE people SET unpaidSalary = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setFloat(1, unpaid);
                        ps.setInt(2, chosenEmployeeID);
                        printResult(ps.executeUpdate(), "Unpaid salary");
                    }
                }

                case "4" -> {
                    System.out.print("Grant admin privileges? (y/n): ");
                    int isAdmin = InputUtilities.readYesNo(input) ? 1 : 0;

                    sql = "UPDATE people SET isAdmin = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, isAdmin);
                        ps.setInt(2, chosenEmployeeID);
                        printResult(ps.executeUpdate(), "Admin privileges");
                    }
                }

                default -> System.out.println("Invalid choice.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printResult(int rows, String field) {
        if (rows > 0) {
            System.out.println(field + " updated successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    public static boolean isAdmin(int employeeID) {
        String sql = "SELECT isAdmin FROM people WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("isAdmin") == 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int addPerson(String person, Scanner input, boolean starter) {
        person = person.toUpperCase();

        String birthdate;
        while (true) {
            System.out.print("Enter birthdate (YYYY-MM-DD): ");
            birthdate = input.nextLine().trim();

            if (birthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                break;
            } else {
                System.out.println("Invalid format. Please use YYYY-MM-DD.");
            }
        }

        float wage;
        while (true) {
            System.out.print("Enter hourly wage: ");
            String wageInput = input.nextLine().trim();

            try {
                wage = (float) Math.round(Float.parseFloat(wageInput) * 100) /100;
                if (wage > 0) {
                    break;
                } else {
                    System.out.println("Wage must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number for wage.");
            }
        }

        int isAdmin;
        System.out.print("Is this user an administrator? (y/n): ");
        boolean adminInput = InputUtilities.readYesNo(input);
        if (adminInput) {
            isAdmin = 1;
        }
        else {
            isAdmin = 0;
        }

        String password;
        boolean passwordsMatch = false;
        while (true) {
            System.out.print("Enter a password (password <= 20 CHARACTERS): ");
            password = input.nextLine();
            if (password.length() > 20) {
                System.out.println("Password must be less than 20 characters.");
                continue;
            }
            while (true) {
                System.out.println("Retype password for verification.");
                String password2 = input.nextLine();
                if (password2.equals(password)) {
                    passwordsMatch = true;
                    break;
                }
                else {
                    System.out.println("Passwords don't match. Would you like to: \n1) Enter again \n2) Enter new password?");
                    int choice  = InputUtilities.readInt(input, 2);
                    if (choice == 2) {
                        break;
                    }
                }
            }
            if (passwordsMatch) {
                break;
            }
        }
        String passwordHash= String.valueOf(password.hashCode());

        String sql = """
        INSERT INTO people (name, birthdate, wage, isAdmin, password)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, person);
            ps.setString(2, birthdate);
            ps.setFloat(3, wage);
            ps.setInt(4, isAdmin);
            ps.setString(5, passwordHash);
            ps.executeUpdate();

            System.out.println("User \"" + person + "\" added successfully.");

        } catch (Exception e) {
            System.out.println("Could not add user.");
            e.printStackTrace();
        }

        if (starter) {
            return getPersonId(person, password);
        }
        else{
            return -1;
        }
    }
}
