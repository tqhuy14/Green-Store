package com.inventory_management.web.service.Impl;

import com.inventory_management.web.dto.BillDto;
import com.inventory_management.web.entity.Bill;
import com.inventory_management.web.entity.UserEntity;
import com.inventory_management.web.mapper.BillMapper;
import com.inventory_management.web.repository.BillRepository;
import com.inventory_management.web.repository.ProductRepository;
import com.inventory_management.web.repository.UserRepository;
import com.inventory_management.web.service.BillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    BillRepository billRepository;
    ProductRepository productRepository;
    UserRepository userRepository;

    public BillServiceImpl(BillRepository billRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.billRepository = billRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<BillDto> searchBills(String name, String status, Pageable pageable) {
        Page<Bill> billsPage = billRepository.searchBill(name, status, pageable);
        return billsPage.map(BillMapper :: toDTO);
    }

    @Override
    public BillDto findByID(Long billID) {
        Bill bill = billRepository.findById(billID).orElse(null);
        return BillMapper.toDTO(bill);
    }

    @Override
    public void updateBill(BillDto billDto) {
        Bill bill = BillMapper.toEntity(billDto);
        billRepository.save(bill);
    }

    @Override
    public void delete(Long billId) {
        billRepository.deleteById(billId);
    }

    @Override
    public void saveBill(BillDto billDto) {
        Long userId = 0L;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Lấy username từ UserDetails
            String username = authentication.getName();

            // Tìm user từ database dựa trên username
            UserEntity user = userRepository.findByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }

        Bill bill = new Bill();
        bill.setDate(LocalDateTime.now());
        bill.setTen_khach_hang(billDto.getTenKhachHang());
        bill.setSo_khach_hang(billDto.getSoKhachHang());
        bill.setProductId(billDto.getProductId());
        bill.setSo_luong_mua(billDto.getSoLuongMua());
        bill.setGia_goc(billDto.getGiaGoc());
        bill.setGia_sale(billDto.getGiaSale());
        bill.setEventId(billDto.getEventId());
        bill.setCreatedByUserId(userId);

        if(billDto.getPaymentMethod().equals("cash")){
            bill.setSo_tien_nhan_tu_khach(billDto.getSoTienNhanTuKhach());
            bill.setSo_tien_tra_lai(billDto.getSoTienTraLai());
            bill.setMua_tai_cua_hang(true);
            bill.setStatus("done");
        }

        if(billDto.getPaymentMethod().equals("delivery")){
            bill.setAddress(billDto.getAddress());
            bill.setTien_coc(billDto.getTienCoc());
            bill.setTien_ship(billDto.getTienShip());
            bill.setTong_tien_nhan_khi_ship(billDto.getTongTienNhanKhiShip());
            bill.setStatus("doing");
        }
        billRepository.save(bill);
        productRepository.updateSoLuongHienTai(billDto.getProductId(), billDto.getSoLuongMua());
    }

    @Override
    public void updateBillStatus(Long billId, String status) {
        billRepository.updateBillStatus(billId, status);
    }

    @Override
    public List<BillDto> findBillsByProductId(Long productId) {
        List<Bill> bills = billRepository.findBillsByProductId(productId);
        return bills.stream().map(BillMapper :: toDTO).collect(Collectors.toList());
    }
}
