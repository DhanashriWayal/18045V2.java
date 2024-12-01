import java.util.Scanner;
import java.util.Vector;

class ProductService {
    private static Product[] products = {
        new Product("Pen", 10),
        new Product("Notebook", 50),
        new Product("Eraser", 5),
        new Product("Marker", 15),
        new Product("Folder", 20),
        new Product("Pencil", 5),
        new Product("Highlighter", 20),
        new Product("Stapler", 55),
        new Product("Glue", 25),
        new Product("Scissors", 60)
    };

    public static Product[] getProducts() {
        return products;
    }

    public static Product getProduct(int index) {
        if (index >= 0 && index < products.length) {
            return products[index];
        }
        return null;
    }

    public static void displayProducts() {
        System.out.println("Available products:");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i]);
        }
    }
}

class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPriceInINR() {
        return price;
    }

    public String toString() {
        return name + ": " + formatCurrency(getPriceInINR());
    }

    private String formatCurrency(int amount) {
        return "Rupees " + String.format("%,d", amount);
    }
}

class Customer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPriceInINR() {
        return product.getPriceInINR() * quantity;
    }

    public String toString() {
        return product.getName() + " (x" + quantity + "): " + formatCurrency(getTotalPriceInINR());
    }

    private String formatCurrency(int amount) {
        return "Rupees " + String.format("%,d", amount);
    }
}

class Order {
    private Customer customer;
    private Vector<OrderItem> orderItems;
    private int totalAmount = 0;

    public Order(Customer customer) {
        this.customer = customer;
        this.orderItems = new Vector<>();
    }

    public void addProduct(Product product, int quantity) {
        orderItems.add(new OrderItem(product, quantity));
        totalAmount += product.getPriceInINR() * quantity;
    }

    public void removeProduct(String productName) {
        boolean found = false;
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem item = orderItems.get(i);
            if (item.getProduct().getName().equalsIgnoreCase(productName)) {
                orderItems.remove(i);
                totalAmount -= item.getTotalPriceInINR();
                System.out.println("Removed " + item.getQuantity() + " x " + productName + " from your order.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Product \"" + productName + "\" not found in your order.");
        }
    }

    public void printOrder() {
        System.out.println("Order for " + customer.getName() + ":");
        for (OrderItem item : orderItems) {
            System.out.println(item);
        }
        System.out.println("Total Amount: " + formatCurrency(totalAmount));
    }

    private String formatCurrency(int amount) {
        return "Rupees " + String.format("%,d", amount);
    }
}

public class StationeryShopB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        Customer customer = new Customer(customerName);
        Order order = new Order(customer);

        ProductService.displayProducts();
        int productLimit = 50;
        int selectedProducts = 0;
        boolean finished = false;

        while (!finished && selectedProducts < productLimit) {
            System.out.println("\nMenu:");
            System.out.println("1. Add product");
            System.out.println("2. Remove product");
            System.out.println("3. View order");
            System.out.println("4. Finish order");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1: // Add product
                    System.out.print("Select a product by number (or type 0 to finish): ");
                    int choice = scanner.nextInt();
                    if (choice == 0) {
                        break;
                    } else if (choice > 0 && choice <= ProductService.getProducts().length) {
                        System.out.print("Enter quantity for " + ProductService.getProduct(choice - 1).getName() + ": ");
                        int quantity = scanner.nextInt();
                        if (quantity > 0) {
                            order.addProduct(ProductService.getProduct(choice - 1), quantity);
                            selectedProducts++;
                            System.out.println("Added " + quantity + " x " + ProductService.getProduct(choice - 1).getName() + " to your order.");
                        } else {
                            System.out.println("Quantity must be greater than 0.");
                        }
                    } else {
                        System.out.println("Invalid choice, please select a valid product.");
                    }
                    break;

                case 2: // Remove product
                    System.out.print("Enter the name of the product to remove: ");
                    String productName = scanner.nextLine();
                    order.removeProduct(productName);
                    break;

                case 3: // View order
                    order.printOrder();
                    break;

                case 4: // Finish order
                    finished = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }

            if (selectedProducts == productLimit) {
                System.out.println("Product limit reached. You can select a maximum of " + productLimit + " products.");
                finished = true;
            }
        }

        System.out.println("\nFinal Order:");
        order.printOrder();
        scanner.close();
    }
}
