package spbpu.clinic.vkr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spbpu.clinic.vkr.entity.Doctor;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @GetMapping("/")
    public String getAllDoctors() {
        return "header";
    }

    @GetMapping("/id")
    public String getDoctorById(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor-page";
    }
}
