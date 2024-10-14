//package com.inventory_management.web.controller;
//
//import com.inventory_management.web.dto.FunctionalityDto;
//import com.inventory_management.web.dto.RoleDto;
//import com.inventory_management.web.service.FunctionalityService;
//import com.inventory_management.web.service.RoleService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//@SessionAttributes("role_current")
//public class RoleController {
//    RoleService roleService;
//    FunctionalityService functionalityService;
//
//    @Autowired
//    public RoleController(RoleService roleService, FunctionalityService functionalityService) {
//        this.roleService = roleService;
//        this.functionalityService = functionalityService;
//    }
//
//    private void checkSessionRole(Model model) {
//        if (model.containsAttribute("role_current")) {
//            String role = (String) model.getAttribute("role_current");
//        }
//    }
//
//    @GetMapping("/roles")
//    public String listRoles(Model model) {
//        checkSessionRole(model); // Kiểm tra session role
//        System.out.println(model.getAttribute("role_current")+"----------------------");
//
//
//        List<FunctionalityDto> functionalityDtos = functionalityService.findAllFunctionalities();
//        List<RoleDto> roleDtos = roleService.findAllRoles();
//
//        model.addAttribute("functionalities", functionalityDtos);
//        model.addAttribute("roles", roleDtos);
//        return "role/roles-list";
//    }
//
//    @GetMapping("/roles/new")
//    public String newRole(Model model) {
//        checkSessionRole(model); // Kiểm tra session role
//
//        RoleDto roleDto = new RoleDto();
//        List<FunctionalityDto> functionalityDtos = functionalityService.findAllFunctionalities();
//
//        model.addAttribute("role", roleDto);
//        model.addAttribute("functionalities", functionalityDtos);
//        return "role/role-create-form";
//    }
//
//    @PostMapping("/roles/new")
//    public String addRole(@Valid @ModelAttribute("role") RoleDto role,
//                          BindingResult result, Model model) {
//        checkSessionRole(model); // Kiểm tra session role
//
//        Role existingName = roleService.findByName(role.getName());
//        if (existingName != null && existingName.getName() != null && !existingName.getName().isEmpty()) {
//            result.rejectValue("name", "error.role", "(*) Đã có vai trò này");
//        }
//
//        if (result.hasErrors()) {
//            model.addAttribute("role", role);
//            model.addAttribute("functionalities", functionalityService.findAllFunctionalities());
//            return "role/role-create-form";
//        }
//
//        roleService.saveRole(role);
//        model.addAttribute("info1", "success");
//
//        List<FunctionalityDto> functionalityDtos = functionalityService.findAllFunctionalities();
//        List<RoleDto> roleDtos = roleService.findAllRoles();
//
//        model.addAttribute("functionalities", functionalityDtos);
//        model.addAttribute("roles", roleDtos);
//        return "role/roles-list";
//    }
//
//    @GetMapping("/roles/{roleID}/delete")
//    public String deleteRole(@PathVariable long roleID, Model model) {
//        checkSessionRole(model); // Kiểm tra session role
//
//        boolean key = roleService.delete(roleID);
//        System.out.println(key+"------------------------");
//        if(!key){
//            model.addAttribute("info1", "fail");
//        }
//
//        List<FunctionalityDto> functionalityDtos = functionalityService.findAllFunctionalities();
//        List<RoleDto> roleDtos = roleService.findAllRoles();
//        model.addAttribute("functionalities", functionalityDtos);
//        model.addAttribute("roles", roleDtos);
//        return "role/roles-list";
//    }
//
//    @GetMapping("/roles/search")
//    public String searchRoles(@RequestParam(required = false) String name,
//                              @RequestParam(required = false) String role,
//                              Model model) {
//        checkSessionRole(model); // Kiểm tra session role
//
//        List<RoleDto> roles = roleService.searchRoles(name, role);
//        model.addAttribute("roles", roles);
//        model.addAttribute("name1", name);
//        model.addAttribute("selectedFunctionality", role);
//
//        List<FunctionalityDto> functionalities = functionalityService.findAllFunctionalities();
//        model.addAttribute("functionalities", functionalities);
//
//        return "role/roles-list";
//    }
//
//    @PostMapping("/roles/{roleID}/edit")
//    public String updateRole(@PathVariable("roleID") Long roleID,
//                             @Valid @ModelAttribute("role") RoleDto roleDto,
//                             BindingResult result, Model model) {
//        checkSessionRole(model); // Kiểm tra session role
//
//        Role currentRole = roleService.findRoleById(roleID);
//
//        Role existingRoleName = roleService.findByName(roleDto.getName());
//        if (existingRoleName != null && !existingRoleName.getId().equals(roleID)) {
//            result.rejectValue("name", "error.role", "(*) Tên vai trò đã tồn tại");
//        }
//
//        if (result.hasErrors()) {
//            List<FunctionalityDto> functionalityDtos = functionalityService.findAllFunctionalities();
//            model.addAttribute("functionalities", functionalityDtos);
//            model.addAttribute("role", roleDto);
//            model.addAttribute("roleID", roleID);
//            return "role/role-edit-form";
//        }
//
//        List<FunctionalityDto> selectedFunctionalities = functionalityService.findByIds(roleDto.getFunctionalitiesIds());
//        roleDto.setFunctionalities(selectedFunctionalities);
//        roleDto.setId(roleID);
//        roleService.updateRole(roleDto);
//
//        model.addAttribute("info1", "success-edit");
//        List<FunctionalityDto> functionalityDtos = functionalityService.findAllFunctionalities();
//        List<RoleDto> roleDtos = roleService.findAllRoles();
//        model.addAttribute("functionalities", functionalityDtos);
//        model.addAttribute("roles", roleDtos);
//
//        return "role/roles-list";
//    }
//
//    @GetMapping("/roles/{roleID}/edit")
//    public String showEditForm(@PathVariable("roleID") Long roleID, Model model) {
//        checkSessionRole(model); // Kiểm tra session role
//
//        RoleDto roleDto = roleService.findRoleDtoById(roleID);
//        List<FunctionalityDto> functionalityDtos = functionalityService.findAllFunctionalities();
//
//        List<Long> ids = roleDto.getFunctionalities().stream()
//                .map(FunctionalityDto::getId)
//                .collect(Collectors.toList());
//
//        roleDto.setFunctionalitiesIds(ids);
//        model.addAttribute("role", roleDto);
//        model.addAttribute("functionalities", functionalityDtos);
//        model.addAttribute("roleID", roleID);
//
//        return "role/role-edit-form";
//    }
//
//    @PostMapping("/roles/delete")
//    public String deleteRoleAccount(@RequestParam(value = "roleIds", required = false) List<Long> roleIds,
//                                    Model model) {
//        checkSessionRole(model); // Kiểm tra session role
//
//        for (Long roleID : roleIds) {
//            boolean key = roleService.delete(roleID);
//            if(!key){
//                model.addAttribute("info1", "fail");
//            }
//        }
//
//        List<FunctionalityDto> functionalityDtos = functionalityService.findAllFunctionalities();
//        List<RoleDto> roleDtos = roleService.findAllRoles();
//        model.addAttribute("functionalities", functionalityDtos);
//        model.addAttribute("roles", roleDtos);
//        return "role/roles-list";
//    }
//
//    @GetMapping("/roles/{roleId}/details")
//    @ResponseBody
//    public RoleDto getRoleDetails(@PathVariable Long roleId, Model model) {
//        checkSessionRole(model); // Kiểm tra session role
//
//        RoleDto role = roleService.findRoleDtoById(roleId);
//        return role;
//    }
//}
