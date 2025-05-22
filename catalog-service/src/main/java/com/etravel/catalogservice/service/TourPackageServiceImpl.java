package com.etravel.catalogservice.service;

import com.etravel.catalogservice.domain.contracts.ITourPackageRepository;
import com.etravel.catalogservice.domain.dto.TourPackageRequestDTO;
import com.etravel.catalogservice.domain.dto.TourPackageResponseDTO;
import com.etravel.catalogservice.domain.entity.TourPackage;
import com.etravel.catalogservice.domain.event.TourPackageEvent;
import com.etravel.catalogservice.domain.mapper.TourPackageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourPackageServiceImpl implements TourPackageService {

    private final ITourPackageRepository repo;
    private final TourPackageMapper mapper;

    public TourPackageServiceImpl(ITourPackageRepository repo, TourPackageMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public List<TourPackageResponseDTO> getAll() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TourPackageResponseDTO getById(Long id) {
        TourPackage pkg = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TourPackage not found: " + id));
        return mapper.toResponse(pkg);
    }

    @Override
    @Transactional
    public TourPackageResponseDTO create(TourPackageRequestDTO dto) {
        TourPackage pkg = mapper.toEntity(dto);
        return mapper.toResponse(repo.save(pkg));
    }

    @Override
    public TourPackageResponseDTO update(Long id, TourPackageRequestDTO dto) {
        TourPackage existing = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TourPackage not found: " + id));
        existing.setDestination(dto.getDestination());
        existing.setPrice(new BigDecimal(dto.getPrice()));
        existing.setStartDate(LocalDate.parse(dto.getStartDate()));
        existing.setEndDate(LocalDate.parse(dto.getEndDate()));
        existing.setImage1(dto.getImage1());
        existing.setImage2(dto.getImage2());
        existing.setImage3(dto.getImage3());
        return mapper.toResponse(repo.save(existing));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public void exportByIds(List<Long> ids, String format, HttpServletResponse response){
        List<TourPackageResponseDTO> packages = ids.stream()
                .map(this::getById)
                .collect(Collectors.toList());

        try {
            switch (format.toLowerCase()) {
                case "csv" -> exportCsv(packages, response);
                case "json" -> exportJson(packages, response);
                case "xml" -> exportXml(packages, response);
                case "doc" -> exportDoc(packages, response);
                default -> throw new IllegalArgumentException("Unsupported format: " + format);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data", e);
        }
    }

    @Override
    public void export(String format, HttpServletResponse response) {
        List<TourPackageResponseDTO> packages = getAll();

        try {
            switch (format.toLowerCase()) {
                case "csv" -> exportCsv(packages, response);
                case "json" -> exportJson(packages, response);
                case "xml" -> exportXml(packages, response);
                case "doc" -> exportDoc(packages, response);
                default -> throw new IllegalArgumentException("Unsupported format: " + format);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data", e);
        }
    }

    private void exportDoc(List<TourPackageResponseDTO> packages, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=tour_packages.docx");

        XWPFDocument doc = new XWPFDocument();

        // Create table with header row
        XWPFTable table = doc.createTable(packages.size() + 1, 5);

        // Header
        table.getRow(0).getCell(0).setText("ID");
        table.getRow(0).getCell(1).setText("Destination");
        table.getRow(0).getCell(2).setText("Start Date");
        table.getRow(0).getCell(3).setText("End Date");
        table.getRow(0).getCell(4).setText("Price");

        // Rows
        for (int i = 0; i < packages.size(); i++) {
            TourPackageResponseDTO pkg = packages.get(i);
            XWPFTableRow row = table.getRow(i + 1);
            row.getCell(0).setText(String.valueOf(pkg.getId()));
            row.getCell(1).setText(pkg.getDestination());
            row.getCell(2).setText(pkg.getStartDate());
            row.getCell(3).setText(pkg.getEndDate());
            row.getCell(4).setText(pkg.getPrice());
        }

        doc.write(response.getOutputStream());
        doc.close();
    }

    private void exportCsv(List<TourPackageResponseDTO> packages, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=tour-packages.csv");

        PrintWriter writer = response.getWriter();
        writer.println("ID,Destination,Start Date,End Date,Price");
        for (TourPackageResponseDTO p : packages) {
            writer.printf("%s,%s,%s,%s,%s\n",
                    p.getId(), p.getDestination(),
                    p.getStartDate(), p.getEndDate(), p.getPrice());
        }
    }

    private void exportJson(List<TourPackageResponseDTO> packages, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=tour-packages.json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), packages);
    }

    private void exportXml(List<TourPackageResponseDTO> packages, HttpServletResponse response) throws IOException {
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=tour-packages.xml");

        PrintWriter writer = response.getWriter();
        writer.println("<tourPackages>");
        for (TourPackageResponseDTO p : packages) {
            writer.println("  <tourPackage>");
            writer.printf("    <id>%s</id>%n", p.getId());
            writer.printf("    <destination>%s</destination>%n", escapeXml(p.getDestination()));
            writer.printf("    <startDate>%s</startDate>%n", p.getStartDate());
            writer.printf("    <endDate>%s</endDate>%n", p.getEndDate());
            writer.printf("    <price>%s</price>%n", p.getPrice());
            writer.println("  </tourPackage>");
        }
        writer.println("</tourPackages>");
    }

    private String escapeXml(String input) {
        return input == null ? "" : input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

}