package com.hust.agriculture.repository;

import com.hust.agriculture.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(
            value = "select p from Payment p where p.user.username = :username and p.status = 1"
    )
    List<Payment> findPaymentNotComplete(@Param("username") String username);
}
