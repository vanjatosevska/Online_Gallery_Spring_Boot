package com.example.gallery.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {

    private List<String> allowedNames;

    @PostConstruct
    public void init() {
        this.allowedNames = new ArrayList<>();
        this.allowedNames.add("emtUser");
    }

    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String info,
                               Model model) {
        model.addAttribute("info", info);
        return "login";
    }

//    @PostMapping
//    public String loginUser(HttpServletRequest req) {
//        String username = req.getParameter("username");
//        if (username != null && this.allowedNames.contains(username)) {
//            req.getSession().setAttribute("username", username);
//            return "redirect:/artworks";
//        } else {
//            return "redirect:/login";
//        }
//    }
}
