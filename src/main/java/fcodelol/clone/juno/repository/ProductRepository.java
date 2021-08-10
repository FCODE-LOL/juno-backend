package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
