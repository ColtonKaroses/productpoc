package ckaroses.products;

import ckaroses.Application;
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

import java.util.List;

/**
 * Created by colton on 2/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@TestPropertySource(locations="classpath:test.properties")
@WebIntegrationTest(randomPort = true)
public class ProductControllerTest extends ProductControllerTestBase {

    @Autowired
    ProductRepository productRepository;

    @Value("${local.server.port}")
    private int port;

    private String baseUri = "http://localhost";

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
        List<Product> result = getProducts(baseUri, port);
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(product));
        Assert.assertTrue(result.contains(product2));
        Assert.assertTrue(result.contains(product3));
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
        List<Product> results = getProducts(baseUri, port);
        Product expectedProduct = results.get(0);
        Product result = getProduct(baseUri, port, expectedProduct);
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedProduct.getSku(), result.getSku());
    }

    @Test
    public void deleteProductTest() {
        List<Product> results = getProducts(baseUri, port);
        Product expectedProduct = results.get(0);
        deleteProduct(baseUri, port, expectedProduct);
        results = getProducts(baseUri, port);
        Assert.assertTrue(!results.contains(expectedProduct));
    }

    @Test
    public void addProductTest() {
        ResponseEntity response = addProduct(baseUri, port, new Product(SKU4, PRODUCT_NAME, CATAGORY1, PRICE));
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void addConflictProductTest() {
        ResponseEntity response = addProduct(baseUri, port, new Product(SKU3, PRODUCT_NAME, CATAGORY1, PRICE));
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }




}
