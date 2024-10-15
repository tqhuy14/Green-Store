package com.inventory_management.web.security;
import com.inventory_management.web.entity.UserEntity;
import com.inventory_management.web.repository.FunctionRepository;
import com.inventory_management.web.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticatedUserService {
    private final UserRepository userRepository;
    private final FunctionRepository functionRepository;

    public AuthenticatedUserService(UserRepository userRepository, FunctionRepository functionRepository) {
        this.userRepository = userRepository;
        this.functionRepository = functionRepository;
    }

    // Lấy các mã chức năng của người dùng hiện tại
    public List<String> getUserFunctionCodes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserEntity user = userRepository.findByUsername(username);
            if (user != null) {
                return "admin".equals(user.getRole())
                        ? functionRepository.findAllFunctionCodes() // Lấy tất cả các mã chức năng nếu là admin
                        : userRepository.findDistinctFunctionCodesByUserId(user.getId()); // Lấy mã chức năng riêng của user
            }
        }
        return List.of(); // Trả về danh sách rỗng nếu không có user
    }

    // Kiểm tra xem người dùng có các chức năng được yêu cầu hay không
    public boolean hasRequiredFunctions(List<String> requiredFunctionCodes) {
        List<String> userFunctions = getUserFunctionCodes();
        return userFunctions.containsAll(requiredFunctionCodes);
    }

    // Trong AuthenticatedUserService
    public boolean hasFunctions(String... functionCodes) {
        return hasRequiredFunctions(List.of(functionCodes));
    }

}


