package com.grabtix.repository;

import com.grabtix.model.entity.Creator;
import com.grabtix.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends JpaRepository<Creator,String> {
    @Query(value = "SELECT * FROM m_creator WHERE user_id =:userId", nativeQuery = true)
    Creator findByUserId(@Param("userId") String userId);
}
