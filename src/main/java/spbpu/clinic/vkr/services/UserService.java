package spbpu.clinic.vkr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;
import spbpu.clinic.vkr.entity.Appointment;
import spbpu.clinic.vkr.entity.Doctor;
import spbpu.clinic.vkr.entity.User;
import spbpu.clinic.vkr.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private BioService bioService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public void delete(Long id) {
        User user = userRepository.getById(id);
        if (user != null) {
            Doctor doctor = doctorService.getByUser(user);
            if (doctor != null) {
                List<Appointment> appointments = appointmentService.findAllByDoctor(doctor.getId());
                if (!CollectionUtils.isEmpty(appointments)) {
                    appointmentService.deleteList(appointments);
                }
                doctorService.deleteById(doctor.getId());
                return;
            }
        }
        user = userRepository.getById(id);
        if (user != null) {
            userRepository.deleteById(user.getId());
        }
    }

    public Map<String, Object> saveUser(User user) {
        user.setActive(Boolean.TRUE);
        String genPass = "";
        if (StringUtils.isEmpty(user.getPassword())) {
            genPass = generatePassword();
            user.setPassword(passwordEncoder.encode(generatePassword()));
        }
        return Map.of("user", userRepository.save(user), "password", genPass);
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
