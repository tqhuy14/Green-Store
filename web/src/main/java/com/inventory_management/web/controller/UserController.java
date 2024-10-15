package com.inventory_management.web.controller;
import com.inventory_management.web.dto.GroupDto;
import com.inventory_management.web.dto.UserDto;
import com.inventory_management.web.entity.UserEntity;
import com.inventory_management.web.security.AuthenticatedUserService;
import com.inventory_management.web.service.GroupService;
import com.inventory_management.web.service.UserService;
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
public class UserController {

    private UserService userService;
    private GroupService groupService;
    private AuthenticatedUserService authenticatedUserService;

    @Autowired
    public UserController(UserService userService, GroupService groupService,AuthenticatedUserService authenticatedUserService) {
        this.userService = userService;
        this.groupService = groupService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/users")
    public String listAndSearchUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "7") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false) String sort,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu người dùng không có quyền truy cập chức năng này
        if (!authenticatedUserService.hasFunctions("QLTK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập trang (Quản Lý Tài Khoản)!");
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
        Page<UserDto> usersPage = userService.searchUsers(name, status, pageable);
        List<UserDto> users = usersPage.getContent();

        // Đưa dữ liệu vào model
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("pageSize", size);

        // Giữ lại giá trị tìm kiếm và sắp xếp
        model.addAttribute("name1", name);
        model.addAttribute("status", status);
        model.addAttribute("sort", sort);

        return "user/users-list";
    }

    @GetMapping("/users/new")
    public String newUser(Model model,
                          RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("TTK", "QLTK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thêm tài khoản!");
            return "redirect:/users";
        }
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "user/user-create-form";
    }


    @PostMapping("/users/new")
    public String addAccount(@Valid @ModelAttribute("user") UserDto user,
                             BindingResult result,
                             Model model) {

        UserEntity existingUsername = userService.findByUsername(user.getUsername());
        if (existingUsername != null && existingUsername.getUsername() != null && !existingUsername.getUsername().isEmpty())  result.rejectValue("username", "error.user", "(*) Tên tài khoản đã tồn tại");
        if ("admin".equals(user.getRole())) result.rejectValue("role", "error.user", "(*) Không thể sử dụng vai trò của admin");

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "user/user-create-form";
        }else{
            user.setStatus("active");
            userService.saveUser(user);
            model.addAttribute("info1", "success");
            model.addAttribute("user", user);
            return "user/user-create-form";
        }
    }


    @GetMapping("/users/{userID}/delete")
    public String deleteUser(@PathVariable long userID,
                             RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("XTK", "QLTK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa tài khoản!");
            return "redirect:/users";
        }

        try {
            userService.delete(userID);
            // Thêm thông báo xóa thành công
            redirectAttributes.addFlashAttribute("message", "Xóa tài khoản thành công!");
        } catch (Exception e) {
            // Thêm thông báo lỗi nếu xóa không thành công
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user. Please try again.");
        }

        return "redirect:/users";
    }


    @PostMapping("/users/{userID}/edit")
    public String updateUser(@PathVariable("userID") Long userID,
                             @Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult result,
                             Model model) {

        // Kiểm tra Username có trùng với tài khoản khác
        UserEntity existingUsername = userService.findByUsername(userDto.getUsername());
        if (existingUsername != null && !existingUsername.getId().equals(userID)) {
            result.rejectValue("username", "error.user", "(*) Tên tài khoản đã tồn tại");
        }
        // Nếu có lỗi validation, giữ lại userID và các dữ liệu cần thiết
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            model.addAttribute("userID", userID);
            return "user/user-edit-form";
        }else{
            // Lưu người dùng đã cập nhật
            userService.updateUser(userDto);
            model.addAttribute("info1", "success");
            model.addAttribute("users", userDto);
            return "user/user-edit-form";
        }
    }


    @GetMapping("/users/{userID}/edit")
    public String showEditForm(@PathVariable("userID") Long userID,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("CSTK", "QLTK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền chỉnh sửa tài khoản!");
            return "redirect:/users";
        }
        // Tìm người dùng theo ID
        UserDto userDto = userService.findByID(userID);
        model.addAttribute("user", userDto);
        model.addAttribute("userID", userID);
        return "user/user-edit-form";
    }

    @PostMapping("/users/inactivate")
    public String inactivateUserAccount(@RequestParam(value = "userIds", required = false) List<Long> userIds,
                                        RedirectAttributes redirectAttributes){

        if (!authenticatedUserService.hasFunctions("VHTK", "QLTK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền vô hiệu hóa!");
            return "redirect:/users";
        }
        try {
            for (Long userId : userIds) {
                UserDto userDto = userService.findByID(userId);
                userDto.setStatus("inactive");
                userService.saveUser(userDto);
            }
            redirectAttributes.addFlashAttribute("message", "Ngưng kích hoạt tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to inactive user. Please try again.");
        }

        return "redirect:/users";
    }

    @PostMapping("/users/activate")
    public String activateUserAccount(@RequestParam(value = "userIds", required = false) List<Long> userIds,
                                      RedirectAttributes redirectAttributes){

        if (!authenticatedUserService.hasFunctions("KHTK", "QLTK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền kích hoạt tài khoản!");
            return "redirect:/users";
        }

        try {
            for (Long userId : userIds) {
                UserDto userDto = userService.findByID(userId);
                userDto.setStatus("active");
                userService.saveUser(userDto);
            }
            redirectAttributes.addFlashAttribute("message", "Kích hoạt tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to active user. Please try again.");
        }
        return "redirect:/users";
    }

    @PostMapping("/users/delete")
    public String deleteUserAccount(@RequestParam(value = "userIds", required = false) List<Long> userIds,
                                    @ModelAttribute("userFunctions") List<String> userFunctions,
                                    RedirectAttributes redirectAttributes){

        if (!authenticatedUserService.hasFunctions("XNTK", "QLTK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa tài khoản");
            return "redirect:/users";
        }

        try {
            for (Long userId : userIds) {
                userService.delete(userId);
            }
            redirectAttributes.addFlashAttribute("message", "Xóa tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user. Please try again.");
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{userId}/details")
    @ResponseBody
    public UserDto getUserDetails(@PathVariable Long userId) {
        UserDto user = userService.findByID(userId);
        List<GroupDto> groups = groupService.findGroupByUserID(userId);
        user.setGroups(groups);
        return user;
    }

}
