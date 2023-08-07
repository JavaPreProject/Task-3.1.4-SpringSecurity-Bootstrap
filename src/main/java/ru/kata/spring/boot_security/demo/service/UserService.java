package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.configs.WebSecurityConfig;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.security.UserDetailsSecurity;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private ApplicationContext applicationContext;

    @Autowired
    public void context(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext; }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(int id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElse(null);
    }

    public User findOne() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetailsSecurity) authentication.getPrincipal()).getUser();
    }

    @Transactional
    public void save(User user) {
        user.setPassword(applicationContext.getBean(WebSecurityConfig.class)
                        .passwordEncoder().encode(user
                                .getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void update(User user) {
        user.setPassword(applicationContext.getBean(WebSecurityConfig.class)
                .passwordEncoder().encode(user
                        .getPassword()));
        userRepository.save(user);

    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);

    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(nickname);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", nickname));
        }
        return new UserDetailsSecurity(user.get());
    }
}
