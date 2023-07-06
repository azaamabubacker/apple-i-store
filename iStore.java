import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class iStore implements Serializable {
    private List<Product> products;
    private List<User> users;

    public iStore() {
        this.products = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    // Product related methods
    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product searchProduct(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public List<Product> searchProductByCategory(String category) {
        List<Product> foundProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }



    // User related methods
    public void addUser(User user) {
        users.add(user);
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return users;
    }

    // File handling methods
    public void saveToFile(String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(this);
            System.out.println("Data saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error occurred while saving data to file: " + e.getMessage());
        }
    }

    public void loadFromFile(String fileName) {
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            iStore loadedStore = (iStore) objectInputStream.readObject();
            this.products = loadedStore.products;
            this.users = loadedStore.users;
            System.out.println("Data loaded from file: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while loading data from file: " + e.getMessage());
        }
    }
}
