package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(nativeQuery = true, value = "SELECT `is_admin` FROM USER WHERE `token` = ?1 AND token_timestamp <= ?2 AND `is_disable` = 0 ")
    Boolean findRoleByToken(String token, Timestamp startTime);
    User findByEmailAndIsDisable(String email,boolean isDisable);
    User findBySocialMediaId(String socialMediaId);
    boolean existsByIdAndIsDisable(int id, boolean isDisable);
}
