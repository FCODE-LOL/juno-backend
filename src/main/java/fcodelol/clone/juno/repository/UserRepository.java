package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.dto.UserByGroupDto;
import fcodelol.clone.juno.repository.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(nativeQuery = true, value = "SELECT `is_admin` FROM USER WHERE `token` = ?1 AND token_timestamp <= ?2 AND `is_disable` = 0 ")
    Boolean findRoleByToken(String token, Timestamp startTime);
    User findByEmail(String email);
    User findBySocialMediaId(String socialMediaId);
    @Query(nativeQuery = true, value = "SELECT `id`,`name` FROM `USER` WHERE `is_disable` = 0 ORDER BY POINT DESC LIMIT ?1 ")
    List<?> getTopCustomer(int numberOfUser);
}
