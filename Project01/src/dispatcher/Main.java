/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dispatcher;

import business.Customers;
import model.Customer;
import tools.Acceptable;
import tools.Inputter;

/**
 *
 * @author Le Nhat Tung
 */
public class Main {

    private static Inputter ndl = new Inputter();
    // Danh sach du lieu dung chung cho toan bo chuong trinh
    private static Customers customerList = new Customers();

    /**
     * In ra menu chinh cua chuong trinh
     */
    private static void showMainMenu() {
        System.out.println("\n===================================================");
        System.out.println("     TRADITIONAL FEAST ORDER MANAGEMENT SYSTEM");
        System.out.println("===================================================");
        System.out.println("1. Register customers");
        System.out.println("2. Update customer information");
        System.out.println("3. Search for customer information by name");
        System.out.println("4. Display feast menus");
        System.out.println("5. Place a feast order");
        System.out.println("6. Update order information");
        System.out.println("7. Save data to file");
        System.out.println("8. Display Customer or Order lists");
        System.out.println("0. Quit");
        System.out.println("===================================================");
    }

    // FUNCTION - 1
    public static void registerCustomer(Customers customerList) {
        boolean isContinue = true;

        do {
            System.out.println("\n=== REGISTER NEW CUSTOMER ===");

            // --- 1&2. Nhap va kiem tra ma khach hang: dung ky tu C/G/K + 4 chu so, VA phai la duy nhat ---
            String id;
            do {
                id = ndl.inputAndLoop("Customer code [C/G/K + 4 digits]: ", Acceptable.CUS_ID_VALID);
                id = id.toUpperCase(); // chuan hoa ve chu hoa de luu tru nhat quan
                if (customerList.isExist(id)) {
                    System.out.println("Customer code already exists ! Please enter a different code.");
                }
            } while (customerList.isExist(id));

            Customer newCustomer = new Customer();
            newCustomer.setId(id);

            // Name/Phone/Email nhap qua Inputter - dung chung code voi Function 2
            ndl.inputCustomerInfo(newCustomer, false);

            customerList.addNew(newCustomer);

            System.out.println("\nRegistration successful !");
            System.out.println(newCustomer);

            // --- 4. Hoi nguoi dung tiep tuc them KH moi hay quay ve menu chinh ---
            String choice = ndl.getString("\nContinue adding new customer? (Y/N): ");
            isContinue = choice.equalsIgnoreCase("Y");

        } while (isContinue);
    }

    // FUNCTION - 2
    public static void updateCustomer(Customers customerList) {
        boolean isContinue = true;

        do {
            System.out.println("\n=== UPDATE CUSTOMER INFORMATION ===");
            String id = ndl.getString("Enter Customer Code: ");
            Customer old = customerList.searchById(id);

            if (old == null) {
                System.out.println("This customer does not exist");
            } else {
                System.out.println("Current information");
                System.out.println(old);

                // Name/Phone/Email cap nhat qua Inputter - dung chung code voi Function 1
                ndl.inputCustomerInfo(old, true);

                customerList.update(old);
                System.out.println("\nUpdate successful");
                System.out.println(old);
            }

            String choice = ndl.getString("\nContinue updating another customer? (Y/N): ");
            isContinue = choice.equalsIgnoreCase("Y");

        } while (isContinue);
    }

    // FUNCTION - 8 
    private static void displayLists(Customers customerList) {
        System.out.println("\n--- DISPLAY LISTS ---");
        System.out.println("1. Display Customer list");
        System.out.println("2. Display Order list");
        int choice = ndl.getInt("Enter your choice: ");

        switch (choice) {
            case 1:
                System.out.println("\nCustomers information:");
                customerList.showAll();
                break;
            case 2:
                // TODO: Orders class chua duoc xay dung - se bo sung sau
                System.out.println("Order list feature is not implemented yet.");
                break;
            default:
                System.out.println("Invalid choice !");
        }
    }

    public static void main(String[] args) {

        // --- Nap du lieu tu file khi chuong trinh khoi dong ---
        boolean isRunning = true;

        do {
            showMainMenu();
            int choice = ndl.getInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerCustomer(customerList);
                    break;
                case 2:
                    // TODO: Function 2 - Update customer information
                    updateCustomer(customerList);
                    break;
                case 3:
                    // TODO: Function 3 - Search for customer information by name
                    System.out.println("Feature 3 is not implemented yet.");
                    break;
                case 4:
                    // TODO: Function 4 - Display feast menus
                    System.out.println("Feature 4 is not implemented yet.");
                    break;
                case 5:
                    // TODO: Function 5 - Place a feast order
                    System.out.println("Feature 5 is not implemented yet.");
                    break;
                case 6:
                    // TODO: Function 6 - Update order information
                    System.out.println("Feature 6 is not implemented yet.");
                    break;
                case 7:
                    // Function 7 - Save data to file
                    break;
                case 8:
                    displayLists(customerList);
                    break;
                case 0:
                    isRunning = false;
                    // Luu du lieu truoc khi thoat, tranh mat du lieu chua ghi
                    System.out.println("Goodbye !");
                    break;
                default:
                    System.out.println("Invalid choice ! Please select again.");
            }

        } while (isRunning);
    }
}
