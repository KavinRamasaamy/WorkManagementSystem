package utils;

import java.util.Scanner;

public class InputUtilities {

    // returns viable number
    public static int readInt(Scanner input, int maxOption) {
        int num = -1;
        while (true) {
            String line = input.nextLine().trim();
            try {
                num = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
                continue;
            }
            if (num < 0 || num > maxOption) {
                System.out.println("Invalid number. Please enter a number between 0 and "  + maxOption + ".");
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

}
