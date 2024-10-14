package com.inventory_management.web.repository;

import com.inventory_management.web.entity.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

    void deleteByUserId(long userID);

    // Truy vấn để lấy danh sách userIds theo groupId
    @Query("SELECT gu.userId FROM GroupUser gu WHERE gu.groupId = :groupId")
    List<Long> findUserIdsByGroupId(@Param("groupId") long groupId);

    void deleteByGroupId(Long id);
}
