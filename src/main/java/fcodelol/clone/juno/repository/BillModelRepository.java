package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.BillModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillModelRepository extends JpaRepository<BillModel, Integer> {
    BillModel findOneById(Integer id);

    @Query(nativeQuery = true, value = "SELECT `BILL_id` FROM `BILL_MODEL` WHERE `id` = ?1")
    Integer findBillId(Integer id);
}
