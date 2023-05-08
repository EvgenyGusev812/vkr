package spbpu.clinic.vkr.controller;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import spbpu.clinic.vkr.entity.*;
import spbpu.clinic.vkr.services.DoctorService;
import spbpu.clinic.vkr.services.PatientService;
import spbpu.clinic.vkr.services.ReportService;
import spbpu.clinic.vkr.services.TypeOfMedicalCareService;
import spbpu.clinic.vkr.types.SelectedItem;
import spbpu.clinic.vkr.wrapper.ReportWrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private TypeOfMedicalCareService typeOfMedicalCareService;

    @GetMapping("/")
    public String getAllReports(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        return "admin-reports";
    }

    @GetMapping("/{id}")
    public String getReport(Model model, @PathVariable(name = "id") Long id) {
        Report report = reportService.getById(id);
        ReportWrapper wrapper = new ReportWrapper();
        wrapper.setCode(report.getCode());
        wrapper.setLabel(report.getLabel());
        wrapper.setId(report.getId());
        model.addAttribute("report", wrapper);
        model.addAttribute("doctors", this.getAllDoctors());
        model.addAttribute("patients", this.getAllPatients());
        model.addAttribute("typeOfMedCare", this.getAllTypes());
        model.addAttribute("reportTypes", this.getAllReportTypes());
        return "admin-report-page";
    }

    @GetMapping("/get")
    public void downloadReport(ReportWrapper reportWrapper, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        String headerValue = "attachment; filename = " + reportWrapper.getCode() + "_" + convertDate(new Date());
        response.setHeader(headerKey, headerValue);
        reportService.downloadReport(reportWrapper, outputStream);
        outputStream.close();
    }


    private List<SelectedItem> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<SelectedItem> selectedItems = new ArrayList<>();
        selectedItems.add(new SelectedItem());
        doctors.forEach(e -> {
            selectedItems.add(new SelectedItem(e.getId(), e.getUser().getBio().getSurname() + " "
                    + e.getUser().getBio().getName() + " " + e.getUser().getBio().getMiddlename()));
        });
        return selectedItems;
    }

    private List<SelectedItem> getAllPatients() {
        List<Patient> patients = patientService.getAll();
        List<SelectedItem> selectedItems = new ArrayList<>();
        selectedItems.add(new SelectedItem());
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
        selectedItems.add(new SelectedItem());
        typeOfMedicalCares.forEach(e -> {
            selectedItems.add(new SelectedItem(e.getId(), e.getLabel()));
        });
        return selectedItems;
    }

    private List<SelectedItem> getAllReportTypes() {
        List<SelectedItem> selectedItems = new ArrayList<>();
        ReportType[] types = ReportType.values();
        for (ReportType reportType : types) {
            selectedItems.add(new SelectedItem(1L, reportType.name()));
        }
        return selectedItems;
    }

    private String convertDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return date != null ? simpleDateFormat.format(date) : null;
    }
}
