package com.hust.agriculture.repository;

import com.hust.agriculture.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(
            value = "select p from Invoice p " +
                    "where p.status = :status " +
                    "and lower(p.user.username) like lower(concat('%', :key, '%')) order by p.dueDate"
    )
    Page<Invoice> findInvoices(Pageable pageable, @Param("status") Integer status, @Param("key") String key);

    @Query(
            value = "select i from Invoice i " +
                    "where i.status = :status and i.user.username = :username order by i.dueDate"
    )
    List<Invoice> findByUser(@Param("status") Integer status, @Param("username") String username);

    @Query(
            value = "select i from Invoice i " +
                    "where i.ID = :invoiceId and i.user.ID = :userId"
    )
    List<Invoice> findByInvoiceIdAndUserId(@Param("invoiceId") Long invoiceId, @Param("userId") Long userId);

    @Query(
            value = "select * from invoice i, users u " +
                    "where i.user_id = u.id and u.username = :username and date(i.due_date) < date(now()) ",
            nativeQuery = true
    )
    List<Invoice> findInvoicesOverDueDateByUsername(@Param("username") String username);
}
