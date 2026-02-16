import dataConfig.Database;
import services.AuthenticationService;
import services.MenuService;

import java.util.Scanner;

public class Application {

    private final Scanner input = new Scanner(System.in);

    public void start(){
        Database.init();

        int employeeID = AuthenticationService.login(input);

        MenuService.menuSelection(employeeID, input);

        System.out.println("Goodbye!");
        input.close();
    }
}
