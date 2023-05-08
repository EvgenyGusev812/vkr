package spbpu.clinic.vkr.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import spbpu.clinic.vkr.entity.Doctor;
import spbpu.clinic.vkr.entity.Role;
import spbpu.clinic.vkr.entity.User;
import spbpu.clinic.vkr.services.DoctorService;
import spbpu.clinic.vkr.wrapper.UserWrapper;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserMapper {

    @Autowired
    private DoctorService doctorService;

    public UserWrapper toWrapper(User user) {
        UserWrapper wrapper = new UserWrapper();
        wrapper.setId(user.getId());
        wrapper.setActive(Boolean.TRUE);
        wrapper.setUsername(user.getUsername());
        wrapper.setPassword(user.getPassword());
        wrapper.setBio(user.getBio());
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            Role role = user.getRoles().stream().findFirst().get();
            wrapper.setRole(role.name());
            wrapper.setRoleLabel(role.getLabel());
        }
        return wrapper;
    }

    public User toEntity(UserWrapper wrapper) {
        User user = new User();
        user.setActive(wrapper.isActive());
        user.setBio(wrapper.getBio());
        user.setPassword(wrapper.getPassword());
        user.setUsername(wrapper.getUsername());
        user.setId(wrapper.getId());
        Set<Role> set = new HashSet<>();
        if (wrapper.getRole() != null) {
            if (wrapper.getRole().equals("ROLE_GENERAL")) {
                set.add(Role.ROLE_GENERAL);
                user.setRoles(set);
            }
            else {
//                Doctor doc = doctorService.getByUser(user);
//                if (doc != null) {
//                    doctorService.saveDoctor(doc);
//                } else {
                    Doctor doctor = new Doctor();
                    doctor.setUser(user);
                    doctorService.saveDoctor(doctor);
                    set.add(Role.ROLE_DOCTOR);
                    user.setRoles(set);
//                }
            }
        }
        return user;
    }
}
