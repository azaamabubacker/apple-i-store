import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        iStore iStore = new iStore();
        loadUserData(iStore);
        loadProductData(iStore);

        iStoreGUI iStoreGUI = new iStoreGUI(iStore);
        iStoreGUI.setVisible(true);
    }

    private static void loadUserData(iStore iStore) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    String username = userData[0];
                    String password = userData[1];
                    String userType = userData[2];
                    User user = new User(username, password, userType);
                    users.add(user);
                    iStore.addUser(user);
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
                    Product product = new Product(name, category, price, stockQuantity);
                    iStore.addProduct(product);
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

    public static void updateUserData(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                saveUserData();
                return;
            }
        }
    }

    public static void updateProductData(Product product) {
        iStore iStore = new iStore();
        List<Product> products = iStore.getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(product.getName())) {
                products.set(i, product);
                saveProductData(iStore);
                return;
            }
        }
    }
}
