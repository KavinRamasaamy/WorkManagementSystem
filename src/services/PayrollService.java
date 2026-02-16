package services;

import repositories.EmployeeRepository;
import repositories.ShiftRepository;

import java.util.Scanner;

public class PayrollService {
    public static void recordShift(int employeeID, Scanner input) {

        System.out.println("Hello, " + EmployeeRepository.getName(employeeID) + ", press Enter when you start your shift.");
        input.nextLine();

        long startTime = System.currentTimeMillis();

        System.out.println("Okay, press Enter when you are done your shift.");
        input.nextLine();

        long endTime = System.currentTimeMillis();

        ShiftRepository.insertShift(employeeID, startTime, endTime);

        long durationMs = endTime - startTime;
        long totalSeconds = durationMs / 1000;

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        double hoursWorked = totalSeconds / 3600.0;

        float wage = EmployeeRepository.getWage(employeeID);
        float earnedPay = (float) Math.round(hoursWorked * wage * 100) / 100;


        ShiftRepository.addToUnpaidSalary(employeeID, earnedPay);

        System.out.printf(
                "Shift recorded. Time worked: %02d:%02d:%02d | Earned: $%.2f%n",
                hours, minutes, seconds, earnedPay
        );
    }
}
