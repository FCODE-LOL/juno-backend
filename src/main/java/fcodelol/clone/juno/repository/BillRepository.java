package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Bill;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query(nativeQuery = true, value = "SELECT SUM(`payment`) FROM BILL WHERE `update_timestamp` BETWEEN ?1 AND ?2 AND `is_disable` = 0")
    BigDecimal getIncome(Timestamp startTime, Timestamp endTime);

    @Query(nativeQuery = true, value = "SELECT `USER_id` FROM `BILL`;")
    Integer getUserIdFromBill(int billId);

    Bill findOneByIdAndIsDisable(int id, boolean isDisable);

    List<Bill> findAll(Sort sort);

    Bill findOneById(int id);
}
