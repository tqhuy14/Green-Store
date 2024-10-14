package com.inventory_management.web.repository;

import com.inventory_management.web.entity.Bill;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {


    @Query("SELECT b FROM bill b WHERE "
            + "(:name IS NULL OR LOWER(b.ten_khach_hang) LIKE LOWER(CONCAT('%', :name, '%'))) "
            + "AND (:status IS NULL OR :status = '' OR b.status = :status)")
    Page<Bill> searchBill(@Param("name") String name,
                            @Param("status") String status,
                            Pageable pageable); // Thêm tham số Pageable


    @Transactional
    @Modifying
    @Query("UPDATE bill b SET b.status = :status WHERE b.id = :billId")
    void updateBillStatus(@Param("billId") Long billId, @Param("status") String status);

    @Transactional
    @Query("SELECT b FROM bill b WHERE b.productId = :productId")
    List<Bill> findBillsByProductId(@Param("productId") Long productId);

    @Query("SELECT SUM(b.gia_sale) FROM bill b")
    Long getTotalPriceBills();

    @Query("SELECT count(b) FROM bill b WHERE b.status = 'cancel'")
    Long getCancelBills();

    @Query("SELECT EXTRACT(MONTH FROM b.date) AS month, SUM(b.gia_sale) AS totalRevenue " +
            "FROM bill b WHERE EXTRACT(YEAR FROM b.date) = :year " +
            "GROUP BY EXTRACT(MONTH FROM b.date) ORDER BY EXTRACT(MONTH FROM b.date) ASC")
    List<Object[]> getMonthlyRevenue(@Param("year") int year);

    @Query("SELECT EXTRACT(MONTH FROM b.date) AS month, COUNT(b) AS orderCount " +
            "FROM bill b WHERE EXTRACT(YEAR FROM b.date) = :year " +
            "GROUP BY EXTRACT(MONTH FROM b.date) ORDER BY EXTRACT(MONTH FROM b.date) ASC")
    List<Object[]> getMonthlyOrderCount(@Param("year") int year);

    @Query("SELECT u.id, u.name, u.role, COUNT(b) AS orderCount, SUM(b.gia_sale) AS totalRevenue " +
            "FROM UserEntity u " +
            "LEFT JOIN bill b ON u.id = b.createdByUserId " + // Đảm bảo trường này đúng
            "WHERE EXTRACT(YEAR FROM b.date) = :year " +
            "GROUP BY u.id, u.name, u.role")
    List<Object[]> getRevenueByEmployee(@Param("year") int year);


}
