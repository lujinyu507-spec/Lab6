package org.example.common.model;
import java.io.Serializable;

public class Product implements Serializable, Comparable<Product> {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;

    public Product(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) { this.id = id; }
    public String getName() { return name; }

    @Override
    public int compareTo(Product o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "'}";
    }
}