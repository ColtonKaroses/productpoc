package ckaroses.products;

import ckaroses.Application;
import ckaroses.products.Product;
import ckaroses.products.ProductRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Created by colton on 2/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class ProductRepositoryIntegrationTest {

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
            Product id = productRepository.save(product);
            Product storedProduct = productRepository.findOne(id.getId());
            Assert.assertEquals(PRODUCT_NAME, storedProduct.getName());
            Assert.assertEquals(CATAGORY, storedProduct.getCategory());
            Assert.assertEquals(SKU, storedProduct.getSku());
            Assert.assertEquals(PRICE, storedProduct.getPrice());
        } catch (Exception e) {
            Assert.fail("Product failed to be stored");
        }
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void nullNameTest() {
        Product product = new Product(SKU, null, CATAGORY, PRICE);
        productRepository.save(product).getName();
    }


    @Test(expected = DataIntegrityViolationException.class)
    public void nullCategoryTest() {
        Product product = new Product(SKU, PRODUCT_NAME, null, PRICE);
        productRepository.save(product);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void nullSkuTest() {
        Product product = new Product(null, PRODUCT_NAME, CATAGORY, PRICE);
        productRepository.save(product);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void nullPriceTest() {
        Product product = new Product(SKU, PRODUCT_NAME, CATAGORY, null);
        productRepository.save(product);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void uniqueSkuTest() {
        Product product = new Product(SKU, PRODUCT_NAME, CATAGORY, PRICE);
        productRepository.save(product);

        Product product2 = new Product(SKU, PRODUCT_NAME, CATAGORY, PRICE);
        productRepository.save(product2);
    }

    @Test
    public void smallPriceTest() {
        Product product = new Product(SKU, PRODUCT_NAME, CATAGORY, new BigDecimal("0.001"));
        Product id = productRepository.save(product);
        Product storedProduct = productRepository.findOne(id.getId());
        Assert.assertEquals(new BigDecimal("0.00"), storedProduct.getPrice());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void largePriceTest() {
        Product product = new Product(SKU, PRODUCT_NAME, CATAGORY, new BigDecimal("1234567890123456"));
        productRepository.save(product);
    }

}
