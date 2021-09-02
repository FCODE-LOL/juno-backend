package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
    List<Type> findByParentId(int typeId);

    boolean existsByName(String name);

}
