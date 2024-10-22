package com.inventory_management.web.controller;

import com.inventory_management.web.dto.*;
import com.inventory_management.web.entity.UserEntity;
import com.inventory_management.web.security.AuthenticatedUserService;
import com.inventory_management.web.service.BillService;
import com.inventory_management.web.service.EventService;
import com.inventory_management.web.service.ProductService;
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
public class BillController {

    BillService billService;
    ProductService productService;
    EventService eventService;
    UserService userService;
    AuthenticatedUserService authenticatedUserService;

    @Autowired
    public BillController(BillService billService, ProductService productService, EventService eventService, UserService userService, AuthenticatedUserService authenticatedUserService) {
        this.billService = billService;
        this.productService = productService;
        this.eventService = eventService;
        this.userService = userService;
        this.authenticatedUserService = authenticatedUserService;

    }

    @GetMapping("/bills")
    public String listAndSearchUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "7") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false) String sort,
            Model model,
            @ModelAttribute("userFunctions") List<String> userFunctions,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu người dùng không có quyền truy cập chức năng này
        if (!authenticatedUserService.hasFunctions("QLDH")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập trang (Quản Lý Đơn Hàng)!");
            return "redirect:/home";
        }
        // Đặt giá trị mặc định cho các trường tìm kiếm nếu chúng là null
        name = (name == null) ? "" : name;

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        // Sắp xếp theo tiêu chí
        if ("date-asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("date").ascending());
        } else if ("date-desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("date").descending());
        }

        // Tìm kiếm và phân trang
        Page<BillDto> billsPage = billService.searchBills(name, status, pageable);
        List<BillDto> bills = billsPage.getContent();
        for (BillDto bill : bills) {

            ProductDto product = productService.findById(bill.getProductId());
            bill.setProduct(product);
        }



        // Đưa dữ liệu vào model
        model.addAttribute("bills", bills);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", billsPage.getTotalPages());
        model.addAttribute("pageSize", size);

        // Giữ lại giá trị tìm kiếm và sắp xếp
        model.addAttribute("name1", name);
        model.addAttribute("status", status);
        model.addAttribute("sort", sort);

        return "bill/bills-list";
    }


    @GetMapping("/bills/new")
    public String createTypesFrom(Model model,
                                  @ModelAttribute("userFunctions") List<String> userFunctions,
                                  RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("TDH", "QLDH")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thêm đơn hàng");
            return "redirect:/bills";
        }

        List<EventDto> eventDtos = eventService.findEventCurrent();
        BillDto billDto = new BillDto();
        billDto.setEvents(eventDtos);
        List<ProductDto> productDtos = productService.findAllProductsDto();
        model.addAttribute("bill", billDto);
        model.addAttribute("productDtos", productDtos);
        return "bill/bill-create-form";
    }

    @PostMapping("/bills/new")
    public String addEvent(@Valid @ModelAttribute("bill") BillDto billDto,
                           BindingResult result, Model model) {

        int numberOfProduct = productService.findById(billDto.getProductId()).getSo_luong_hien_tai();
        System.out.println(numberOfProduct+"----------"+ billDto.getSoLuongMua());
        if (numberOfProduct < billDto.getSoLuongMua()) {
            result.rejectValue("soLuongMua", "error.bill", "(*) Số lượng sản phẩm còn lại không đủ");
        }

        if (!result.hasErrors()) {
            billService.saveBill(billDto);
            model.addAttribute("info1", "success");
        }
        List<EventDto> eventDtos = eventService.findEventCurrent();
        billDto.setEvents(eventDtos);
        List<ProductDto> productDtos = productService.findAllProductsDto();
        model.addAttribute("bill", billDto);
        model.addAttribute("productDtos", productDtos);
        return "bill/bill-create-form";
    }

    @GetMapping("/bills/{billID}/delete")
    public String deleteType(@PathVariable Long billID,
                             @ModelAttribute("userFunctions") List<String> userFunctions,
                             RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("XDH", "QLDH")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa đơn hàng!");
            return "redirect:/bills";
        }

        try {
            billService.delete(billID);
            redirectAttributes.addFlashAttribute("message", "Xóa đơn hàng thành công");
        } catch (Exception e) {
            // Thêm thông báo lỗi nếu xóa không thành công
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete bill. Please try again.");
        }

        return "redirect:/bills";
    }

    @PostMapping("/bills/delete")
    public String deleteType(@RequestParam(value = "billIds", required = false) List<Long> billIds,
                             @ModelAttribute("userFunctions") List<String> userFunctions,
                             RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("XNDH", "QLDH")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa đơn hàng!");
            return "redirect:/bills";
        }

        try {
            for (Long billId : billIds) {
                billService.delete(billId);
            }
            redirectAttributes.addFlashAttribute("message", "Xóa đơn hàng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user. Please try again.");
        }
        return "redirect:/bills";
    }


    @PostMapping("/bills/{billID}/edit")
    public String updateRole(@PathVariable("billID") Long billID,
                             @Valid @ModelAttribute("bill") BillDto billDto,
                             BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("bill", billDto);
            model.addAttribute("billID", billID);
            return "bill/bill-edit-form";
        }else{
            billService.updateBill(billDto);
            model.addAttribute("info1", "success");
            model.addAttribute("bill", billDto);
            return "bill/bill-edit-form";
        }
    }

    @GetMapping("/bills/{billID}/edit")
    public String showEditForm(@PathVariable("billID") Long billID, Model model,
                               @ModelAttribute("userFunctions") List<String> userFunctions,
                               RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("CSDH", "QLDH")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền chỉnh sửa đơn hàng!");
            return "redirect:/bills";
        }

        BillDto billDto = billService.findByID(billID);
        List<EventDto> eventDtos = eventService.findEventCurrent();
        billDto.setEvents(eventDtos);
        List<ProductDto> productDtos = productService.findAllProductsDto();
        model.addAttribute("productDtos", productDtos);
        model.addAttribute("billID", billID);
        model.addAttribute("bill", billDto);
        return "bill/bill-edit-form";
    }

    @GetMapping("/bills/{billId}/details")
    @ResponseBody
    public BillDto getBillDetails(@PathVariable Long billId) {

        BillDto bill = billService.findByID(billId);
        if(bill.getEventId() != null) {
            EventDto eventDto = eventService.findByID(bill.getEventId());
            bill.setEvent(eventDto);
        }
        ProductDto product = productService.findById(bill.getProductId());
        bill.setProduct(product);
        UserDto userDto = userService.findByID(bill.getCreatedByUserId());
        bill.setUser(userDto);
        System.out.println(bill);

        return bill;
    }

    @PostMapping("/bills/done")
    public String doneBills(@RequestParam(value = "billIds", required = false) List<Long> billIds,
                                        @ModelAttribute("userFunctions") List<String> userFunctions,
                                        RedirectAttributes redirectAttributes){

        if (!authenticatedUserService.hasFunctions("HTDH", "QLDH")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền hoàn thành đơn hàng!");
            return "redirect:/bills";
        }

        try {
            for (Long billId : billIds) {
                BillDto billDto = billService.findByID(billId);
                if(billDto.getStatus().equals("doing")){
                    billService.updateBillStatus(billId,"done");
                }else{
                    redirectAttributes.addFlashAttribute("errorMessage", "Hoàn thành đơn không thành công!");
                }
            }
            redirectAttributes.addFlashAttribute("message", "Hoàn thành đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to finish bill. Please try again.");
        }

        return "redirect:/bills";
    }

    @PostMapping("/bills/cancel")
    public String activateUserAccount(@RequestParam(value = "billIds", required = false) List<Long> billIds,
                                      @ModelAttribute("userFunctions") List<String> userFunctions,
                                      RedirectAttributes redirectAttributes){

        if (!authenticatedUserService.hasFunctions("HDH", "QLDH")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền huỷ đơn hàng !");
            return "redirect:/bills";
        }

        try {
            for (Long billId : billIds) {
                BillDto billDto = billService.findByID(billId);
                if(billDto.getStatus().equals("doing")){
                    billService.updateBillStatus(billId,"cancel");
                }else{
                    redirectAttributes.addFlashAttribute("errorMessage", "Hủy đơn không thành công!");
                }
            }
            redirectAttributes.addFlashAttribute("message", "Hủy đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel bill. Please try again.");
        }
        return "redirect:/bills";
    }

}
