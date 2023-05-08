package spbpu.clinic.vkr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;
import spbpu.clinic.vkr.entity.Role;
import spbpu.clinic.vkr.entity.User;
import spbpu.clinic.vkr.mapper.UserMapper;
import spbpu.clinic.vkr.services.UserService;
import spbpu.clinic.vkr.wrapper.UserWrapper;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String getAllEmployees(Model model) {
        model.addAttribute("users", userService.getAllUsers().
                stream().
                map(userMapper::toWrapper).
                collect(Collectors.toList()));
        return "employees";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("employee", userMapper.toWrapper(userService.getById(id)));
        model.addAttribute("role", Set.of(Role.ROLE_DOCTOR, Role.ROLE_GENERAL));
        return "employee-page";
    }

    @PostMapping("/delete/{id}")
    public String delete(Model model, @PathVariable(name = "id") Long id) {
        userService.delete(id);
        model.addAttribute("users", userService.getAllUsers().stream().
                map(userMapper::toWrapper).
                collect(Collectors.toList()));
        return "employees";
    }

    @PostMapping("/save")
    public String saveEmployee(Model model, UserWrapper wrapper) {
        if (StringUtils.isEmpty(wrapper.getPassword())) {
            Map<String, Object> map = userService.saveUser(userMapper.toEntity(wrapper));
            model.addAttribute("pass", map.get("password"));
            return "admin-generated-password";
        } else {
            Map<String, Object> map = userService.saveUser(userMapper.toEntity(wrapper));
            User user = (User) map.get("user");
            model.addAttribute("employee", userMapper.toWrapper(user));
            model.addAttribute("role", Set.of(Role.ROLE_DOCTOR, Role.ROLE_GENERAL));
            return "employee-page";
        }
    }

}
