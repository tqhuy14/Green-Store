package com.inventory_management.web.controller;

import com.inventory_management.web.dto.FunctionDto;
import com.inventory_management.web.dto.GroupDto;
import com.inventory_management.web.dto.UserDto;
import com.inventory_management.web.entity.Group;
import com.inventory_management.web.security.AuthenticatedUserService;
import com.inventory_management.web.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class GroupController {

    GroupService groupService;
    UserService userService;
    FunctionService functionService;
    GroupUserService groupUserService;
    GroupFunctionService groupFunctionService;
    AuthenticatedUserService authenticatedUserService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService,
                           FunctionService functionService, GroupUserService groupUserService,
                           GroupFunctionService groupFunctionService, AuthenticatedUserService authenticatedUserService) {
        this.groupService = groupService;
        this.userService = userService;
        this.functionService = functionService;
        this.groupUserService = groupUserService;
        this.groupFunctionService = groupFunctionService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/groups")
    public String listAndSearchGroups(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "7") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false) String sort,
            Model model,
            @ModelAttribute("userFunctions") List<String> userFunctions,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu người dùng không có quyền truy cập chức năng này
        if (!authenticatedUserService.hasFunctions("QLNQ")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập trang (Quản Lý Phân Quyền)!");
            return "redirect:/home";
        }

        // Đặt giá trị mặc định cho các trường tìm kiếm nếu chúng là null
        name = (name == null) ? "" : name;
        status = (status == null) ? "" : status;

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        // Sắp xếp theo tiêu chí
        if ("create-asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("createdOn").ascending());
        } else if ("create-desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        } else if ("update-asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("updatedOn").ascending());
        } else if ("update-desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("updatedOn").descending());
        }

        // Tìm kiếm và phân trang
        Page<GroupDto> groupsPage = groupService.searchGroups(name, status, pageable);
        List<GroupDto> groups = groupsPage.getContent();

        // Đưa dữ liệu vào model
        model.addAttribute("groups", groups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", groupsPage.getTotalPages());
        model.addAttribute("pageSize", size);

        // Giữ lại giá trị tìm kiếm và sắp xếp
        model.addAttribute("name1", name);
        model.addAttribute("status", status);
        model.addAttribute("sort", sort);

        return "group/groups-list";
    }

    @GetMapping("/groups/new")
    public String newGroup(Model model,
                           @ModelAttribute("userFunctions") List<String> userFunctions,
                           RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("TNQ", "QLNQ")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thêm nhóm!");
            return "redirect:/groups";
        }

        GroupDto groupDto = new GroupDto();
        List<UserDto> users = userService.findAllUsers();
        List<FunctionDto> functions = functionService.findAll();
        model.addAttribute("group", groupDto);
        model.addAttribute("users", users);
        model.addAttribute("functions", functions);
        return "group/group-create-form";
    }

    @PostMapping("/groups/new")
    public String addAccount(@Valid @ModelAttribute("group") GroupDto groups,
                             BindingResult result,
                             Model model) {

        List<UserDto> users = userService.findAllUsers();
        List<FunctionDto> functions = functionService.findAll();

        Group existingGroupName = groupService.findByGroupByName(groups.getName());
        if (existingGroupName != null && existingGroupName.getName() != null && !existingGroupName.getName().isEmpty()) {
            result.rejectValue("name", "error.group", "(*) Tên nhóm đã tồn tại");
        }

        if (result.hasErrors()) {
            model.addAttribute("group", groups);
            model.addAttribute("users", users);
            model.addAttribute("functions", functions);
            return "group/group-create-form";
        }else{
            groups.setStatus("1");
            groupService.save(groups,groups.getUserIds(),groups.getFunctionIds());
            System.out.println("check");
            model.addAttribute("info1", "success");
            model.addAttribute("group", groups);
            model.addAttribute("users", users);
            model.addAttribute("functions", functions);
            return "group/group-create-form";
        }
    }

    @GetMapping("/groups/{groupID}/edit")
    public String showEditForm(@PathVariable("groupID") Long groupID,
                               @ModelAttribute("userFunctions") List<String> userFunctions,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (!authenticatedUserService.hasFunctions("CSNQ", "QLNQ")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền chỉnh sửa nhóm!");
            return "redirect:/groups";
        }

        List<UserDto> users = userService.findAllUsers();
        List<FunctionDto> functions = functionService.findAll();
        List<Long> userIds = groupUserService.findUserIdsByGroupId(groupID);
        List<Long> functionIds = groupFunctionService.findFunctionsByGroupId(groupID);

        // Tìm nhóm quyền theo ID
        GroupDto groupDto = groupService.findByID(groupID);
        groupDto.setUserIds(userIds);
        groupDto.setFunctionIds(functionIds);

        model.addAttribute("group", groupDto);
        model.addAttribute("groupID", groupID);
        model.addAttribute("users", users);
        model.addAttribute("functions", functions);
        return "group/group-edit-form";
    }

    @PostMapping("/groups/{groupID}/edit")
    public String updateGroup(@PathVariable("groupID") Long groupID,
                             @Valid @ModelAttribute("group") GroupDto groupDto,
                             BindingResult result,
                             Model model) {
        List<UserDto> users = userService.findAllUsers();
        List<FunctionDto> functions = functionService.findAll();
        // Kiểm tra GroupName có trùng với nhóm khác
        Group existingGroupName = groupService.findByGroupByName(groupDto.getName());
        if (existingGroupName != null && !existingGroupName.getId().equals(groupID)) {
            result.rejectValue("name", "error.group", "(*) Tên nhóm đã tồn tại");
        }
        // Nếu có lỗi validation, giữ lại groupID và các dữ liệu cần thiết
        if (result.hasErrors()) {
            model.addAttribute("group", groupDto);
            model.addAttribute("groupID", groupID);
            model.addAttribute("users", users);
            model.addAttribute("functions", functions);
            return "group/group-edit-form";
        }else{
            // Lưu nhóm đã cập nhật
            groupService.updateGroup(groupDto);
            model.addAttribute("info1", "success");
            System.out.println(groupDto);
            model.addAttribute("groups", groupDto);
            model.addAttribute("users", users);
            model.addAttribute("functions", functions);
            return "group/group-edit-form";
        }
    }

    @GetMapping("/groups/{groupID}/delete")
    public String deleteGroup(@PathVariable long groupID,
                              @ModelAttribute("userFunctions") List<String> userFunctions,
                              RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("XNQ", "QLNQ")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa nhóm!");
            return "redirect:/groups";
        }

        try {
            groupService.delete(groupID);
            // Thêm thông báo xóa thành công
            redirectAttributes.addFlashAttribute("message", "Xóa nhóm quyền thành công!");
        } catch (Exception e) {
            // Thêm thông báo lỗi nếu xóa không thành công
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete group. Please try again.");
        }

        return "redirect:/groups";
    }

    @PostMapping("/groups/inactivate")
    public String inactivateGroups(@RequestParam(value = "groupIds", required = false) List<Long> groupIds,
                                   @ModelAttribute("userFunctions") List<String> userFunctions,
                                   RedirectAttributes redirectAttributes){

        if (!authenticatedUserService.hasFunctions("VHNQ", "QLNQ")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền vô hiệu hóa nhóm!");
            return "redirect:/groups";
        }

        try {
            for (Long groupId : groupIds) {
                groupService.updateStatusGroup(groupId,"0");
            }
            redirectAttributes.addFlashAttribute("message", "Ngưng kích hoạt nhóm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to inactive user. Please try again.");
        }

        return "redirect:/groups";
    }

    @PostMapping("/groups/activate")
    public String activateGroups(@RequestParam(value = "groupIds", required = false) List<Long> groupIds,
                                 @ModelAttribute("userFunctions") List<String> userFunctions,
                                 RedirectAttributes redirectAttributes){

        if (!authenticatedUserService.hasFunctions("KHNQ", "QLNQ")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền kích hoạt nhóm!");
            return "redirect:/groups";
        }

        try {
            for (Long groupId : groupIds) {
                groupService.updateStatusGroup(groupId,"1");
            }
            redirectAttributes.addFlashAttribute("message", "Kích hoạt nhóm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to active group. Please try again.");
            System.out.println("3---"+e.getMessage());
        }
        return "redirect:/groups";
    }

    @PostMapping("/groups/delete")
    public String deleteGroups(@RequestParam(value = "groupIds", required = false) List<Long> groupIds,
                               @ModelAttribute("userFunctions") List<String> userFunctions,
                               RedirectAttributes redirectAttributes){

        if (!authenticatedUserService.hasFunctions("XNNQ", "QLNQ")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa nhóm!");
            return "redirect:/groups";
        }

        try {
            for (Long groupId : groupIds) {
                groupService.delete(groupId);
            }
            redirectAttributes.addFlashAttribute("message", "Xóa nhóm quyền thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete group. Please try again.");
        }
        return "redirect:/groups";
    }

    @GetMapping("/groups/{groupId}/details")
    @ResponseBody
    public GroupDto getGroupDetails(@PathVariable Long groupId, RedirectAttributes redirectAttributes) {
        try {
            GroupDto group = groupService.findByID(groupId);
            group.setUserNames(groupService.getListUserNamesByID(groupId));
            group.setFunctionNames(groupService.getListFunctionNamesByID(groupId));
            System.out.println(group.getFunctionNames());
            System.out.println(group.getUserNames());
            return group;
        }catch (Exception e) {
            System.out.println("4---"+e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete group. Please try again.");
        }

        return null;
    }
}
