import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        iStore iStore = new iStore();
        loadUserData();
        loadProductData(iStore);
        Scanner scanner = new Scanner(System.in);
        displayUserTypeMenu(iStore, scanner);
        saveUserData();
        saveProductData(iStore);
        scanner.close();
    }

    private static void loadUserData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    String username = userData[0];
                    String password = userData[1];
                    String userType = userData[2];
                    users.add(new User(username, password, userType));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }

    private static void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User user : users) {
                String line = user.getUsername() + "," + user.getPassword() + "," + user.getUserType();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    private static void loadProductData(iStore iStore) {
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split(",");
                if (productData.length == 4) {
                    String name = productData[0];
                    String category = productData[1];
                    double price = Double.parseDouble(productData[2]);
                    int stockQuantity = Integer.parseInt(productData[3]);
                    iStore.addProduct(new Product(name, category, price, stockQuantity));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading product data: " + e.getMessage());
        }
    }

    private static void saveProductData(iStore iStore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            List<Product> products = iStore.getProducts();
            for (Product product : products) {
                String line = product.getName() + "," + product.getCategory() + "," +
                        product.getPrice() + "," + product.getStockQuantity();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving product data: " + e.getMessage());
        }
    }

    private static void displayUserTypeMenu(iStore iStore, Scanner scanner) {
        int choice;
        String userType;

        do {
            System.out.println("**************************************");
            System.out.println("*                                    *");
            System.out.println("*     Welcome to Apple iStore!       *");
            System.out.println("*                                    *");
            System.out.println("**************************************");
            System.out.println("");
            System.out.print("Enter user type (Cashier/Manager) or type quit: ");
            userType = scanner.nextLine();

            if (userType.equalsIgnoreCase("quit")) {
                System.out.println("Exiting the program.");
                break;
            } else if (userType.equalsIgnoreCase("Cashier")) {
                if (authenticateUser(scanner)) {
                    while (true) {
                        System.out.println("-------- Cashier Menu ---------");
                        System.out.println("1. View all products");
                        System.out.println("2. Search for a product by name");
                        System.out.println("3. Search for products by category");
                        System.out.println("4. Search for stock details of a product");
                        System.out.println("5. Quit");
                        System.out.print("Enter your choice: ");
                        choice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        switch (choice) {
                            case 1:
                                displayAllProducts(iStore);
                                break;
                            case 2:
                                searchProductByName(scanner, iStore);
                                break;
                            case 3:
                                searchProductsByCategory(scanner, iStore);
                                break;
                            case 4:
                                searchStockDetails(scanner, iStore);
                                break;
                            case 5:
                                System.out.println("Returning to the user type menu.");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }

                        if (choice == 5) {
                            break; // Exit the cashier menu loop
                        }
                    }
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                }
            } else if (userType.equalsIgnoreCase("Manager")) {
                if (authenticateUser(scanner)) {
                    while (true) {
                        System.out.println("-------- Manager Menu ---------");
                        System.out.println("1. View all products");
                        System.out.println("2. Search for a product by name");
                        System.out.println("3. Search for products by category");
                        System.out.println("4. Search for stock details of a product");
                        System.out.println("5. Add a new product");
                        System.out.println("6. Add a new user");
                        System.out.println("7. Quit");
                        System.out.print("Enter your choice: ");
                        choice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        switch (choice) {
                            case 1:
                                displayAllProducts(iStore);
                                break;
                            case 2:
                                searchProductByName(scanner, iStore);
                                break;
                            case 3:
                                searchProductsByCategory(scanner, iStore);
                                break;
                            case 4:
                                searchStockDetails(scanner, iStore);
                                break;
                            case 5:
                                addNewProduct(scanner, iStore);
                                break;
                            case 6:
                                addNewUser(scanner);
                                break;
                            case 7:
                                System.out.println("Returning to the user type menu.");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }

                        if (choice == 7) {
                            break; // Exit the manager menu loop
                        }
                    }
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                }
            } else {
                System.out.println("Invalid user type. Please try again or type quit to exit the program.");
            }
        } while (true);
    }

    private static boolean authenticateUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    private static void displayAllProducts(iStore iStore) {
        System.out.println("------ All Products ------");
        List<Product> products = iStore.getProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            for (Product product : products) {
                System.out.println(product.getName() + " - $" + product.getPrice());
            }
        }
        System.out.println("--------------------------");
    }

    private static void searchProductByName(Scanner scanner, iStore iStore) {
        System.out.print("Enter the product name: ");
        String productName = scanner.nextLine();
        Product product = iStore.searchProduct(productName);
        if (product != null) {
            System.out.println("Product found:");
            System.out.println("Name: " + product.getName());
            System.out.println("Category: " + product.getCategory());
            System.out.println("Price: $" + product.getPrice());
            System.out.println("Stock Quantity: " + product.getStockQuantity());
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void searchProductsByCategory(Scanner scanner, iStore iStore) {
        System.out.print("Enter the category: ");
        String category = scanner.nextLine();
        List<Product> products = iStore.searchProductByCategory(category);
        if (products.isEmpty()) {
            System.out.println("No products found in the category: " + category);
        } else {
            System.out.println("Products found in the category: " + category);
            for (Product product : products) {
                System.out.println("Name: " + product.getName());
                System.out.println("Price: $" + product.getPrice());
                System.out.println("Stock Quantity: " + product.getStockQuantity());
                System.out.println("---------------------");
            }
        }
    }

    private static void searchStockDetails(Scanner scanner, iStore iStore) {
        System.out.print("Enter the product name: ");
        String productName = scanner.nextLine();
        Product product = iStore.searchProduct(productName);
        if (product != null) {
            System.out.println("Stock details for product: " + productName);
            System.out.println("Stock Quantity: " + product.getStockQuantity());
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void addNewProduct(Scanner scanner, iStore iStore) {
        System.out.print("Enter the product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the product category: ");
        String category = scanner.nextLine();
        System.out.print("Enter the product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter the stock quantity: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        Product product = new Product(name, category, price, stockQuantity);
        iStore.addProduct(product);

        System.out.println("New product added successfully!");
    }

    private static void addNewUser(Scanner scanner) {
        System.out.print("Enter the username: ");
        String username = scanner.nextLine();
        System.out.print("Enter the password: ");
        String password = scanner.nextLine();
        System.out.print("Enter the user type: ");
        String userType = scanner.nextLine();

        users.add(new User(username, password, userType));

        System.out.println("New user added successfully!");
    }
}
