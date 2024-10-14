package com.inventory_management.web.repository;

import com.inventory_management.web.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByName(String name);

    void deleteByName(String name);

    @Query("SELECT e FROM event e WHERE (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Event> searchEvent(@Param("name") String name,
                            Pageable pageable);

    @Query("SELECT e FROM event e WHERE CURRENT_TIMESTAMP BETWEEN e.dateBegin AND e.dateEnd")
    List<Event> findEventCurrent();

}
