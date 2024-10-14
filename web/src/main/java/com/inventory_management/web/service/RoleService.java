//package com.inventory_management.web.service;
//
//
//import com.inventory_management.web.dto.RoleDto;
//import jakarta.validation.constraints.NotBlank;
//
//import java.util.List;
//
//public interface RoleService {
//
//    List<RoleDto> findAllRoles();
//
//    void saveRole (RoleDto roleDto);
//
//    void updateRole (RoleDto role);
//
//    void deleteRole (long roleId);
//
//    RoleDto findRoleDtoById(Long roleId);
//
//    Role findRoleById(Long id);
//
//    Role findByName(@NotBlank(message = "Role name is required") String name);
//
//    boolean delete(long roleID);
//
//    List<RoleDto> searchRoles(String name, String role);
//}
