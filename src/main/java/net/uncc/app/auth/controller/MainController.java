package net.uncc.app.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "auth/index";
    }

    @GetMapping("/authenticate")
    public String login(Model model) {
        return "auth/authenticate";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }
}
