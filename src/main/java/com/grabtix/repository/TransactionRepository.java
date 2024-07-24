package com.grabtix.repository;

import com.grabtix.model.entity.Ticket;
import com.grabtix.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {
    @Query(value = "SELECT * FROM m_transaction WHERE customer_id = :customerId", nativeQuery = true)
    List<Transaction> findTransactionByCustomerId(@Param("customerId")String customerId);
}
