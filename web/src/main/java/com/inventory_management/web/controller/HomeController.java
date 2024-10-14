package com.inventory_management.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("role_current")
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        return "home/index";
    }
}
