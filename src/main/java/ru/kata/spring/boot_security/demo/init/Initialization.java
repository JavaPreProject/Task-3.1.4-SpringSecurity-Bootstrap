package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Initialization {
    private final RoleRepository roleRepository;
    private final UserService usersService;

    @Autowired
    public Initialization(RoleRepository roleRepository, UserService usersService) {
        this.roleRepository = roleRepository;
        this.usersService = usersService;
    }

    @PostConstruct
    public void initTestUsers() {
        Role userTest = new Role("ROLE_USER");
        Role adminTest = new Role("ROLE_ADMIN");
        roleRepository.save(userTest);
        roleRepository.save(adminTest);
        Set<Role> userSet = Stream.of(userTest).collect(Collectors.toSet());
        Set<Role> adminSet = Stream.of(adminTest).collect(Collectors.toSet());

        User user = new User("NoName", "NoName", "user", "myEmail@myemail.com", "user", userSet);
        User admin = new User("NoName", "NoName", "admin", "youEmail@youemail.com", "admin", adminSet);
        usersService.save(user);
        usersService.save(admin);
    }
}
