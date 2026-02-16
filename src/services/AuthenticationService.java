package services;

import repositories.EmployeeRepository;
import utils.InputUtilities;

import java.util.Scanner;

public class AuthenticationService {
    static int employeeID = -1;
    static String person;
    static int choice;

    public static int login(Scanner input) {
        System.out.print("Hello this is the Simple Work Management System");
        System.out.println("Would you like to: \n1) Create a profile for an employee \n2) Continue as an existing profile \n3) Exit?");
        choice = InputUtilities.readInt(input, 3);
        switch (choice) {

            case 1 -> {
                newAccount(input);
            }

            case 2 -> {
                existingAccount(input);
            }

            case 3 -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }
        return employeeID;
    }

    public static void newAccount(Scanner input) {
        System.out.println("What is your name?");
        person = input.nextLine().toUpperCase();

        if (EmployeeRepository.personExists(person)) {
            System.out.println("Somebody by that name already exists. Do you have an existing account (y/n)?");
            boolean existing = InputUtilities.readYesNo(input);

            if (existing) {

                while (true) {
                    System.out.println("What is your password?");
                    String password = input.nextLine();

                    employeeID = EmployeeRepository.getPersonId(person, password);

                    if (employeeID == -1){
                        System.out.println("The Password you entered is incorrect (if you cannot remember your password get a administrator to change it). Would you like to: \n1) Try again \n2) Exit");
                        choice = InputUtilities.readInt(input, 2);
                        if (choice == 2) {
                            System.exit(0);
                        }
                    }
                    else{
                        break;
                    }
                }

            }
            else{
                accountCreation(input);
            }
        }
        else{
            accountCreation(input);
        }
    }
    public static void accountCreation(Scanner input) {
        int newId = EmployeeRepository.addPerson(person, input, true);
        if (newId != -1) {
            employeeID = newId;
        }
        System.out.println("Would you like to: \n1) continue \n2) exit?");
        choice = InputUtilities.readInt(input, 2);
        if (choice == 2) {
            System.exit(0);
        }
    }
    public static void existingAccount(Scanner input){
        while(true){

            System.out.println("What is your name?");
            person = input.nextLine().toUpperCase();

            if (!EmployeeRepository.personExists(person)) {
                System.out.println("User not found. Would you like to: \n1) Try again  \n2) Create a new profile.");
                choice = InputUtilities.readInt(input, 2);
                if  (choice == 2) {
                    int newId = EmployeeRepository.addPerson(person, input, true);
                    if (newId != -1) {
                        employeeID = newId;
                    }
                    System.out.println("Would you like to: \n1) continue \n2) exit?");
                    choice = InputUtilities.readInt(input, 2);
                    if (choice == 1) {
                        break;
                    }
                    else{
                        System.exit(0);
                    }
                }
            }
            else{
                while (true) {
                    System.out.println("What is your password?");
                    String password = input.nextLine();
                    employeeID = EmployeeRepository.getPersonId(person, password);
                    if (employeeID == -1){
                        System.out.println("The Password you entered is incorrect (if you cannot remember your password get a administrator to change it). Would you like to: \n1) Try again \n2) Exit");
                        choice = InputUtilities.readInt(input, 2);
                        if  (choice == 2) {
                            System.exit(0);
                        }
                    }
                    else{
                        break;
                    }
                }
                break;
            }
        }
    }
}
