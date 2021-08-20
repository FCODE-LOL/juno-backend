package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    List<Product> findByNameContainingIgnoreCase(String name, Sort sort);
    Product findOneById(String id);
    boolean existsByIdAndIsDisable(String id, boolean isDisable);
}
