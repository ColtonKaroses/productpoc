package ckaroses.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by colton on 2/2/16.
 */

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Iterable<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        Assert.notNull(id, "Id cannot be null");
        return productRepository.findOne(id);
    }

    @Override
    public void addProduct(Product product) {
        Assert.notNull(product.getCategory(), "Category cannot be null");
        Assert.notNull(product.getName(), "Name cannot be null");
        Assert.notNull(product.getPrice(), "Price cannot be null");
        Assert.notNull(product.getSku(), "SKU cannot be null");
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Assert.notNull(id, "Id cannot be null");
        productRepository.delete(id);
    }

    @Override
    public Iterable<Product> getByCategory(String category) {
        Assert.notNull(category, "Category cannot be null");
        return productRepository.findByCategory(category);
    }
}
