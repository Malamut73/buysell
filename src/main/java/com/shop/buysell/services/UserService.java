package com.shop.buysell.services;

import com.shop.buysell.models.User;
import com.shop.buysell.models.enums.Role;
import com.shop.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean create(User user){

        String email = user.getEmail();

        if(userRepository.findByEmail(email) != null) return false;

        user.setActive(true);
        user.getRoles().add(Role.ROLE_ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        log.info("Saving new User with email: {}", email);

        userRepository.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public void banUser(User user) {
        if(user.isActive()){
            user.setActive(false);
            log.info("Ban user with id = {}; email: {}", user.getId(), user.getEmail());

        }else{
            user.setActive(true);
            log.info("Unban user with id = {}; email: {}", user.getId(), user.getEmail());

        }
        userRepository.save(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
