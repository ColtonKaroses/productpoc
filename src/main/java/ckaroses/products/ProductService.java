package ckaroses.products;

/**
 * Created by colton on 2/2/16.
 */
public interface ProductService {

    public Iterable<Product> getProducts();

    public Product getProduct(Long id);

    public void addProduct(Product product);

    public void deleteProduct(Long id);

    public Iterable<Product> getByCategory(String category);
}
