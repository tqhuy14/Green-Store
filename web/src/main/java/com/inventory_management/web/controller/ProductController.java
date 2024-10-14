package com.inventory_management.web.controller;

import com.inventory_management.web.dto.BillDto;
import com.inventory_management.web.dto.ProductDto;
import com.inventory_management.web.dto.ProductTypeDto;
import com.inventory_management.web.entity.Product;
import com.inventory_management.web.entity.ProductType;
import com.inventory_management.web.service.BillService;
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

import java.util.Collections;
import java.util.List;


@Controller
public class ProductController {

    TypeService typeService;
    ProductService productService;
    BillService billService;

    @Autowired
    public ProductController(TypeService typeService, ProductService productService, BillService billService) {
        this.typeService = typeService;
        this.productService = productService;
        this.billService = billService;
    }

    @GetMapping("/products")
    public String listAndSearchUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "start_price", required = false) String start_price,
            @RequestParam(value = "end_price", required = false) String end_price,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "productTypeId", required = false) Integer productTypeId,
            Model model,
            @ModelAttribute("userFunctions") List<String> userFunctions,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu người dùng không có quyền truy cập chức năng này
        if (!userFunctions.contains("QLSP")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập trang (Quản Lý Sản Phẩm)!");
            return "redirect:/home";
        }

        // Đặt giá trị mặc định cho các trường tìm kiếm nếu chúng là null
        name = (name == null) ? "" : name;

        Long startPrice = 0L;
        Long endPrice = Long.MAX_VALUE;

        try {
            // Parse start_price and end_price to double
            if (start_price != null && !start_price.isEmpty()) {
                startPrice = Long.parseLong(start_price);
            }
            if (end_price != null && !end_price.isEmpty()) {
                endPrice = Long.parseLong(end_price);
            }
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giá phải là một số hợp lệ!");
            return "redirect:/products"; // Redirect back to the same page with an error message
        }



        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Sắp xếp theo tiêu chí
        if ("num-asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("so_luong_hien_tai").ascending());
        } else if ("num-desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("so_luong_hien_tai").descending());
        } else if ("date-asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("date").ascending());
        } else if ("date-desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("date").descending());
        }

        Page<ProductDto> productsPage = productService.searchProducts(name, startPrice, endPrice, pageable, productTypeId);

        // Check if productsPage is null
        if (productsPage == null || productsPage.isEmpty()) {
            List<ProductType> types = typeService.findAllProductTypes();
            model.addAttribute("types", types);
            model.addAttribute("errorMessage", "Không tìm thấy sản phẩm phù hợp.");
            model.addAttribute("products", Collections.emptyList()); // Empty list if no products found
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
        } else {
            List<ProductType> types = typeService.findAllProductTypes();
            List<ProductDto> products = productsPage.getContent();
            // Đưa dữ liệu vào model
            model.addAttribute("types", types);
            model.addAttribute("products", products);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productsPage.getTotalPages());
            model.addAttribute("pageSize", size);
        }

        // Giữ lại giá trị tìm kiếm và sắp xếp
        model.addAttribute("name1", name);
        model.addAttribute("start_price", start_price);
        model.addAttribute("end_price", end_price);
        model.addAttribute("sort", sort);
        model.addAttribute("productTypeId", productTypeId);

        return "product/products-list";
    }




    @GetMapping("/products/new")
    public String createProductFrom(Model model,
                                    @ModelAttribute("userFunctions") List<String> userFunctions,
                                    RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("TSP") || !userFunctions.contains("QLSP")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thêm sản phẩm!");
            return "redirect:/products";
        }

        ProductDto productDto = new ProductDto();
        List<ProductType> types = typeService.findAllProductTypes();
        model.addAttribute("types", types);
        model.addAttribute("product", productDto);
        return "product/product-create-form";
    }

    @PostMapping("/products/new")
    public String addProduct(@Valid @ModelAttribute("product") ProductDto productDto,
                          BindingResult result, Model model) {

        Product existingName = productService.findByName(productDto.getName());
        if (existingName != null && existingName.getName() != null && !existingName.getName().isEmpty()) {
            result.rejectValue("name", "error.product", "(*) Đã có sản phẩm này");
        }

        Product existingCode = productService.findByCode(productDto.getCode());
        if (existingCode != null && existingCode.getName() != null && !existingCode.getName().isEmpty()) {
            result.rejectValue("code", "error.product", "(*) Đã có mã sản phẩm này");
        }

        if (result.hasErrors()) {
            List<ProductType> types = typeService.findAllProductTypes();
            model.addAttribute("types", types);
            model.addAttribute("product", productDto);
            return "product/product-create-form";
        }else{
            productService.saveProduct(productDto);
            List<ProductType> types = typeService.findAllProductTypes();
            model.addAttribute("types", types);
            model.addAttribute("info1", "success");
            model.addAttribute("product", productDto);
            return "product/product-create-form";
        }
    }

    @GetMapping("/products/{productID}/delete")
    public String deleteProduct(@PathVariable long productID, Model model,
                                @ModelAttribute("userFunctions") List<String> userFunctions,
                                RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("XSP") || !userFunctions.contains("QLSP")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa sản phẩm!");
            return "redirect:/products";
        }

        try{
            productService.delete(productID);
            redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user. Please try again.");
        }
        return "redirect:/products";
    }

    @PostMapping("/products/delete")
    public String deleteProducts(@RequestParam(value = "productIds", required = false) List<Long> productIds,
                                 @ModelAttribute("userFunctions") List<String> userFunctions,
                                 RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("XNSP") || !userFunctions.contains("QLSP")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa sản phẩm");
            return "redirect:/products";
        }

        System.out.println(productIds+"----------------");
        try {
            for (Long productId : productIds) {
                productService.delete(productId);
            }
            redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product. Please try again.");
        }
        return "redirect:/products";
    }




    @PostMapping("/products/{productID}/edit")
    public String updateProduct(@PathVariable("productID") Long productID,
                             @Valid @ModelAttribute("product") ProductDto productDto,
                             BindingResult result, Model model) {

        Product existingName = productService.findByName(productDto.getName());
        if (existingName != null && !existingName.getId().equals(productID)) {
            result.rejectValue("name", "error.product", "(*) Tên sản phẩm đã tồn tại");
        }

        Product existingCode = productService.findByCode(productDto.getCode());
        if (existingCode != null && !existingCode.getId().equals(productID)) {
            result.rejectValue("code", "error.product", "(*) Đã có mã sản phẩm này");
        }

        if (result.hasErrors()) {
            List<ProductType> types = typeService.findAllProductTypes();
            model.addAttribute("types", types);
            model.addAttribute("product", productDto);
            model.addAttribute("productID", productID);
            return "product/product-edit-form";
        }else{
            productService.updateProduct(productDto);
            List<ProductType> types = typeService.findAllProductTypes();
            model.addAttribute("types", types);
            model.addAttribute("info1", "success");
            model.addAttribute("products", productDto);
            return "product/product-edit-form";
        }
    }

    @GetMapping("/products/{productID}/edit")
    public String showEditForm(@PathVariable("productID") Long producID,
                               Model model,
                               @ModelAttribute("userFunctions") List<String> userFunctions,
                               RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("CSSP") || !userFunctions.contains("QLSP")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền chỉnh sửa sản phẩm!");
            return "redirect:/products";
        }

        ProductDto productDto = productService.findById(producID);
        List<ProductType> types = typeService.findAllProductTypes();
        model.addAttribute("types", types);
        model.addAttribute("product", productDto);
        model.addAttribute("producID", producID);
        return "product/product-edit-form";
    }

    @GetMapping("/products/{productId}/details")
    public String getproductDetails(@PathVariable Long productId, Model model,
                                    @ModelAttribute("userFunctions") List<String> userFunctions,
                                    RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("CTSP") || !userFunctions.contains("QLSP")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xem chi tiết!");
            return "redirect:/products";
        }
        ProductDto product = productService.findById(productId);
        List<BillDto> bills = billService.findBillsByProductId(productId);
        ProductTypeDto productTypeDto = typeService.findById(product.getProductTypeId());
        product.setProductTypeDto(productTypeDto);
        model.addAttribute("product", product);
        model.addAttribute("bills", bills);
        return "product/product-detail";
    }

    @PostMapping("/products/addQuantity")
    public String addQuantity(@RequestParam("productId") Long productId,
                              @RequestParam("quantity") int quantity,
                              @RequestParam("capital") Long capital,
                              @ModelAttribute("userFunctions") List<String> userFunctions,
                              RedirectAttributes redirectAttributes) {

        if (!userFunctions.contains("TSLSP") || !userFunctions.contains("QLSP")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thêm số lượng sản phẩm!");
            return "redirect:/products";
        }
        // Xử lý thêm số lượng và cập nhật giá vốn cho sản phẩm
        // Ví dụ:
        ProductDto product = productService.findById(productId);
        product.setSo_luong_hien_tai(product.getSo_luong_hien_tai() + quantity);
        product.setSo_luong_da_nhap(product.getSo_luong_da_nhap() + quantity);
        product.setTong_von_san_pham(product.getTong_von_san_pham()+ capital);
        productService.save(product);

        // Thêm thông báo thành công vào redirect attributes
        redirectAttributes.addFlashAttribute("successMessage", "Số lượng đã được thêm thành công!");
        return "redirect:/products/" + productId + "/details";
    }


}
