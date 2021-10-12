package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {
}
