package ckaroses;

import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by colton on 2/1/16.
 */

@Entity
public class Product {

    public Product() {}

    public Product(String sku, String name, String category, BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_id_seq")
    @SequenceGenerator(name="product_id_seq", sequenceName="product_id_seq", allocationSize=1)
    private long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    @LastModifiedDate
    private Timestamp lastUpdated;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @PrePersist
    public void updateLastUpdated() {
        lastUpdated = new Timestamp(System.currentTimeMillis());
    }

    public long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public Number getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product)) {
            return false;
        }

        Product product = (Product) o;
        if (id != product.getId() ||
                !sku.equals(product.getSku()) ||
                !name.equalsIgnoreCase(product.getName()) ||
                !category.equalsIgnoreCase(product.getCategory()) ||
                !price.equals(product.getPrice())) {
            return false;
        }
        return true;

    }
}
