package utils;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class InputUtilities {

    // returns viable number
    public static int readInt(Scanner input, int maxOption) {
        int num;
        while (true) {
            String line = input.nextLine().trim();
            try {
                num = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
                continue;
            }
            if (num < 1 || num > maxOption) {
                System.out.println("Invalid number. Please enter a number between 1 and "  + maxOption + ".");
                continue;
            }
            return num;
        }
    }

    // returns true for yes and false for no
    public static boolean readYesNo(Scanner input) {
        while (true) {
            String line = input.nextLine().trim().toLowerCase();
            if (line.equals("y") || line.equals("yes")) {
                return true;
            } else if (line.equals("n") || line.equals("no")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }

    public static LocalDate readDate(Scanner input) {
        String date;
        while (true) {
            System.out.print("Enter birthdate (YYYY-MM-DD): ");
            date = input.nextLine().trim();
            try {
                LocalDate birthDay = LocalDate.parse(date);
                LocalDate today = LocalDate.now();
                if (birthDay.isAfter(today)) {
                    System.out.println("Invalid birthdate. Birthdate must not be in the future.");
                    continue;
                }
                if (birthDay.isBefore(today.minusYears(120))) {
                    System.out.println("Invalid birthdate. Birthdate must be within the last 120 years.");
                    continue;
                }
                return birthDay;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
            }
        }
    }



}
