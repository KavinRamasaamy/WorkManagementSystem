package services;

import repositories.EmployeeRepository;
import utils.InputUtilities;

import java.util.Scanner;

public class MenuService {

    public static void menuSelection(int employeeID,  Scanner input) {
        if (EmployeeRepository.isAdmin(employeeID)) {
            adminMenu(employeeID, input);
        } else {
            employeeMenu(employeeID, input);
        }
    }

    public static void adminMenu(int employeeID,  Scanner input){

        System.out.println("Administrator access detected: \n1) Edit employee information \n2) Record my shift \n3) Exit?");

        int choice = InputUtilities.readInt(input, 3);

        switch (choice) {
            case 1 -> EmployeeRepository.editingInformation(input);
            case 2 -> PayrollService.recordShift(employeeID, input);
            case 3 -> System.exit(0);
        }
    }
    public static void employeeMenu(int employeeID,  Scanner input){
        System.out.println("Would you like to: \n1) Start a new shift \n2) Exit");

        int choice = InputUtilities.readInt(input, 2);

        switch (choice) {
            case 1 -> PayrollService.recordShift(employeeID, input);
            case 2 -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }
    }

}
