package ckaroses.products;

import ckaroses.Application;
import ckaroses.products.Product;
import ckaroses.products.ProductRepository;
import ckaroses.products.ProductService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Created by colton on 2/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class ProductServiceIntegrationTest {


    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    private final String PRODUCT_NAME = "testProduct";
    private final String CATAGORY = "test";
    private final String SKU = "test123";
    private final BigDecimal PRICE = new BigDecimal("2.99");

    @After
    public void after() {
        productRepository.deleteAll();
    }

    @Test
    public void addProductTest() {
        Product product = new Product(SKU, PRODUCT_NAME, CATAGORY, PRICE);
        try {
            productService.addProduct(product);
            Product storedProduct = productService.getProduct(product.getId());
            Assert.assertEquals(PRODUCT_NAME, storedProduct.getName());
            Assert.assertEquals(CATAGORY, storedProduct.getCategory());
            Assert.assertEquals(SKU, storedProduct.getSku());
            Assert.assertEquals(PRICE, storedProduct.getPrice());
        } catch (Exception e) {
            Assert.fail("Product failed to be stored");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullNameTest() {
        Product product = new Product(SKU, null, CATAGORY, PRICE);
        productService.addProduct(product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullCategoryTest() {
        Product product = new Product(SKU, PRODUCT_NAME, null, PRICE);
        productService.addProduct(product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSkuTest() {
        Product product = new Product(null, PRODUCT_NAME, CATAGORY, PRICE);
        productService.addProduct(product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPriceTest() {
        Product product = new Product(SKU, PRODUCT_NAME, CATAGORY, null);
        productService.addProduct(product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullGetProductIdTest() {
        productService.getProduct(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullGetProductsByNullCategoryTest() {
        productService.getByCategory(null);
    }
}
