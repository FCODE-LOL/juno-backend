package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Model;
import fcodelol.clone.juno.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {

    @Query(nativeQuery = true, value = "SELECT `discount_price` FROM `MODEL` WHERE `id` = ?1")
    BigDecimal findDiscountPrice(int modelId);

    @Query(nativeQuery = true, value = "SELECT `price` FROM `MODEL` WHERE `id` = ?1")
    BigDecimal findPrice(int modelId);

    Model findOneById(int id);
}
