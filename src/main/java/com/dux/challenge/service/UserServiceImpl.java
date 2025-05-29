package com.dux.challenge.service;

import com.dux.challenge.model.User;
import com.dux.challenge.repository.UserRepository;
import com.dux.challenge.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new CustomUserDetails(
            user.getUsername(),
            user.getPassword(),
            user.isAccountNonExpired(),
            user.isAccountNonLocked(),
            user.isCredentialsNonExpired(),
            user.isEnabled()
        );
    }

    @Override
    public void initializeDefaultUser() {
        if (userRepo.findByUsername("test").isEmpty()) {
            User test = new User();
            test.setUsername("test");
            test.setPassword(passwordEncoder.encode("12345"));
            test.setEnabled(true);
            test.setAccountNonLocked(true);
            // Fijamos expiraciones a un futuro lejano
            test.setAccountExpiryDate(LocalDate.now().plusYears(100));
            test.setCredentialsExpiryDate(LocalDate.now().plusYears(100));
            userRepo.save(test);
        }
    }
}
