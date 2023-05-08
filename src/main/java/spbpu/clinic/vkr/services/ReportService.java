package spbpu.clinic.vkr.services;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import spbpu.clinic.vkr.entity.Appointment;
import spbpu.clinic.vkr.entity.Bio;
import spbpu.clinic.vkr.entity.Report;
import spbpu.clinic.vkr.entity.ReportType;
import spbpu.clinic.vkr.repository.ReportRepository;
import spbpu.clinic.vkr.wrapper.ReportWrapper;

import javax.servlet.ServletOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repository;

    @Autowired
    private AppointmentService appointmentService;

    public List<Report> getAllReports() {
        return repository.findAll();
    }

    public Report getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void downloadReport(ReportWrapper reportWrapper, ServletOutputStream outputStream) {
        if (reportWrapper.getCode().equals("AllAppsReport")) {
            allAppsReport(reportWrapper, outputStream);
        }
    }

    private void allAppsReport(ReportWrapper report, ServletOutputStream outputStream) {
        switch (report.getReportType()) {
            case "XLSX" -> generateExcelAllAppointments(report, outputStream);
            case "XML" -> generateXMLAllAppointments(report, outputStream);
        }
    }


    @SneakyThrows
    public void generateXMLAllAppointments(ReportWrapper report, ServletOutputStream outputStream) {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element reportRoot = document.createElement("Отчет");
        document.appendChild(reportRoot);
        List<Appointment> appointmentList = prepareDataForReport(report);
        Element appSize = document.createElement("КолвоЗаписей");
        appSize.setTextContent(String.valueOf(appointmentList.size()));
        reportRoot.appendChild(appSize);
        for (Appointment appointment : appointmentList) {
            Element order = document.createElement("Запись");
            Element number = document.createElement("Номер");
            number.setTextContent(String.valueOf(appointment.getId()));

            Element date = document.createElement("Дата");
            date.setTextContent(convertDate(appointment.getDate()));

            Element type = document.createElement("ВидУслуга");
            type.setTextContent(appointment.getTypeOfMedicalCare() != null ? appointment.getTypeOfMedicalCare().getLabel() : "");

            Element fioPat = document.createElement("ФИОПациент");
            Bio bio = appointment.getPatient().getBio();
            fioPat.setTextContent(bio.getSurname() + " " + bio.getName() + " " + bio.getMiddlename());

            bio = appointment.getDoctor().getUser().getBio();
            Element birthDatePat = document.createElement("ДатаРождПациент");
            birthDatePat.setTextContent(convertDateWithTime(bio.getBirthdate()));

            Element fioDoc = document.createElement("ФИОДоктор");
            fioDoc.setTextContent(bio.getSurname() + " " + bio.getName() + " " + bio.getMiddlename());

            order.appendChild(number);
            order.appendChild(date);
            order.appendChild(date);
            order.appendChild(type);
            order.appendChild(fioPat);
            order.appendChild(birthDatePat);
            order.appendChild(fioDoc);
            reportRoot.appendChild(order);

        }
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult streamResult = new StreamResult();
        streamResult.setOutputStream(outputStream);
        tr.transform(source, streamResult);
    }
    @SneakyThrows
    public void generateExcelAllAppointments(ReportWrapper report, ServletOutputStream outputStream) {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Отчет по записям");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        int rows = 0;
        Row row = sheet.createRow(rows);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        List<Appointment> appointmentList = prepareDataForReport(report);
        Cell headerCell = row.createCell(0);
        headerCell.setCellValue("Номер записи");
        headerCell.setCellStyle(headerStyle);

        headerCell = row.createCell(1);
        headerCell.setCellValue("Дата приема");
        headerCell.setCellStyle(headerStyle);

        headerCell = row.createCell(2);
        headerCell.setCellValue("Вид услуги");
        headerCell.setCellStyle(headerStyle);

        headerCell = row.createCell(3);
        headerCell.setCellValue("ФИО пациента");
        headerCell.setCellStyle(headerStyle);

        headerCell = row.createCell(4);
        headerCell.setCellValue("Дата рождения пациента");
        headerCell.setCellStyle(headerStyle);

        headerCell = row.createCell(5);
        headerCell.setCellValue("ФИО врача");
        headerCell.setCellStyle(headerStyle);

        for (Appointment appointment : appointmentList) {
            row = sheet.createRow(++rows);
            Cell cell = row.createCell(0);
            cell.setCellValue(appointment.getId());
            cell = row.createCell(1);
            cell.setCellValue(convertDate(appointment.getDate()));
            cell = row.createCell(2);
            cell.setCellValue(appointment.getTypeOfMedicalCare().getLabel());
            cell = row.createCell(3);
            Bio bio = appointment.getPatient().getBio();
            cell.setCellValue(bio.getSurname() + " " + bio.getName() + " " + bio.getMiddlename());
            cell = row.createCell(4);
            cell.setCellValue(convertDateWithTime(bio.getBirthdate()));
            cell = row.createCell(5);
            bio = appointment.getDoctor().getUser().getBio();
            cell.setCellValue(bio.getSurname() + " " + bio.getName() + " " + bio.getMiddlename());
        }

        row = sheet.createRow(rows++);
        Cell cell = row.createCell(0);
        cell.setCellValue("Всего записей");
        cell = row.createCell(1);
        cell.setCellValue(appointmentList.size());

        workbook.write(outputStream);
        outputStream.close();
    }

    private List<Appointment> prepareDataForReport(ReportWrapper report) {
        List<Appointment> appointments = appointmentService.getAll();
        if (!StringUtils.isEmpty(report.getDoctor())) {
            Long docId = convertToId(report.getDoctor());
            appointments.removeIf(e -> !e.getDoctor().getId().equals(docId));
        }
        if (!StringUtils.isEmpty(report.getPatient())){
            Long patId = convertToId(report.getPatient());
            appointments.removeIf(e -> !e.getDoctor().getId().equals(patId));
        }
        if (!StringUtils.isEmpty(report.getTypeOfMedicalCare())) {
            Long typeOfMedCareId = convertToId(report.getTypeOfMedicalCare());
            appointments.removeIf(e -> !e.getTypeOfMedicalCare().getId().equals(typeOfMedCareId));
        }
        if (report.getDateFrom() != null) {
            appointments.removeIf( e -> e.getDate().before(report.getDateFrom()));
        }
        if (report.getDateTo() != null) {
            appointments.removeIf(e -> e.getDate().after(report.getDateTo()));
        }
        return appointments;
    }

    private String convertDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return date != null ? simpleDateFormat.format(date) : null;
    }

    private String convertDateWithTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return date != null ? simpleDateFormat.format(date) : null;
    }

    private Long convertToId(String str) {
        int index = str.indexOf(",");
        if (index == -1) {
            return Long.valueOf(str);
        } else {
            return Long.valueOf(str.substring(0, index));
        }
    }
}
