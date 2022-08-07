package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCaseAndIsDisable(String name, Boolean isDisable, Sort sort);

    Product findOneById(String id);

    List<Product> findByIsDisable(Boolean isDisable);

    boolean existsByIdAndIsDisable(String id, boolean isDisable);

    Product findByIdAndIsDisable(String id, boolean isDisable);

    //get products sold with the largest number,
    @Query(nativeQuery = true, value = "SELECT P.id, P.name, P.link_images, P.created_timestamp FROM `MODEL` AS `M` JOIN `BILL_MODEL` AS `B` ON  M.id = B.MODEL_id " +
            "JOIN `PRODUCT` AS P ON M.PRODUCT_id = P.id WHERE P.is_disable = 0 GROUP BY `PRODUCT_id` ORDER BY SUM(B.quantity) DESC LIMIT ?1 ")
    List<?> getTopSaleProduct(int numberOfProduct);

    //get products sold with the best income
    @Query(nativeQuery = true, value = "SELECT P.id, P.name, P.link_images, P.created_timestamp FROM `MODEL` AS `M` JOIN `BILL_MODEL` AS `B` ON  M.id = B.MODEL_id " +
            "JOIN `PRODUCT` AS P ON M.PRODUCT_id = P.id WHERE P.is_disable = 0 GROUP BY `PRODUCT_id` ORDER BY SUM(B.price) DESC LIMIT ?1")
    List<?> getTopIncomeProduct(int numberOfProduct);

    //get products bought with product having
    @Query(nativeQuery = true, value = "SELECT DISTINCT `P`.id,`P`.name,`P`.link_images, `P`.created_timestamp FROM `PRODUCT` AS P,`MODEL`AS `M`, " +
            "(SELECT `BP2`.`MODEL_id` AS model ,COUNT(`BP2`.id) AS `quantity` FROM `BILL_MODEL` AS `BP1`,`BILL_MODEL` AS `BP2` WHERE `BP1`.`MODEL_id` = ?1 AND `BP1`.`BILL_id` = `BP2`.`BILL_id` AND `BP2`.`MODEL_id` != ?1 GROUP BY model) AS `BP` " +
            "WHERE `M`.id = `BP`.model  AND `P`.id = `M`.`PRODUCT_id` AND P.`is_disable` = 0 ORDER BY `BP`.quantity DESC LIMIT ?2")
    List<?> getTopRelatedProduct(int productId, int numberOfProduct);

    @Query(nativeQuery = true, value = "SELECT `color_id` FROM `MODEL` WHERE `PRODUCT_id` = ?1")
    List<String> getColorsOfdProduct(String productId);

}
