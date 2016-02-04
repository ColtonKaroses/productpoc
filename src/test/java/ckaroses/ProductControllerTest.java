package ckaroses;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by colton on 2/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@TestPropertySource(locations="classpath:test.properties")
@WebIntegrationTest(randomPort = true)
public class ProductControllerTest {

    @Autowired
    ProductRepository productRepository;

    @Value("${local.server.port}")
    private int port;

    private String baseUri = "http://localhost";

    private final String PRODUCT_NAME = "testProduct";
    private final String CATAGORY1 = "testcategory1";
    private final String CATAGORY2 = "testcategory2";

    private final String SKU1 = "test1";
    private final String SKU2 = "test2";
    private final String SKU3 = "test3";
    private final String SKU4 = "test4";

    private final BigDecimal PRICE = new BigDecimal("2.99");

    private Product product;
    private Product product2;
    private Product product3;

    @Before
    public void before() {
        product = new Product(SKU1,PRODUCT_NAME,CATAGORY1,PRICE);
        productRepository.save(product);

        product2 = new Product(SKU2,PRODUCT_NAME,CATAGORY2,PRICE);
        productRepository.save(product2);

        product3 = new Product(SKU3,PRODUCT_NAME,CATAGORY2,PRICE);
        productRepository.save(product3);
    }

    @After
    public void after() {
        productRepository.deleteAll();
    }

    @Test
    public void getProductsTest() {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products");
        ResponseEntity<List<Product>> result = template.exchange(builder.toUriString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>(){});
        Assert.assertEquals(3, result.getBody().size());
        Assert.assertTrue(result.getBody().contains(product));
        Assert.assertTrue(result.getBody().contains(product2));
        Assert.assertTrue(result.getBody().contains(product3));
    }

    @Test
    public void getByCategoryTest() {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products")
                        .queryParam("category", CATAGORY2);
        ResponseEntity<List<Product>> result = template.exchange(builder.toUriString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>(){});
        Assert.assertEquals(2, result.getBody().size());
        Assert.assertTrue(result.getBody().contains(product2));
        Assert.assertTrue(result.getBody().contains(product3));
    }

    @Test
    public void getProductTest() {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products/")
                        .path(String.valueOf(product.getId()));
        Product result = template.getForObject(builder.toUriString(), Product.class);
        Assert.assertEquals(product.getSku(), result.getSku());
    }

    @Test
    public void deleteProductTest() {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products/")
                        .path(String.valueOf(product.getId()));
        template.delete(builder.toUriString());
    }

    @Test
    public void addProductTest() {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products");
        ResponseEntity response = template.postForEntity(builder.toUriString(),
                new Product(SKU4, PRODUCT_NAME, CATAGORY1, PRICE), ResponseEntity.class);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void addConflictProductTest() {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products");
        ResponseEntity response = template.postForEntity(builder.toUriString(),
                new Product(SKU3, PRODUCT_NAME, CATAGORY1, PRICE), ResponseEntity.class);
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }




}
