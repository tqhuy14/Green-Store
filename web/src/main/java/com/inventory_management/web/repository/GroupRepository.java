package com.inventory_management.web.repository;

import com.inventory_management.web.entity.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
        Group findByName(String name);
        Group findFirstByOrderByCreatedOnDesc();

        @Modifying
        @Transactional
        @Query("UPDATE Group g SET g.status = :status WHERE g.id = :groupId")
        void updateGroupStatus(@Param("status") String status, @Param("groupId") Long groupId);

        @Query("SELECT g FROM Group g WHERE "
                + "(:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
                + "AND (:status IS NULL OR :status = '' OR g.status = :status)")
        Page<Group> searchGroup(@Param("name") String name,
                                    @Param("status") String status,
                                    Pageable pageable); // Thêm tham số Pageable


        @Query("SELECT g FROM Group g JOIN GroupUser gu ON g.id = gu.groupId WHERE gu.userId = :userId")
        List<Group> findGroupByUserID(@Param("userId") Long userId);


}
