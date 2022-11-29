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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderServiceImpl mailSenderServiceImpl;

    public boolean create(User user){

        String email = user.getEmail();


        if(userRepository.findByEmail(email) != null) return false;

        user.setActive(true);
        user.getRoles().add(Role.ROLE_ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());

        log.info("Saving new User with email: {}", email);

        userRepository.save(user);

        String message = String.format(
//                "Hello, %s! \n" +
//                        "Welcome to Sweater. Please visit next link: " +
//                        "http://localhost:8080/activate/%s",
                "Hello, %s! \n" +
                        "Welcome to Sweater. Please visit next link: " +
                        "http://ul-portal.ru/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        if(!StringUtils.isEmpty(user.getEmail())){
            mailSenderServiceImpl.send(user.getEmail(), "Activation code", message);
        }

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

    public boolean activateUser(String code) {

       User user = userRepository. findByActivationCode(code);
       if(user == null){
           return false;
       }

       user.setActivationCode(null);
       userRepository.save(user);

       return true;

    }
}
