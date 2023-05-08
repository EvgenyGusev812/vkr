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
import spbpu.clinic.vkr.entity.Appointment;
import spbpu.clinic.vkr.entity.TypeOfMedicalCare;
import spbpu.clinic.vkr.services.TypeOfMedicalCareService;

import java.lang.reflect.Type;
import java.util.List;

@Controller
@RequestMapping("/med-cares")
public class TypeOfMedicalCareController {

    @Autowired
    private TypeOfMedicalCareService typeOfMedicalCareService;

    @GetMapping("/page/{pageNo}")
    public String getAllMedCares(Model model,
                                 @PathVariable(name = "pageNo") Integer pageNo) {
        Page<TypeOfMedicalCare> page = typeOfMedicalCareService.findPage(pageNo);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<TypeOfMedicalCare> appointments = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("medCares", appointments);
        return "admin-med-cares";
    }

    @PostMapping("/delete/page/{pageNo}/{id}")
    public String deleteMedCare(@PathVariable(name = "id") Long id,
                                @PathVariable(name = "pageNo") Integer pageNo,
                                Model model) {
        typeOfMedicalCareService.deleteById(id);
        Page<TypeOfMedicalCare> page = typeOfMedicalCareService.findPage(pageNo);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<TypeOfMedicalCare> appointments = page.getContent();

        if (CollectionUtils.isEmpty(appointments) && pageNo > 0) {
            page = typeOfMedicalCareService.findPage(--pageNo);
            totalPages = page.getTotalPages();
            totalItems = page.getTotalElements();
            appointments = page.getContent();
        }
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("medCares", appointments);
        model.addAttribute("medCares", typeOfMedicalCareService.findAll());
        return "admin-med-cares";
    }

    @GetMapping("/new")
    public String newMedCare(Model model) {
        model.addAttribute("medCare", new TypeOfMedicalCare());
        return "new-med-care";
    }

    @PostMapping("/new/save")
    public String newMedCare(Model model, TypeOfMedicalCare typeOfMedicalCare) {
        model.addAttribute("medCare", typeOfMedicalCareService.saveMedicalCare(typeOfMedicalCare));
        return "new-med-care";
    }

    @GetMapping("/edit/{id}")
    public String newMedCare(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("medCare", typeOfMedicalCareService.getById(id));
        return "new-med-care";
    }
}
