package com.e_commerce.made2automade.HomeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.e_commerce.made2automade.Dao.UserRepositry;
import com.e_commerce.made2automade.Entities.User;

import jakarta.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private UserRepositry repositry;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    // it's a Home handler
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home - Contact Management");
        model.addAttribute("flag", false);
        return "home";
    }

    // it's a About handler
    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    // it's a Signup handler
    @RequestMapping("/signup")
    public String sign(Model model) {
        model.addAttribute("title", "Register - Contact Management");
        model.addAttribute("user", new User());
        return "signup";
    }

    // this handler for registring the user
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String form(@Valid @ModelAttribute("user") User user, BindingResult result,
            @RequestParam(value = "agriment", defaultValue = "false") boolean agriment, Model model) {
        model.addAttribute("flag1", false);
        model.addAttribute("flag1", false);
        if (agriment && result.hasErrors() == false) {
            user.setEnabled(true);
            user.setRole("ROLE_USER");
            user.setImageUrl("banner.jpg");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.repositry.save(user);
            model.addAttribute("user", new User());
            model.addAttribute("flag1", true);
            return "signup";
        } else {
            model.addAttribute("flag2", true);
            model.addAttribute("user", user);
            System.out.println("Error is " + result);
            return "signup";
        }
    }
     
    // this is handler for login
    @GetMapping("/signin")
    public String customLogin(Model model){
        model.addAttribute("title", "User-Login");
        return "login";
    }

    //this is for start
    @GetMapping("/let-start")
    public String start(Model model){
        model.addAttribute("title", "Start-Contact Management");
        model.addAttribute("flag", true);
        return "home";
    }
}
