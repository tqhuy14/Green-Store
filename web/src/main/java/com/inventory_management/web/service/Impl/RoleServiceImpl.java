//package com.inventory_management.web.service.Impl;
//
//import com.inventory_management.web.dto.RoleDto;
//import com.inventory_management.web.mapper.RoleMapper;
//import com.inventory_management.web.entity.Function;
//import com.inventory_management.web.repository.RoleRepository;
//import com.inventory_management.web.service.FunctionalityService;
//import com.inventory_management.web.service.RoleService;
//import com.inventory_management.web.service.UserService;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class RoleServiceImpl implements RoleService {
//
//    RoleRepository roleRepository;
//    FunctionalityService functionalityServiceImpl;
//    UserService userService;
//
//    public RoleServiceImpl(RoleRepository roleRepository, FunctionalityServiceImpl functionalityServiceImpl, UserServiceImpl userService) {
//        this.roleRepository = roleRepository;
//        this.functionalityServiceImpl = functionalityServiceImpl;
//        this.userService = userService;
//    }
//
//    @Override
//    public List<RoleDto> findAllRoles() {
//        List<Role> roles = roleRepository.findAll();
//        if (roles.size() > 1) {
//            roles = roles.subList(1, roles.size());
//        }
//        roles.sort(Comparator.comparing(Role::getId));
//        return roles.stream().map(RoleMapper :: toDto).collect(Collectors.toList());
//    }
//
//    @Override
//    public void saveRole(RoleDto roleDto) {
//        List<Function> functionalities = new ArrayList<>();
//        if (roleDto.getFunctionalitiesIds() != null) {
//            for (Long id : roleDto.getFunctionalitiesIds()) {
//                Function functionality = functionalityServiceImpl.findFunctionalityById(id) ;
//                if (functionality != null) {
//                    functionalities.add(functionality);
//                }
//            }
//        }
//        Role role = RoleMapper.toEntity(roleDto);
//        role.setFunctionalities(functionalities);
//        roleRepository.save(role);
//    }
//
//    @Override
//    public void updateRole(RoleDto role) {
//        Role role1 = RoleMapper.toEntity(role);
//        roleRepository.save(role1);
//    }
//
//    @Override
//    public void deleteRole(long roleId) {
//
//    }
//
//    @Override
//    public RoleDto findRoleDtoById(Long roleId) {
//        return RoleMapper.toDto(roleRepository.findById(roleId).get());
//    }
//
//    @Override
//    public Role findRoleById(Long id) {
//        return roleRepository.findById(id).get();
//    }
//
//    @Override
//    public Role findByName(String name) {
//        return roleRepository.findByName(name);
//    }
//
//    @Override
//    public boolean delete(long roleID) {
//        if(userService.existsByRoleId(roleID)) {
//            return false;
//        }
//        roleRepository.deleteById(roleID);
//        return true;
//    }
//
//    @Override
//    public List<RoleDto> searchRoles(String name, String role) {
//        List<Role> roles = roleRepository.searchRole(name, role);
//        roles.sort(Comparator.comparing(Role::getId));
//        if (roles.get(0).getId() == 1) {
//            roles = roles.subList(1, roles.size());
//        }
//        return roles.stream().map(role1 -> RoleMapper.toDto(role1)).collect(Collectors.toList());
//    }
//}
