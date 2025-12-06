package org.example.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entity.Student;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class để export dữ liệu ra Excel
 */
public class ExcelExporter {

    /**
     * Export danh sách học sinh ra Excel với format đẹp
     */
    public static void exportStudentsToExcel(List<Student> students, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách học sinh");

        // Tạo title style
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFont(titleFont);
        titleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);

        // Tạo title row
        Row titleRow = sheet.createRow(0);
        titleRow.setHeight((short) 600);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DANH SÁCH HỌC SINH");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 9));

        // Tạo header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Tạo data style
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Tạo center style
        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.cloneStyleFrom(dataStyle);
        centerStyle.setAlignment(HorizontalAlignment.CENTER);

        // Tạo status active style
        CellStyle activeStyle = workbook.createCellStyle();
        activeStyle.cloneStyleFrom(centerStyle);
        activeStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        activeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font activeFont = workbook.createFont();
        activeFont.setColor(IndexedColors.DARK_GREEN.getIndex());
        activeFont.setBold(true);
        activeStyle.setFont(activeFont);

        // Tạo status inactive style
        CellStyle inactiveStyle = workbook.createCellStyle();
        inactiveStyle.cloneStyleFrom(centerStyle);
        inactiveStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        inactiveStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font inactiveFont = workbook.createFont();
        inactiveFont.setColor(IndexedColors.DARK_RED.getIndex());
        inactiveFont.setBold(true);
        inactiveStyle.setFont(inactiveFont);

        // Tạo header row
        Row headerRow = sheet.createRow(1);
        headerRow.setHeight((short) 400);
        String[] headers = {"STT", "Mã học sinh", "Họ tên", "Ngày sinh", "Giới tính", 
                           "Số điện thoại", "Địa chỉ", "Tên phụ huynh", "SĐT phụ huynh", "Trạng thái"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Tạo data rows
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int rowNum = 2;
        int stt = 1;
        for (Student student : students) {
            Row row = sheet.createRow(rowNum++);
            row.setHeight((short) 350);
            
            // STT
            Cell sttCell = row.createCell(0);
            sttCell.setCellValue(stt++);
            sttCell.setCellStyle(centerStyle);
            
            // Mã học sinh
            Cell codeCell = row.createCell(1);
            codeCell.setCellValue(student.getStudentCode());
            codeCell.setCellStyle(centerStyle);
            
            // Họ tên
            Cell nameCell = row.createCell(2);
            nameCell.setCellValue(student.getFullName());
            nameCell.setCellStyle(dataStyle);
            
            // Ngày sinh
            Cell dobCell = row.createCell(3);
            dobCell.setCellValue(student.getDateOfBirth() != null ? 
                                student.getDateOfBirth().format(dateFormatter) : "");
            dobCell.setCellStyle(centerStyle);
            
            // Giới tính
            Cell genderCell = row.createCell(4);
            genderCell.setCellValue(student.getGender());
            genderCell.setCellStyle(centerStyle);
            
            // Số điện thoại
            Cell phoneCell = row.createCell(5);
            phoneCell.setCellValue(student.getPhone() != null ? student.getPhone() : "");
            phoneCell.setCellStyle(centerStyle);
            
            // Địa chỉ
            Cell addressCell = row.createCell(6);
            addressCell.setCellValue(student.getAddress() != null ? student.getAddress() : "");
            addressCell.setCellStyle(dataStyle);
            
            // Tên phụ huynh
            Cell parentNameCell = row.createCell(7);
            parentNameCell.setCellValue(student.getParentName() != null ? student.getParentName() : "");
            parentNameCell.setCellStyle(dataStyle);
            
            // SĐT phụ huynh
            Cell parentPhoneCell = row.createCell(8);
            parentPhoneCell.setCellValue(student.getParentPhone());
            parentPhoneCell.setCellStyle(centerStyle);
            
            // Trạng thái
            Cell statusCell = row.createCell(9);
            String statusText = "ACTIVE".equals(student.getStatus()) ? "Đang học" : "Đã nghỉ";
            statusCell.setCellValue(statusText);
            statusCell.setCellStyle("ACTIVE".equals(student.getStatus()) ? activeStyle : inactiveStyle);
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            // Add some padding
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, currentWidth + 1000);
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(new File(filePath))) {
            workbook.write(fileOut);
        }

        workbook.close();
    }

    /**
     * Export báo cáo thu nhập ra Excel
     */
    public static void exportIncomeReportToExcel(List<IncomeReport> reports, String filePath, 
                                                 int month, int year) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Báo cáo thu nhập tháng " + month + "/" + year);

        // Tạo title
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("BÁO CÁO THU NHẬP THÁNG " + month + "/" + year);
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);

        // Tạo header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Tạo header row
        Row headerRow = sheet.createRow(2);
        String[] headers = {"STT", "Mã HS", "Tên học sinh", "Lớp học", "Môn học", 
                           "Số buổi", "Học phí/buổi", "Tổng thu nhập"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Tạo data rows
        int rowNum = 3;
        double totalIncome = 0;
        for (IncomeReport report : reports) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(rowNum - 3);
            row.createCell(1).setCellValue(report.getStudentCode());
            row.createCell(2).setCellValue(report.getStudentName());
            row.createCell(3).setCellValue(report.getClassName());
            row.createCell(4).setCellValue(report.getSubject());
            row.createCell(5).setCellValue(report.getTotalSessions());
            row.createCell(6).setCellValue(report.getFeePerSession());
            row.createCell(7).setCellValue(report.getTotalIncome());
            
            totalIncome += report.getTotalIncome();
        }

        // Tạo total row
        Row totalRow = sheet.createRow(rowNum + 1);
        Cell totalLabelCell = totalRow.createCell(6);
        totalLabelCell.setCellValue("TỔNG CỘNG:");
        CellStyle totalStyle = workbook.createCellStyle();
        Font totalFont = workbook.createFont();
        totalFont.setBold(true);
        totalStyle.setFont(totalFont);
        totalLabelCell.setCellStyle(totalStyle);
        
        Cell totalValueCell = totalRow.createCell(7);
        totalValueCell.setCellValue(totalIncome);
        totalValueCell.setCellStyle(totalStyle);

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(new File(filePath))) {
            workbook.write(fileOut);
        }

        workbook.close();
    }

    /**
     * Inner class để chứa thông tin báo cáo
     */
    public static class IncomeReport {
        private String studentCode;
        private String studentName;
        private String className;
        private String subject;
        private int totalSessions;
        private double feePerSession;
        private double totalIncome;

        // Getters and Setters
        public String getStudentCode() {
            return studentCode;
        }

        public void setStudentCode(String studentCode) {
            this.studentCode = studentCode;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public int getTotalSessions() {
            return totalSessions;
        }

        public void setTotalSessions(int totalSessions) {
            this.totalSessions = totalSessions;
        }

        public double getFeePerSession() {
            return feePerSession;
        }

        public void setFeePerSession(double feePerSession) {
            this.feePerSession = feePerSession;
        }

        public double getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(double totalIncome) {
            this.totalIncome = totalIncome;
        }
    }
}
