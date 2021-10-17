package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Discount;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    Discount findOneByIdAndIsDisable(int id,Boolean isDisable);
    List<Discount> findByIsDisable(Boolean isDisable);
    List<Discount> findByIsDisable(Boolean isDisable, Sort sort);

}
