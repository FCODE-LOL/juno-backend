package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.dto.UserByGroupExtendDto;
import fcodelol.clone.juno.repository.entity.User;
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

    @Query("SELECT new fcodelol.clone.juno.dto.UserByGroupDto(id,name) FROM User WHERE isDisable = 0 ORDER BY point DESC  ")
    List<User> getTopCustomer(Pageable pageable);

    @Query("SELECT new fcodelol.clone.juno.dto.UserByGroupExtendDto(id,name,point,registerTimestamp,isDisable) FROM User")
    public List<UserByGroupExtendDto> getAllUser();

    @Query(nativeQuery = true, value = "SELECT `id` FROM `USER` WHERE `token` = ?1")
    public int getIdByToken(String token);

    User findOneById(int id);

}
