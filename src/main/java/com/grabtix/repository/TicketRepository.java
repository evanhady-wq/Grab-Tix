package com.grabtix.repository;

import com.grabtix.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TicketRepository extends JpaRepository<Ticket,String> {
    @Query(value = "SELECT * FROM m_ticket WHERE customer_id = :customerId", nativeQuery = true)
    List<Ticket> findTicketByCustomerId(@Param("customerId")String customerId);

    @Query(value = "SELECT t.* FROM m_ticket t " +
            "JOIN m_transaction tr ON t.transaction_id = tr.id " +
            "WHERE tr.status = 0 AND tr.customer_id = :customerId", nativeQuery = true)
    List<Ticket> findPaidTicket(@Param("customerId")String customerId);

}
