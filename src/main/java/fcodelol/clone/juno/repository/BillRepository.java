package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Bill;
import fcodelol.clone.juno.repository.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query(nativeQuery = true, value = "SELECT SUM(`payment`) FROM BILL WHERE `update_timestamp` BETWEEN ?1 AND ?2 AND `is_disable` = 0")
    BigDecimal getIncome(Timestamp startTime, Timestamp endTime);

    @Query(nativeQuery = true, value = "SELECT `USER_id` FROM `BILL` WHERE `id` = ?1")
    Integer getUserIdFromBill(int billId);

    @Query(nativeQuery = true, value = "SELECT `P`.`name` FROM `BILL` AS `B`\n" +
            "JOIN `BILL_MODEL` AS `BM` ON `B`.`id` = `BM`.`BILL_id`\n" +
            "JOIN `MODEL` AS `M` ON `BM`.`MODEL_id`=`M`.`id` \n" +
            "JOIN `PRODUCT` AS `P` ON `M`.`PRODUCT_id` = `P`.`id` \n" +
            "WHERE `B`.`id` = ?1")
    List<String> getProductNamesFromBill(int billId);

    Bill findOneByIdAndIsDisable(int id, boolean isDisable);

    List<Bill> findAll(Sort sort);

    List<Bill> findByUser(User user);

    Bill findOneById(int id);
}
