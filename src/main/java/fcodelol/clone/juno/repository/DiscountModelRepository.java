package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.DiscountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountModelRepository extends JpaRepository<DiscountModel, Integer> {
}
