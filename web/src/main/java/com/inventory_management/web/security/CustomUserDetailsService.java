package com.inventory_management.web.security;

import com.inventory_management.web.entity.UserEntity;
import com.inventory_management.web.repository.FunctionRepository;
import com.inventory_management.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private FunctionRepository functionRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, FunctionRepository functionRepository) {
        this.userRepository = userRepository;
        this.functionRepository = functionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if(user != null) {
            // Kiểm tra trạng thái của người dùng
            if ("inactive".equals(user.getStatus())) {
                throw new UsernameNotFoundException("(*) Tài khoản đã bị ngưng kích hoạt");
            }
            // Kiểm tra vai trò của người dùng và lấy danh sách chức năng tương ứng
            List<String> functionCodes = user.getRole().equals("admin")
                    ? functionRepository.findAllFunctionCodes()
                    : userRepository.findDistinctFunctionCodesByUserId(user.getId());

            List<GrantedAuthority> authorities = functionCodes.stream()
                    .map(code -> new SimpleGrantedAuthority(code))
                    .collect(Collectors.toList());

            User authUser = new User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities
            );
            return authUser;
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
