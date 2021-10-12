package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
    @Query(nativeQuery = true, value = "SELECT `color_id`,`size` FROM `MODEL` WHERE `PRODUCT_id` = ?1 AND is_disable = false ")
    List<?> getModelOfProduct(int productId);
    @Query(nativeQuery = true, value = "SELECT `discount_price` FROM `MODEL` WHERE `id` = ?1;")
    BigDecimal findDiscountPrice(int modelId);
    @Query(nativeQuery = true, value = "SELECT `quantity` FROM `MODEL` WHERE `id` = ?1;")
    Integer findQuantity(int modelId);
    Model findOneById(int id);
}
