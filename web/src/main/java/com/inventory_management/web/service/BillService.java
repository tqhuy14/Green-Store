package com.inventory_management.web.service;

import com.inventory_management.web.dto.BillDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BillService {

    Page<BillDto> searchBills(String name, String status, Pageable pageable);

    BillDto findByID(Long billID);

    void updateBill(@Valid BillDto billDto);

    void delete(Long billId);

    void saveBill(@Valid BillDto billDto);

    void updateBillStatus(Long billId, String status);

    List<BillDto> findBillsByProductId(Long productId);
}
