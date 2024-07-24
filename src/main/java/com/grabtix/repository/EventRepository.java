package com.grabtix.repository;

import com.grabtix.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByCategory(String category);

    @Query(value = "SELECT * FROM m_event WHERE creator_id = :creatorId",nativeQuery = true)
    List<Event> findByCreatorId(@Param("creatorId") String creatorId);

    @Query(value = "SELECT * FROM m_event WHERE date > CURRENT_DATE", nativeQuery = true)
    List<Event> findUpcomingEvents();

    @Query(value = "SELECT * FROM m_event WHERE name ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<Event> findAllByNameContaining(@Param("name")String name);
}

