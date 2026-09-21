/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dispatcher;

import business.Customers;
import business.Orders;
import business.SetMenus;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.Customer;
import model.Order;
import model.SetMenu;
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
    private static SetMenus menuList = new SetMenus();
    private static Orders orderList = new Orders();

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

    // FUNCTION - 3
    public static void searchCustomerByName(Customers customerList) {
        System.out.println("\n=== SEARCH CUSTOMER BY NAME ===");
        String keyword = ndl.getString("Enter customer name [or part of name]: ");

        List<Customer> result = customerList.filterByName(keyword);

        if (result.isEmpty()) {
            System.out.println("No one matches the search criteria!");
        } else {
            System.out.println("Matching Customers");
            customerList.showAll(result);
        }
    }

    // FUNCTION - 4
    public static void displayFeastMenus(SetMenus menuList) {
        menuList.showMenuList();
    }

    private static void printOrderReceipt(Order o, Customer c, SetMenu m, double totalCost) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Customer order information [Order ID: " + o.getOrderCode() + "]");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Code           : " + c.getId());
        System.out.println("Customer name  : " + c.getName());
        System.out.println("Phone number   : " + c.getPhone());
        System.out.println("Email          : " + c.getEmail());
        System.out.println("----------------------------------------------------------------");
        System.out.println("Code of Set Menu: " + m.getMenuId());
        System.out.println("Set menu name  : " + m.getMenuName());
        System.out.println("Event date     : " + df.format(o.getEventDate()));
        System.out.println("Number of tables: " + o.getNumOfTables());
        System.out.println("Price          : " + String.format("%,.0f", m.getPrice()) + " Vnd");
        System.out.println("Ingredients:");
        System.out.println(m.getIngredients());
        System.out.println("----------------------------------------------------------------");
        System.out.println("Total cost     : " + String.format("%,.0f", totalCost) + " Vnd");
        System.out.println("----------------------------------------------------------------");
    }

    // FUNCTION - 5
    public static void placeFeastOrder(Customers customerList, SetMenus menuList, Orders orderList) {
        boolean isContinue = true;
        do {
            System.out.println("\n=== PLACE A FEAST ORDER ===");

            Order newOrder = new Order();
            ndl.inputOrderInfo(newOrder, customerList, menuList, orderList, false);
            if (orderList.isDuplicate(newOrder)) {
                System.out.println("Dupplicate data !");
            } else {
                newOrder.setOrderCode((orderList.size() + 1) + "");
                orderList.add(newOrder);
                Customer customer = customerList.searchById(newOrder.getCustomerId());
                SetMenu menu = menuList.searchById(newOrder.getMenuId());
                double totalCost = menu.getPrice() * newOrder.getNumOfTables();
                printOrderReceipt(newOrder, customer, menu, totalCost);
            }

            String choice = ndl.getString("\nContinue placing another order? (Y/N): ");
            isContinue = choice.equalsIgnoreCase("Y");
        } while (isContinue);
    }

    // FUNCTION - 6
    public static void updateOrder(Orders orderList, Customers customerList, SetMenus menuList) {
        boolean isContinue = true;

        do {
            System.out.println("\n=== UPDATE ORDER INFORMATION ===");
            String orderId = ndl.getString("Enter Order ID: ");
            Order old = orderList.searchById(orderId);

            if (old == null) {
                System.out.println("This Order does not exist.");
            } else {
                if (old.getEventDate().before(new Date())) {
                    System.out.println("Cannot update an order whose event date has already passed.");
                } else {
                    System.out.println("Current information:");
                    System.out.println(old);

                    ndl.inputOrderInfo(old, customerList, menuList, orderList, true); // chi hoi Tables/Date

                    orderList.update(old);
                    System.out.println("\nUpdate successful !");
                    System.out.println(old);
                }
            }

            String choice = ndl.getString("\nContinue updating another order? (Y/N): ");
            isContinue = choice.equalsIgnoreCase("Y");

        } while (isContinue);
    }

    // FUNCTION - 7
    public static void saveData() {
        customerList.saveToFile();
        System.out.println("Customer data has been successfully saved to \"customers.dat\".");
        orderList.saveToFile();
        System.out.println("Order data has been successfully saved to \"feast_order_service.dat\".");
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
                orderList.showAll(menuList);  // rỗng -> "No data in the system."
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
                    searchCustomerByName(customerList);
                    break;
                case 4:
                    // TODO: Function 4 - Display feast menus
                    displayFeastMenus(menuList);
                    break;
                case 5:
                    // TODO: Function 5 - Place a feast order
                    placeFeastOrder(customerList, menuList, orderList);
                    break;
                case 6:
                    updateOrder(orderList, customerList, menuList);
                    System.out.println("Feature 6 is not implemented yet.");
                    break;
                case 7:
                    saveData();
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
