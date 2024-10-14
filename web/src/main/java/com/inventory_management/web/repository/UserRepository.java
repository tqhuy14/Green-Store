package com.inventory_management.web.repository;

import com.inventory_management.web.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findByPhone(String phone);

    @Query("SELECT u FROM UserEntity u WHERE "
            + "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
            + "AND (:status IS NULL OR :status = '' OR u.status = :status)")
    Page<UserEntity> searchUser(@Param("name") String name,
                                @Param("status") String status,
                                Pageable pageable); // Thêm tham số Pageable

    @Query("SELECT DISTINCT f.code FROM Function f "
            + "JOIN GroupFunction gf ON gf.functionId = f.id "
            + "JOIN GroupUser gu ON gu.groupId = gf.groupId "
            + "JOIN Group g ON g.id = gu.groupId "  // Join Group table
            + "WHERE gu.userId = :userId AND g.status = '1'") // Filter by Group status
    List<String> findDistinctFunctionCodesByUserId(@Param("userId") Long userId);



}

