package com.inventory_management.web.repository;

import com.inventory_management.web.entity.GroupFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupFunctionRepository extends JpaRepository<GroupFunction, Long> {

    // Truy vấn để lấy danh sách userIds theo groupId
    @Query("SELECT gf.functionId FROM GroupFunction gf WHERE gf.groupId = :groupId")
    List<Long> findFunctionsIdsByGroupId(@Param("groupId") long groupId);

    List<String> findFunctionsNamesByGroupId(@Param("groupId") long groupId);
    void deleteByGroupId(Long id);
}
