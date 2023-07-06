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


}
