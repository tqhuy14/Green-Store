//package com.inventory_management.web.repository;
//
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface RoleRepository extends JpaRepository<Role, Long> {
//    Role findByName(String name);
//
//    @Query("SELECT DISTINCT r FROM roles r LEFT JOIN r.functionalities f WHERE "
//            + "(:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
//            + "AND (:functionality IS NULL OR :functionality = '' OR f.code = :functionality)")
//    List<Role> searchRole(@Param("name") String name,
//                          @Param("functionality") String functionality);
//
//
//
//
//}
