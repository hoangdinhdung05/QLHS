package org.example.util;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.example.entity.Student;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class để export dữ liệu ra PDF với format đẹp
 */
public class PdfExporter {

    /**
     * Export danh sách học sinh ra PDF với format đẹp
     */
    public static void exportStudentsToPdf(List<Student> students, String filePath) throws IOException {
        PdfWriter writer = new PdfWriter(new File(filePath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            // Load font hỗ trợ tiếng Việt - sử dụng font mặc định
            PdfFont font = PdfFontFactory.createFont("Helvetica", PdfEncodings.CP1252);
            PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold", PdfEncodings.CP1252);

            // Tiêu đề
            Paragraph title = new Paragraph("DANH SACH HOC SINH")
                    .setFont(boldFont)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(0, 51, 102))
                    .setBold()
                    .setMarginBottom(10);
            document.add(title);

            // Thông tin ngày xuất
            Paragraph dateInfo = new Paragraph("Ngay xuat: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginBottom(15)
                    .setItalic();
            document.add(dateInfo);

            // Tạo bảng với 10 cột
            float[] columnWidths = {30f, 60f, 120f, 70f, 50f, 70f, 100f, 100f, 70f, 60f};
            Table table = new Table(UnitValue.createPointArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Header
            String[] headers = {"STT", "Ma HS", "Ho ten", "Ngay sinh", "Gioi tinh",
                    "SDT", "Dia chi", "Ten PH", "SDT PH", "Trang thai"};

            for (String header : headers) {
                Cell headerCell = new Cell()
                        .add(new Paragraph(header).setFont(boldFont).setFontSize(9))
                        .setBackgroundColor(new DeviceRgb(65, 105, 225))
                        .setFontColor(ColorConstants.WHITE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(8)
                        .setBold();
                table.addHeaderCell(headerCell);
            }

            // Data rows
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            int stt = 1;
            boolean alternate = false;

            for (Student student : students) {
                // Background color cho hàng xen kẽ
                DeviceRgb bgColor = alternate ? new DeviceRgb(240, 248, 255) : new DeviceRgb(255, 255, 255);
                alternate = !alternate;

                // STT
                table.addCell(createDataCell(String.valueOf(stt++), font, bgColor, TextAlignment.CENTER));

                // Mã học sinh
                table.addCell(createDataCell(student.getStudentCode(), font, bgColor, TextAlignment.CENTER));

                // Họ tên
                table.addCell(createDataCell(student.getFullName(), font, bgColor, TextAlignment.LEFT));

                // Ngày sinh
                String dob = student.getDateOfBirth() != null ?
                        student.getDateOfBirth().format(dateFormatter) : "";
                table.addCell(createDataCell(dob, font, bgColor, TextAlignment.CENTER));

                // Giới tính
                table.addCell(createDataCell(student.getGender(), font, bgColor, TextAlignment.CENTER));

                // Số điện thoại
                String phone = student.getPhone() != null ? student.getPhone() : "";
                table.addCell(createDataCell(phone, font, bgColor, TextAlignment.CENTER));

                // Địa chỉ
                String address = student.getAddress() != null ? student.getAddress() : "";
                table.addCell(createDataCell(address, font, bgColor, TextAlignment.LEFT));

                // Tên phụ huynh
                String parentName = student.getParentName() != null ? student.getParentName() : "";
                table.addCell(createDataCell(parentName, font, bgColor, TextAlignment.LEFT));

                // SĐT phụ huynh
                table.addCell(createDataCell(student.getParentPhone(), font, bgColor, TextAlignment.CENTER));

                // Trạng thái
                String status = "ACTIVE".equals(student.getStatus()) ? "Dang hoc" : "Da nghi";
                DeviceRgb statusBgColor = "ACTIVE".equals(student.getStatus()) ?
                        new DeviceRgb(144, 238, 144) : new DeviceRgb(255, 182, 193);
                DeviceRgb statusTextColor = "ACTIVE".equals(student.getStatus()) ?
                        new DeviceRgb(0, 100, 0) : new DeviceRgb(139, 0, 0);

                Cell statusCell = new Cell()
                        .add(new Paragraph(status).setFont(boldFont).setFontSize(8))
                        .setBackgroundColor(statusBgColor)
                        .setFontColor(statusTextColor)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(5);
                table.addCell(statusCell);
            }

            document.add(table);

            // Footer với tổng số học sinh
            Paragraph footer = new Paragraph("Tong so: " + students.size() + " hoc sinh")
                    .setFont(boldFont)
                    .setFontSize(11)
                    .setMarginTop(15)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontColor(new DeviceRgb(0, 51, 102));
            document.add(footer);

        } catch (Exception e) {
            throw new IOException("Loi khi tao file PDF: " + e.getMessage(), e);
        } finally {
            document.close();
        }
    }

    /**
     * Helper method để tạo data cell với style nhất quán
     */
    private static Cell createDataCell(String content, PdfFont font, DeviceRgb bgColor, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(content).setFont(font).setFontSize(8))
                .setBackgroundColor(bgColor)
                .setTextAlignment(alignment)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(5);
    }
}
