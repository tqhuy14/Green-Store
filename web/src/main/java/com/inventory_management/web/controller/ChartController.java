package com.inventory_management.web.controller;

import com.inventory_management.web.repository.BillRepository;
import com.inventory_management.web.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ChartController {

    BillRepository billRepository;
    ProductRepository productRepository;

    public ChartController(BillRepository billRepository, ProductRepository productRepository) {
        this.billRepository = billRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/chart")
    public String chart(Model model) {
        int currentMonth = LocalDate.now().getMonthValue(); // Lấy tháng hiện tại (1 - 12)
        int currentYear = LocalDate.now().getYear(); // Lấy tháng hiện tại (1 - 12)
        Long totalPriceGet = billRepository.getTotalPriceBills();
        Long totalPriceProducts = productRepository.totalPriceProducts();
        Long totalBills = billRepository.count();
        Long cancelBills = billRepository.getCancelBills();

        List<Object[]> monthlyRevenue = billRepository.getMonthlyRevenue(currentYear); // Doanh thu hàng tháng
        List<Object[]> monthlyOrders = billRepository.getMonthlyOrderCount(currentYear); // Số lượng đơn hàng mỗi tháng

        // Khởi tạo danh sách cho 12 tháng
        List<Integer> months = new ArrayList<>();
        List<Long> revenues = new ArrayList<>(Collections.nCopies(12, 0L)); // Mặc định doanh thu là 0
        List<Long> orders = new ArrayList<>(Collections.nCopies(12, 0L)); // Mặc định đơn hàng là 0

        // Thêm tháng vào danh sách
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }

        // Cập nhật doanh thu
        for (Object[] result : monthlyRevenue) {
            int month = ((Number) result[0]).intValue(); // Tháng
            Long revenue = ((Number) result[1]).longValue(); // Doanh thu
            revenues.set(month - 1, revenue); // Cập nhật doanh thu tại vị trí tháng tương ứng
        }

        // Cập nhật số lượng đơn hàng
        for (Object[] result : monthlyOrders) {
            int month = ((Number) result[0]).intValue(); // Tháng
            Long orderCount = ((Number) result[1]).longValue(); // Số lượng đơn hàng
            orders.set(month - 1, orderCount); // Cập nhật số lượng đơn hàng tại vị trí tháng tương ứng
        }

        List<Object[]> revenueByEmployee = billRepository.getRevenueByEmployee(currentYear);

        // Gửi dữ liệu đến Thymeleaf
        model.addAttribute("revenueByEmployee", revenueByEmployee);
        model.addAttribute("months", months); // Tháng
        model.addAttribute("revenues", revenues); // Doanh thu
        model.addAttribute("orders", orders); // Số lượng đơn hàng
        model.addAttribute("year", currentYear);

        model.addAttribute("month", currentMonth);
        model.addAttribute("totalPrice", totalPriceGet);
        model.addAttribute("totalPriceProducts", totalPriceProducts);
        model.addAttribute("totalBills", totalBills);
        model.addAttribute("cancelBills", cancelBills);

        return "chart/chart";
    }
}
