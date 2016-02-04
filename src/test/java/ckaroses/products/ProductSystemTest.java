package ckaroses.products;

import org.junit.*;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by colton on 2/3/16.
 */
public class ProductSystemTest extends ProductControllerTestBase {

    private static String baseUri = "http://localhost:8080";

    @Before
    public void before() {
        product = new Product(SKU1,PRODUCT_NAME,CATAGORY1,PRICE);
        ResponseEntity response = addProduct(baseUri, product);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        product2 = new Product(SKU2,PRODUCT_NAME,CATAGORY2,PRICE);
        response = addProduct(baseUri, product2);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        product3 = new Product(SKU3,PRODUCT_NAME,CATAGORY2,PRICE);
        response = addProduct(baseUri, product3);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @After
    public void after() {
        List<Product> result = getProducts(baseUri);
        for (Product product : result) {
            deleteProduct(baseUri, product);
        }
    }

    @Test
    public void getProductsTest() {
        List<Product> result = getProducts(baseUri);
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
        List<Product> results = getProducts(baseUri);
        Product expectedProduct = results.get(0);
        Product result = getProduct(baseUri, expectedProduct);
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedProduct.getSku(), result.getSku());
    }

    @Test
    public void deleteProductTest() {
        List<Product> results = getProducts(baseUri);
        Product expectedProduct = results.get(0);
        deleteProduct(baseUri, expectedProduct);
        results = getProducts(baseUri);
        Assert.assertTrue(!results.contains(expectedProduct));
    }

    @Test
    public void addProductTest() {
        ResponseEntity response = addProduct(baseUri, new Product(SKU4, PRODUCT_NAME, CATAGORY1, PRICE));
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void addConflictProductTest() {
        ResponseEntity response = addProduct(baseUri, new Product(SKU3, PRODUCT_NAME, CATAGORY1, PRICE));
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
