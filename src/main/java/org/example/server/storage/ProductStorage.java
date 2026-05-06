package org.example.server.storage;

import org.example.common.model.Product;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductStorage {
    private List<Product> products = new ArrayList<>();
    private int nextId = 1;
    private final String FILE = "products.json";

    public void addProduct(Product product) {
        product.setId(nextId++);
        products.add(product);
    }

    public List<Product> getSortedProducts() {
        return products.stream()
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .collect(Collectors.toList());
    }

    public void clearCollection() {
        products.clear();
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(products);
            oos.writeInt(nextId);
        } catch (Exception ignored) {}
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            products = (List<Product>) ois.readObject();
            nextId = ois.readInt();
        } catch (Exception ignored) {}
    }
}