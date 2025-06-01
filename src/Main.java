import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName;
    private double price;
    private int quantity;
    private String customerName;
    private String city;
    private String deliveryMethod;
    private boolean isPaid;

    public Order(String productName, double price, int quantity, String customerName, String city, String deliveryMethod, boolean isPaid) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.customerName = customerName;
        this.city = city;
        this.deliveryMethod = deliveryMethod;
        this.isPaid = isPaid;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public String getCity() {
        return city;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    @Override
    public String toString() {
        return String.format("Товар: %s | Ціна: %.2f | Кількість: %d | Замовник: %s | Місто: %s | Доставка: %s | Оплата: %s",
                productName, price, quantity, customerName, city, deliveryMethod, isPaid ? "Так" : "Ні");
    }
}

public class Main {
    private static final String FILE_NAME = "orders.dat";
    private static List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {
        loadOrders(); // Завантажуємо замовлення з файлу
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Додати замовлення");
            System.out.println("2. Вивести всі замовлення");
            System.out.println("3. Показати оплачені замовлення для міста");
            System.out.println("4. Показати неоплачені замовлення за способом доставки");
            System.out.println("5. Вийти");

            System.out.print("Оберіть дію: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Очищення буфера

            switch (choice) {
                case 1 -> addOrder(scanner);
                case 2 -> displayOrders();
                case 3 -> filterPaidOrders(scanner);
                case 4 -> filterUnpaidOrders(scanner);
                case 5 -> {
                    saveOrders();
                    System.out.println("Завершення роботи...");
                    return;
                }
                default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void addOrder(Scanner scanner) {
        System.out.print("Введіть назву товару: ");
        String productName = scanner.nextLine();

        System.out.print("Введіть ціну: ");
        double price = scanner.nextDouble();

        System.out.print("Введіть кількість: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Очищення буфера

        System.out.print("Введіть ПІБ замовника: ");
        String customerName = scanner.nextLine();

        System.out.print("Введіть місто доставки: ");
        String city = scanner.nextLine();

        System.out.print("Введіть спосіб доставки: ");
        String deliveryMethod = scanner.nextLine();

        System.out.print("Чи оплачене замовлення? (true/false): ");
        boolean isPaid = scanner.nextBoolean();

        orders.add(new Order(productName, price, quantity, customerName, city, deliveryMethod, isPaid));
        System.out.println("Замовлення додано!");
    }

    private static void displayOrders() {
        if (orders.isEmpty()) {
            System.out.println("Замовлення відсутні.");
        } else {
            orders.forEach(System.out::println);
        }
    }

    private static void filterPaidOrders(Scanner scanner) {
        System.out.print("Введіть місто для пошуку оплачених замовлень: ");
        String city = scanner.nextLine();

        List<Order> filteredOrders = orders.stream()
                .filter(order -> order.isPaid() && order.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());

        if (filteredOrders.isEmpty()) {
            System.out.println("Оплачених замовлень у цьому місті немає.");
        } else {
            filteredOrders.forEach(System.out::println);
        }
    }

    private static void filterUnpaidOrders(Scanner scanner) {
        System.out.print("Введіть спосіб доставки для пошуку неоплачених замовлень: ");
        String deliveryMethod = scanner.nextLine();

        List<Order> filteredOrders = orders.stream()
                .filter(order -> !order.isPaid() && order.getDeliveryMethod().equalsIgnoreCase(deliveryMethod))
                .collect(Collectors.toList());

        if (filteredOrders.isEmpty()) {
            System.out.println("Неоплачених замовлень з таким способом доставки немає.");
        } else {
            filteredOrders.forEach(System.out::println);
        }
    }

    private static void saveOrders() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(orders);
            System.out.println("Дані успішно збережено у файл.");
        } catch (IOException e) {
            System.out.println("Помилка при збереженні файлу: " + e.getMessage());
        }
    }

    private static void loadOrders() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            orders = (List<Order>) in.readObject();
            System.out.println("Дані успішно завантажено з файлу.");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не знайдено, починаємо з пустої бази.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Помилка при завантаженні файлу: " + e.getMessage());
        }
    }
}
