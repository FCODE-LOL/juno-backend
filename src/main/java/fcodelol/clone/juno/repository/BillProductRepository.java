package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Bill;
import fcodelol.clone.juno.repository.entity.BillProduct;
import fcodelol.clone.juno.repository.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillProductRepository extends JpaRepository<BillProduct, Integer> {
    BillProduct findOneByBillAndModel(Bill bill, Model model);
}
