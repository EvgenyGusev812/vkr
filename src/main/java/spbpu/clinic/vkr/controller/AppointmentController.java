package spbpu.clinic.vkr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import spbpu.clinic.vkr.entity.Appointment;
import spbpu.clinic.vkr.entity.Doctor;
import spbpu.clinic.vkr.entity.Patient;
import spbpu.clinic.vkr.entity.TypeOfMedicalCare;
import spbpu.clinic.vkr.mapper.AppointmentMapper;
import spbpu.clinic.vkr.services.*;
import spbpu.clinic.vkr.types.SelectedItem;
import spbpu.clinic.vkr.wrapper.AppointmentWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private TypeOfMedicalCareService typeOfMedicalCareService;


    @GetMapping("/")
    public String getAllAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAll());
        return "appointments";
    }

    @GetMapping("/patient-apps/{id}")
    public String getPatientAppointments(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("appointments", appointmentService.findAllByPatient(id));
        return "appointments";
    }

    @GetMapping("/{id}")
    public String getAppointmentById(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("appointment", appointmentService.getById(id));
        return "appointment-page";
    }

    @GetMapping("/find-by-keyword")
    public String getAllAppointmentsByKeyword(String keyword, Model model) {
        model.addAttribute("appointments", appointmentService.findAllByKeyword(keyword));
        return "appointments";
    }


    @PostMapping("/save")
    public String saveAppointment(Model model, Appointment appointment) {
        model.addAttribute("appointment", appointmentService.save(appointment));
        return "appointment-page";
    }


    // admin

    @PostMapping("/delete/page/{pageNo}/{id}")
    public String deleteAppointment(Model model,
                                    @PathVariable(name = "id") Long id,
                                    @PathVariable(name = "pageNo") Integer pageNo) {
        appointmentService.delete(id);
        Page<Appointment> page = appointmentService.findPage(pageNo);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Appointment> appointments = page.getContent();

        if (CollectionUtils.isEmpty(appointments) && pageNo > 0) {
            page = appointmentService.findPage(--pageNo);
            totalPages = page.getTotalPages();
            totalItems = page.getTotalElements();
            appointments = page.getContent();
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("appointments", appointments);
        return "admin-appointments";
    }


    @GetMapping("/edit/page/{pageNo}")
    public String getAdminAppointments(Model model, @PathVariable(value = "pageNo") Integer pageNo) {
        Page<Appointment> page = appointmentService.findPage(pageNo);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Appointment> appointments = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("appointments", appointments);
        return "admin-appointments";
    }

    @GetMapping("/new")
    public String newAppointment(Model model) {
        model.addAttribute("appointment", appointmentMapper.toWrapper(new Appointment()));
        model.addAttribute("doctors", this.getAllDoctors());
        model.addAttribute("patients", this.getAllPatients());
        model.addAttribute("typeOfMedCare", this.getAllTypes());
        return "new-appointment";
    }

    @PostMapping("/new/save")
    public String saveNewAppointment(Model model, @ModelAttribute("appointment") AppointmentWrapper appointmentWrapper) {
        Appointment appointment = appointmentMapper.toEntity(appointmentWrapper);
        Appointment newApp = appointmentService.save(appointment);
        model.addAttribute("appointment", appointmentMapper.toWrapper(newApp));
        model.addAttribute("doctors", this.getAllDoctors());
        model.addAttribute("patients", this.getAllPatients());
        model.addAttribute("typeOfMedCare", this.getAllTypes());
        return "admin-appointment-page";
    }

    @GetMapping("/edit/{id}")
    public String getAdminAppointmentById(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("appointment", appointmentMapper.toWrapper(appointmentService.getById(id)));
        model.addAttribute("doctors", this.getAllDoctors());
        model.addAttribute("patients", this.getAllPatients());
        model.addAttribute("typeOfMedCare", this.getAllTypes());
        return "admin-appointment-page";
    }

    @GetMapping("/patient-apps/edit/page/{pageNo}/{id}")
    public String getAdminPatientAppointments(Model model,
                                              @PathVariable(name = "id") Long id,
                                              @PathVariable(name = "pageNo") Integer pageNo) {
        Page<Appointment> page = appointmentService.findPage(pageNo); // TODO: 08.05.2023 Доделать
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Appointment> appointments = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("appointments", appointments);
        model.addAttribute("appointments", appointmentService.findAllByPatient(id));
        return "admin-appointments";
    }

    private List<SelectedItem> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<SelectedItem> selectedItems = new ArrayList<>();
        doctors.forEach(e -> {
            selectedItems.add(new SelectedItem(e.getId(), e.getUser().getBio().getSurname() + " "
                    + e.getUser().getBio().getName() + " " + e.getUser().getBio().getMiddlename()));
        });
        return selectedItems;
    }

    private List<SelectedItem> getAllPatients() {
        List<Patient> patients = patientService.getAll();
        List<SelectedItem> selectedItems = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        patients.forEach(e -> {
            selectedItems.add(new SelectedItem(e.getId(), e.getBio().getSurname() +  " " +
                    e.getBio().getName() + " " + e.getBio().getMiddlename() + " " + simpleDateFormat.format(e.getBio().getBirthdate())));
        });
        return selectedItems;
    }

    private List<SelectedItem> getAllTypes() {
        List<TypeOfMedicalCare> typeOfMedicalCares = typeOfMedicalCareService.findAll();
        List<SelectedItem> selectedItems = new ArrayList<>();
        typeOfMedicalCares.forEach(e -> {
            selectedItems.add(new SelectedItem(e.getId(), e.getLabel()));
        });
        return selectedItems;
    }
}
