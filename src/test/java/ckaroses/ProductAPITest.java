package ckaroses;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by colton on 2/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest(randomPort = true)
public class ProductAPITest {

    @Autowired
    ProductRepository productRepository;

    RestTemplate restTemplate = new TestRestTemplate();

    private final String PRODUCT_NAME = "testProduct";
    private final String CATAGORY = "test";
    private final String SKU = "test123";
    private final Timestamp DATETIME = new Timestamp(0);
    private final BigDecimal PRICE = new BigDecimal("2.99");

    @Before
    public void before() {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setCategory(CATAGORY);
        product.setSku(SKU);
        product.setLastUpdated(DATETIME);
        product.setPrice(PRICE);
        productRepository.save(product);


    }

    @After
    public void after() {
        productRepository.deleteAll();
    }


}
