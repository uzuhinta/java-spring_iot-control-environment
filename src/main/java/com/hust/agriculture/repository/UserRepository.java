package com.hust.agriculture.repository;

import com.hust.agriculture.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(
            value = "select u from User u where u.username = :username or u.email = :email or u.phoneNumber = :phoneNumber"
    )
    User findByUsernameOrEmailOrPhoneNumber(@Param("username") String username,
                                            @Param("email") String email,
                                            @Param("phoneNumber") String phoneNumber);

    User findByTopicName(String topicName);

    @Query(
            value = "select u from User u where u.role.ID = 1 and u.status = :status order by u.ID desc"
    )
    List<User> findAllCustomer(@Param("status") Integer status);

    @Query(
            value = "select u from User u " +
                    "where u.role.ID = 1 or u.role.ID = 3" +
                    "and u.status = :status " +
                    "and lower(u.username) like lower(concat('%', :key, '%'))" +
                    "order by u.ID desc"
    )
    Page<User> findCustomer(Pageable pageable,@Param("status") Integer status, @Param("key") String key);
}
