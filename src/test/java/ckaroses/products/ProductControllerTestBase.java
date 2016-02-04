package ckaroses.products;

import ckaroses.products.Product;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by colton on 2/3/16.
 */
public class ProductControllerTestBase {

    final static int port = 8080;

    protected final String PRODUCT_NAME = "testProduct";
    protected final String CATAGORY1 = "testcategory1";
    protected final String CATAGORY2 = "testcategory2";

    protected final String SKU1 = "test1";
    protected final String SKU2 = "test2";
    protected final String SKU3 = "test3";
    protected final String SKU4 = "test4";

    protected final BigDecimal PRICE = new BigDecimal("2.99");

    protected Product product;
    protected Product product2;
    protected Product product3;

    protected void deleteProduct(String baseUri, Product product) {
        deleteProduct(baseUri, port, product);
    }

    protected void deleteProduct(String baseUri, int port, Product product) {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products/")
                        .path(String.valueOf(product.getId()));
        template.delete(builder.toUriString());
    }

    protected List<Product> getProducts(String baseUri) {
        return getProducts(baseUri, port);
    }

    protected List<Product> getProducts(String baseUri, int port) {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products");
        return template.exchange(builder.toUriString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>(){}).getBody();
    }

    protected ResponseEntity addProduct(String baseUri, Product product) {
        return addProduct(baseUri, port, product);
    }

    protected ResponseEntity addProduct(String baseUri, int port, Product product) {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products");
        return template.postForEntity(builder.toUriString(), product, ResponseEntity.class);
    }

    protected Product getProduct(String baseUri, Product product) {
        return getProduct(baseUri, port, product);
    }

    protected Product getProduct(String baseUri, int port, Product product) {
        RestTemplate template = new TestRestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(baseUri)
                        .port(port)
                        .path("products/")
                        .path(String.valueOf(product.getId()));
        return template.getForObject(builder.toUriString(), Product.class);
    }
}
