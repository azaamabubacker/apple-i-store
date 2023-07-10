import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class iStoreGUI extends JFrame {
    private iStore iStore;

    private JButton cashierButton;
    private JButton managerButton;

    private JPanel loginPanel;
    private JPanel mainMenuPanel;
    private JPanel cashierPanel;
    private JPanel managerPanel;

    public iStoreGUI(iStore iStore) {
        this.iStore = iStore;
        setTitle("Apple iStore");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        displayWelcomeScreen();
        createLoginPanel();
    }

    private void displayWelcomeScreen() {
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Apple iStore!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        add(welcomePanel, BorderLayout.CENTER);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        cashierButton = new JButton("Cashier");
        cashierButton.setPreferredSize(new Dimension(150, 50));
        cashierButton.setFont(new Font("Arial", Font.BOLD, 20));
        cashierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPrompt("Cashier");
            }
        });
        loginPanel.add(cashierButton, gbc);

        managerButton = new JButton("Manager");
        managerButton.setPreferredSize(new Dimension(150, 50));
        managerButton.setFont(new Font("Arial", Font.BOLD, 20));
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPrompt("Manager");
            }
        });
        gbc.gridy = 1;
        loginPanel.add(managerButton, gbc);

        add(loginPanel, BorderLayout.CENTER);
    }

    private void showLoginPrompt(String userType) {
        loginPanel.removeAll();
        loginPanel.revalidate();
        loginPanel.repaint();

        JPanel loginPromptPanel = new JPanel(new BorderLayout());
        loginPromptPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(250, 30)); // Set preferred size for username field

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 30)); // Set preferred size for password field

        JButton loginButton = new JButton("Login");

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        loginPromptPanel.add(formPanel, BorderLayout.CENTER);
        loginPromptPanel.add(loginButton, BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (authenticateUser(username, password, userType)) {
                    loginPanel.removeAll();
                    loginPanel.revalidate();
                    loginPanel.repaint();
                    if (userType.equals("Cashier")) {
                        showMainMenu();
                        showCashierPanel();
                    } else if (userType.equals("Manager")) {
                        showMainMenu();
                        showManagerPanel();
                    }
                } else {
                    JOptionPane.showMessageDialog(iStoreGUI.this, "Invalid username or password. Please try again.");
                }
            }
        });

        loginPanel.add(loginPromptPanel, BorderLayout.CENTER);
        loginPanel.revalidate();
        loginPanel.repaint();
    }

    private boolean authenticateUser(String username, String password, String userType) {
        User user = iStore.getUser(username);
        if (user != null && user.getPassword().equals(password) && user.getUserType().equals(userType)) {
            return true;
        }
        return false;
    }

    private void showMainMenu() {
        mainMenuPanel = new JPanel(new BorderLayout());
        mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuPanel.removeAll();
                mainMenuPanel.revalidate();
                mainMenuPanel.repaint();
                createLoginPanel();
            }
        });

        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(logoutButton, BorderLayout.EAST);
        mainMenuPanel.add(menuPanel, BorderLayout.NORTH);

        add(mainMenuPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    private void showSearchByCategoryDialog(JTextArea outputArea) {
        String category = JOptionPane.showInputDialog(iStoreGUI.this, "Enter the category:");
        List<Product> products = iStore.searchProductByCategory(category);
        outputArea.setText("");
        if (!products.isEmpty()) {
            outputArea.append("Products found in the category: " + category + "\n");
            for (Product product : products) {
                outputArea.append("Name: " + product.getName() + "\n");
                outputArea.append("Price: $" + product.getPrice() + "\n");
                outputArea.append("Stock Quantity: " + product.getStockQuantity() + "\n");
                outputArea.append("---------------------\n");
            }
        } else {
            outputArea.append("No products found in the category: " + category + "\n");
        }
    }

    private void showSearchByNameDialog(JTextArea outputArea) {
        String productName = JOptionPane.showInputDialog(iStoreGUI.this, "Enter the product name:");
        Product product = iStore.searchProduct(productName);
        outputArea.setText("");
        if (product != null) {
            outputArea.append("Product found:\n");
            outputArea.append("Name: " + product.getName() + "\n");
            outputArea.append("Category: " + product.getCategory() + "\n");
            outputArea.append("Price: $" + product.getPrice() + "\n");
            outputArea.append("Stock Quantity: " + product.getStockQuantity() + "\n");
        } else {
            outputArea.append("Product not found.\n");
        }
    }

    private void showSearchStockDetailsDialog(JTextArea outputArea) {
        String productName = JOptionPane.showInputDialog(iStoreGUI.this, "Enter the product name:");
        Product product = iStore.searchProduct(productName);
        outputArea.setText("");
        if (product != null) {
            outputArea.append("Stock details for product: " + product.getName() + "\n");
            outputArea.append("Stock Quantity: " + product.getStockQuantity() + "\n");
        } else {
            outputArea.append("Product not found.\n");
        }
    }

    private void showCashierPanel() {
        cashierPanel = new JPanel(new BorderLayout());

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        cashierPanel.add(scrollPane, BorderLayout.CENTER);

        JButton viewAllButton = new JButton("View All Products");
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Product> products = iStore.getProducts();
                outputArea.setText("");
                if (!products.isEmpty()) {
                    outputArea.append("------ All Products ------\n");
                    for (Product product : products) {
                        outputArea.append(product.getName() + " - $" + product.getPrice() + "\n");
                    }
                    outputArea.append("--------------------------\n");
                } else {
                    outputArea.append("No products available.\n");
                }
            }
        });

        JButton searchByNameButton = new JButton("Search for a product by name");
        searchByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchByNameDialog(outputArea);
            }
        });

        JButton searchByCategoryButton = new JButton("Search for products by category");
        searchByCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchByCategoryDialog(outputArea);
            }
        });

        JButton searchStockDetailsButton = new JButton("Search for stock details of a product");
        searchStockDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchStockDetailsDialog(outputArea);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(viewAllButton);
        buttonPanel.add(searchByNameButton);
        buttonPanel.add(searchByCategoryButton);
        buttonPanel.add(searchStockDetailsButton);

        cashierPanel.add(buttonPanel, BorderLayout.NORTH);

        mainMenuPanel.add(cashierPanel, BorderLayout.CENTER);

        setContentPane(mainMenuPanel);
        revalidate();
        repaint();
    }

    private void showManagerPanel() {
        managerPanel = new JPanel(new BorderLayout());

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        managerPanel.add(scrollPane, BorderLayout.CENTER);

        JButton viewAllButton = new JButton("View All Products");
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Product> products = iStore.getProducts();
                outputArea.setText("");
                if (!products.isEmpty()) {
                    outputArea.append("------ All Products ------\n");
                    for (Product product : products) {
                        outputArea.append(product.getName() + " - $" + product.getPrice() + "\n");
                    }
                    outputArea.append("--------------------------\n");
                } else {
                    outputArea.append("No products available.\n");
                }
            }
        });

        JButton searchByNameButton = new JButton("Search for a product by name");
        searchByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchByNameDialog(outputArea);
            }
        });

        JButton searchByCategoryButton = new JButton("Search for products by category");
        searchByCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchByCategoryDialog(outputArea);
            }
        });

        JButton searchStockDetailsButton = new JButton("Search for stock details of a product");
        searchStockDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchStockDetailsDialog(outputArea);
            }
        });

        JButton addProductButton = new JButton("Add a new product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProductDialog();
            }
        });

        JButton addUserButton = new JButton("Add a new user");
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddUserDialog();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.add(viewAllButton);
        buttonPanel.add(searchByNameButton);
        buttonPanel.add(searchByCategoryButton);
        buttonPanel.add(searchStockDetailsButton);
        buttonPanel.add(addProductButton);
        buttonPanel.add(addUserButton);

        managerPanel.add(buttonPanel, BorderLayout.NORTH);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });

        managerPanel.add(logoutButton, BorderLayout.SOUTH);

        mainMenuPanel.add(managerPanel, BorderLayout.CENTER);

        setContentPane(mainMenuPanel);
        revalidate();
        repaint();
    }

    private void showAddProductDialog() {
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockQuantityField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Stock Quantity:"));
        inputPanel.add(stockQuantityField);

        int result = JOptionPane.showConfirmDialog(iStoreGUI.this, inputPanel, "Add New Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String category = categoryField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stockQuantity = Integer.parseInt(stockQuantityField.getText());

            Product product = new Product(name, category, price, stockQuantity);
            iStore.addProduct(product);

            JOptionPane.showMessageDialog(iStoreGUI.this, "New product added successfully!");
        }
    }

    private void showAddUserDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField userTypeField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("User Type:"));
        inputPanel.add(userTypeField);

        int result = JOptionPane.showConfirmDialog(iStoreGUI.this, inputPanel, "Add New User",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String userType = userTypeField.getText();

            User user = new User(username, password, userType);
            iStore.addUser(user);

            JOptionPane.showMessageDialog(iStoreGUI.this, "New user added successfully!");
        }
    }
}
