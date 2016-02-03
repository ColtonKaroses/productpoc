package ckaroses;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by colton on 2/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ProductRepositoryIntegrationTest {

    @Autowired
    ProductRepository productRepository;

    Product product;

    private final String PRODUCT_NAME = "testProduct";
    private final String CATAGORY = "test";
    private final String SKU = "test123";
    private final BigDecimal PRICE = new BigDecimal("2.99");

    @Before
    public void before() {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setCategory(CATAGORY);
        product.setSku(SKU);
        product.setPrice(PRICE);
        this.product = product;
    }

    @After
    public void after() {
        productRepository.deleteAll();
    }

    @Test
    public void addProductTest() {
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
        product.setName(null);
        productRepository.save(product).getName();
    }


    @Test(expected = DataIntegrityViolationException.class)
    public void nullCategoryTest() {
        product.setCategory(null);
        productRepository.save(product);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void nullSkuTest() {
        product.setSku(null);
        productRepository.save(product);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void nullPriceTest() {
        product.setPrice(null);
        productRepository.save(product);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void uniqueSkuTest() {
        productRepository.save(product);
        product.setId(9999L);
        productRepository.save(product);
    }

    @Test
    public void smallPriceTest() {
        product.setPrice(new BigDecimal("0.001"));
        Product id = productRepository.save(product);
        Product storedProduct = productRepository.findOne(id.getId());
        Assert.assertEquals(new BigDecimal("0.00"), storedProduct.getPrice());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void largePriceTest() {
        product.setPrice(new BigDecimal("1234567890123456"));
        productRepository.save(product);
    }

}
