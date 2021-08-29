package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.repository.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    List<Product> findByNameContainingIgnoreCase(String name, Sort sort);
    Product findOneById(String id);
    boolean existsByIdAndIsDisable(String id, boolean isDisable);
    @Query(nativeQuery = true, value = "SELECT `PRODUCT_id` AS `id`,`name`,`price`,`link_images` AS `linkImages`,`created_timestamp` AS `createdTimestamp` FROM `BILL_PRODUCT` AS B " +
            "INNER JOIN `PRODUCT` AS `P` ON `B`.`PRODUCT_id` = `P`.`id`  WHERE `P`.`is_disable` = 0 GROUP BY `PRODUCT_id` ORDER BY sum( `B`.`quantity`) desc LIMIT ?1 ")
    List<?> getTopProduct(int numberOfProduct);
}
