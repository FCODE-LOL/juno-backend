package fcodelol.clone.juno.repository;

import fcodelol.clone.juno.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(nativeQuery = true, value = "SELECT `role` FROM USER WHERE `token` = ?1 ")
    String findRoleByToken(String token);
}
