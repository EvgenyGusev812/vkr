package spbpu.clinic.vkr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spbpu.clinic.vkr.entity.Patient;
import spbpu.clinic.vkr.entity.TypeOfMedicalCare;
import spbpu.clinic.vkr.services.PatientService;

import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/page/{pageNo}")
    public String getAllPatients(Model model,
                                 @PathVariable(name = "pageNo") Integer pageNo) {
        Page<Patient> page = patientService.findPage(pageNo);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Patient> appointments = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("medCares", appointments);
        model.addAttribute("patients", patientService.findPage(pageNo));
        return "patients";
    }

    @GetMapping("/{id}")
    public String getPatient(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("patient", patientService.getById(id));
        return "patient-page";
    }

    @GetMapping("/find-by-keyword")
    public String getAllPatientsByKeyword(String keyword, Model model) {
        model.addAttribute("patients", patientService.findAllByKeyword(keyword));
        return "patients";
    }

    @PostMapping("/save")
    public String savePatient(Patient patient, Model model) {
        model.addAttribute("patient", patientService.savePatient(patient));
        return "patient-page";
    }

    //admin

    @PostMapping("/delete/page/{pageNo}/{id}")
    public String getAdminPatients(Model model,
                                   @PathVariable(name = "id") Long id,
                                   @PathVariable(name = "pageNo") Integer pageNo) {
        patientService.delete(id);
        Page<Patient> page = patientService.findPage(pageNo);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Patient> appointments = page.getContent();

        if (CollectionUtils.isEmpty(appointments) && pageNo > 0) {
            page = patientService.findPage(--pageNo);
            totalPages = page.getTotalPages();
            totalItems = page.getTotalElements();
            appointments = page.getContent();
        }
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("medCares", appointments);

        model.addAttribute("patients", page);
        return "admin-patients";
    }

    @GetMapping("/edit/page/{pageNo}")
    public String getAdminPatients(Model model, @PathVariable(name = "pageNo") Integer pageNo) {
        Page<Patient> page = patientService.findPage(pageNo);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Patient> appointments = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("medCares", appointments);
        model.addAttribute("patients", patientService.findPage(pageNo));
        return "admin-patients";
    }

    @GetMapping("/edit/{id}")
    public String getAdminPatient(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("patient", patientService.getById(id));
        return "admin-patient-page";
    }

    @PostMapping("/edit/save")
    public String saveAdminPatient(Patient patient, Model model) {
        model.addAttribute("patient", patientService.savePatient(patient));
        return "admin-patient-page";
    }

}
