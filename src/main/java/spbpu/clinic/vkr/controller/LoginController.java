package spbpu.clinic.vkr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/default")
    public String redirect(HttpServletRequest request) {
        if (request.isUserInRole("GENERAL")) {
            return "redirect:/appointments/edit";
        } else {
            return "redirect:/appointments/";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
