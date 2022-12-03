package com.shop.buysell.services;

import com.shop.buysell.models.User;
import com.shop.buysell.models.enums.Role;
import com.shop.buysell.repositories.UserRepository;
import com.shop.buysell.userdto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderServiceImpl mailSenderServiceImpl;

    public boolean create(UserDTO userDTO){

        String email = userDTO.getEmail();

        if(userRepository.findByEmail(email) != null) return false;

        User user = User.builder()
                        .name(userDTO.getName())
                        .phoneNumber(userDTO.getPhoneNumber())
                        .email(userDTO.getEmail())
                        .active(true)
                        .roles(new HashSet<>())
                        .password(passwordEncoder.encode(userDTO.getPassword()))
                        .activationCode(UUID.randomUUID().toString())
                        .build();
        user.getRoles().add(Role.ROLE_ADMIN);

//        userDTO.setActive(true);
//        userDTO.getRoles().add(Role.ROLE_ADMIN);
//        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//        userDTO.setActivationCode(UUID.randomUUID().toString());

        log.info("Saving new User with email: {}", email);

        userRepository.save(user);

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Sweater. Please visit next link: " +
                        "http://ul-portal.ru/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        if(!StringUtils.isEmpty(userDTO.getEmail())){
            mailSenderServiceImpl.send(userDTO.getEmail(), "Activation code", message);
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


    public UserDTO findByEmail(String email) {
        return toDto(userRepository.findByEmail(email));
    }

    private UserDTO toDto(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }
}
