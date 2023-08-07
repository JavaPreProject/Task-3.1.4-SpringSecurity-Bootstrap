package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.NameValidator;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping
public class AdminController {
    private final RoleService roleService;
    private final UserService userService;
    private final NameValidator nameValidator;


    @Autowired
    public AdminController(RoleService roleService, UserService userService, NameValidator nameValidator) {
        this.roleService = roleService;
        this.userService = userService;
        this.nameValidator = nameValidator;
    }

    @GetMapping("/admin/user")
    public String showAllUsers(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user",userService.findOne());
        model.addAttribute("roles", roleService.getRoles());
        return "all_users";//adminPage
    }


    @GetMapping("/admin/new")//add
    public String showPageCreatingUser(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("user",userService.findOne());
        model.addAttribute("role", roleService.getRoles());
        return "new";
    }

    @PostMapping("/admin/user")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
       nameValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/wrong";
        }
        userService.save(user);
        return "redirect:/admin/user";
    }

    @GetMapping("/wrong")//add
    public String wrong(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("user",userService.findOne());
        model.addAttribute("role", roleService.getRoles());
        return "new";
    }

    @PatchMapping("/admin/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            return "new";
        }
        model.addAttribute("roles",roleService.getRoles());
        userService.update(user);
        return "redirect:/admin/user";
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin/user";
    }

    @GetMapping("/")
    private String showWelcomePage() {
        return "login";
    }

    @GetMapping("/login")
    public String getAuthenticated() {
        return "login";
    }

}
