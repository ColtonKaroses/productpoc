package ckaroses;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by colton on 2/1/16.
 */

public interface ProductRepository extends CrudRepository<Product, Long> {

    Iterable<Product> findByCategory(String category);
}
