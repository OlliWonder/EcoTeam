package com.sber.java13.ecoteam.service.userdetails;

import com.sber.java13.ecoteam.constants.UserRolesConstants;
import com.sber.java13.ecoteam.model.User;
import com.sber.java13.ecoteam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
//Ищет пользователя по логину и пытается авторизовать
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Value("${spring.security.user.name}")
    private String adminUserName;
    @Value("${spring.security.user.password}")
    private String adminPassword;
    @Value("${spring.security.user.roles}")
    private String adminRole;
    
    private final UserRepository userRepository;
    
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(adminUserName)) {
            return new CustomUserDetails(null, username, adminPassword, List.of(new SimpleGrantedAuthority("ROLE_" + adminRole)));
        }
        else {
            User user = userRepository.findUserByLoginAndIsDeletedFalse(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (user.getRole().getId() == 1L) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + UserRolesConstants.USER));
            }
            else if (user.getRole().getId() == 3L) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + UserRolesConstants.AGENT));
            }
            else if (user.getRole().getId() == 2L) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + UserRolesConstants.MODERATOR));
            }
            return new CustomUserDetails(user.getId().intValue(), username, user.getPassword(), authorities);
        }
    }
}
