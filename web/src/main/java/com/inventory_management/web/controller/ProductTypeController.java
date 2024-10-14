package com.inventory_management.web.controller;

import com.inventory_management.web.dto.ProductTypeDto;
import com.inventory_management.web.dto.UserDto;
import com.inventory_management.web.entity.ProductType;
import com.inventory_management.web.service.ProductService;
import com.inventory_management.web.service.TypeService;
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
public class ProductTypeController {

    TypeService typeService;
    ProductService productService;
    @Autowired
    public ProductTypeController(TypeService typeService, ProductService productService) {
        this.typeService = typeService;
        this.productService = productService;
    }

    @GetMapping("/types")
    public String listAndSearchUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "7") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sort", required = false) String sort,
            Model model,
            @ModelAttribute("userFunctions") List<String> userFunctions,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu người dùng không có quyền truy cập chức năng này
        if (!userFunctions.contains("QLTL")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập trang (Quản Lý Thể Loại)!");
            return "redirect:/home";
        }
        // Đặt giá trị mặc định cho các trường tìm kiếm nếu chúng là null
        name = (name == null) ? "" : name;

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        // Sắp xếp theo tiêu chí
        if ("product-asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("numOfProduct").ascending());
        } else if ("product-desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("numOfProduct").descending());
        } else if ("date-asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("date").ascending());
        } else if ("date-desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("date").descending());
        }

        // Tìm kiếm và phân trang
        Page<ProductTypeDto> typesPage = typeService.searchUsers(name, pageable);
        List<ProductTypeDto> types = typesPage.getContent();

        // Đưa dữ liệu vào model
        model.addAttribute("types", types);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", typesPage.getTotalPages());
        model.addAttribute("pageSize", size);

        // Giữ lại giá trị tìm kiếm và sắp xếp
        model.addAttribute("name1", name);
        model.addAttribute("sort", sort);

        return "product-type/types-list";
    }


    @GetMapping("/types/new")
    public String createTypesFrom(Model model,
                                  @ModelAttribute("userFunctions") List<String> userFunctions,
                                  RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("TTL") || !userFunctions.contains("QLTL")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thêm thể loại!");
            return "redirect:/types";
        }

        ProductTypeDto productTypeDto = new ProductTypeDto();
        model.addAttribute("type", productTypeDto);
        return "product-type/type-create-form";
    }

    @PostMapping("/types/new")
    public String addType(@Valid @ModelAttribute("type") ProductTypeDto typeDto,
                          BindingResult result, Model model) {

        typeDto.setNumOfProduct(0);
        ProductType existingName = typeService.findByName(typeDto.getName());
        if (existingName != null && existingName.getName() != null && !existingName.getName().isEmpty()) {
            result.rejectValue("name", "error.type", "(*) Đã có thể loại này");
        }

        ProductType existingCode = typeService.findByCode(typeDto.getCode());
        if (existingCode != null && existingCode.getCode() != null && !existingCode.getCode().isEmpty()) {
            result.rejectValue("code", "error.type", "(*) Đã có mã thể loại này");
        }

        if (result.hasErrors()) {
            model.addAttribute("type", typeDto);
            return "product-type/type-create-form";
        }else {
            typeService.saveType(typeDto);
            model.addAttribute("info1", "success");
            model.addAttribute("type", typeDto);
            return "product-type/type-create-form";
        }
    }

    @GetMapping("/types/{typeID}/delete")
    public String deleteType(@PathVariable long typeID, Model model,
                             @ModelAttribute("userFunctions") List<String> userFunctions,
                             RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("XTL") || !userFunctions.contains("QLTL")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa thể loại!");
            return "redirect:/types";
        }

        try {
        boolean key = typeService.delete(typeID);

        if(!key){
            redirectAttributes.addFlashAttribute("message", "Xóa thể loại thành công");
        }else{
            redirectAttributes.addFlashAttribute("errorMessage", "Thể loại đang có sản phẩn sử dụng không thể xóa!");
        }
        } catch (Exception e) {
            // Thêm thông báo lỗi nếu xóa không thành công
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete type. Please try again.");
        }

        return "redirect:/types";
    }

    @PostMapping("/types/delete")
    public String deleteType(@RequestParam(value = "typeIds", required = false) List<Long> typeIds,
                             Model model,
                             @ModelAttribute("userFunctions") List<String> userFunctions,
                             RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("XNTL") || !userFunctions.contains("QLTL")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa thể loại");
            return "redirect:/types";
        }

        try {
            for (Long typeId : typeIds) {
                boolean key = typeService.delete(typeId);
                if(!key){
                    redirectAttributes.addFlashAttribute("errorMessage", "Thể loại đang có sản phẩn sử dụng không thể xóa!");
                }else{
                    redirectAttributes.addFlashAttribute("message", "Xóa thể loại thành công");
                }
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user. Please try again.");
        }
        return "redirect:/types";
    }


    @PostMapping("/types/{typeID}/edit")
    public String updateRole(@PathVariable("typeID") Long typeID,
                             @Valid @ModelAttribute("type") ProductTypeDto typeDto,
                             BindingResult result, Model model) {

        ProductType existingName = typeService.findByName(typeDto.getName());
        if (existingName != null && !existingName.getId().equals(typeID)) {
            result.rejectValue("name", "error.type", "(*) Tên vai trò đã tồn tại");
        }

        ProductType existingCode = typeService.findByCode(typeDto.getCode());
        if (existingCode != null && !existingCode.getId().equals(typeID)) {
            result.rejectValue("code", "error.type", "(*) Đã có mã thể loại này");
        }

        if (result.hasErrors()) {
            model.addAttribute("type", typeDto);
            model.addAttribute("typeID", typeID);
            return "product-type/type-edit-form";
        }else{
            typeDto.setId(typeID);
            typeDto.setNumOfProduct(productService.getTotalQuantityByProductType(typeID));
            typeService.updateRole(typeDto);
            model.addAttribute("info1", "success");
            model.addAttribute("type", typeDto);
            return "product-type/type-edit-form";
        }
    }

    @GetMapping("/types/{typeID}/edit")
    public String showEditForm(@PathVariable("typeID") Long typeID, Model model,
                               @ModelAttribute("userFunctions") List<String> userFunctions,
                               RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("CSTL") || !userFunctions.contains("QLTL")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền chỉnh sửa thể loại!");
            return "redirect:/types";
        }

        ProductTypeDto productTypeDto = typeService.findById(typeID);
        model.addAttribute("type", productTypeDto);
        model.addAttribute("typeID", typeID);
        return "product-type/type-edit-form";
    }

    @GetMapping("/types/{typeId}/details")
    @ResponseBody
    public ProductTypeDto getTypeDetails(@PathVariable Long typeId) {
        ProductTypeDto type = typeService.findById(typeId);
        System.out.println(type);
        return type;
    }

}
