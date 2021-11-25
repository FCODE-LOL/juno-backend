package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.BillProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillProductRepository extends JpaRepository<BillProduct, Integer> {
    BillProduct findOneById(Integer id);

    @Query(nativeQuery = true, value = "SELECT `BILL_id` FROM `BILL_PRODUCT` WHERE `id` = ?1")
    Integer findBillId(Integer id);
}
