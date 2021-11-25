package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.repository.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCaseAndIsDisable(String name,Boolean isDisable, Sort sort);

    Product findOneById(String id);

    List<Product> findByIsDisable(Boolean isDisable);

    boolean existsByIdAndIsDisable(String id, boolean isDisable);

    Product findByIdAndIsDisable(String id, boolean isDisable);

    //get products sold with the largest number
    @Query(nativeQuery = true, value = "SELECT P.id, P.name,P.created_timestamp FROM `MODEL` AS `M` JOIN `BILL_PRODUCT` AS `B` ON  M.id = B.MODEL_id " +
            "JOIN `PRODUCT` AS P ON M.PRODUCT_id = P.id WHERE P.is_disable = 0 GROUP BY `PRODUCT_id` ORDER BY SUM(B.quantity) DESC; ")
    List<?> getTopSaleProduct(int numberOfProduct);

    //get products sold with the best income
    @Query(nativeQuery = true, value = "SELECT P.id, P.name,P.created_timestamp FROM `MODEL` AS `M` JOIN `BILL_PRODUCT` AS `B` ON  M.id = B.MODEL_id " +
            "JOIN `PRODUCT` AS P ON M.PRODUCT_id = P.id WHERE P.is_disable = 0 GROUP BY `PRODUCT_id` ORDER BY SUM(B.price) DESC ")
    List<?> getTopIncomeProduct(int numberOfProduct);

}
