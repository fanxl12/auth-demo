package com.fanxl.auth.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 18:10
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return User.withUsername(username)
//                .password(bCryptPasswordEncoder.encode("123456"))
                .password("$2a$10$Hteb93vooN7csdNN8xaTROF8Nnl/dUkfkuel2ZKyFwgTEvdI7gln6")
                .authorities("ROLE_ADMIN")
                .build();
    }
}
