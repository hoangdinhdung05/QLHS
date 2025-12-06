package org.example.util;

import org.example.config.DatabaseConfig;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class để backup database
 */
public class BackupUtil {

    /**
     * Backup database SQLite
     * @param targetDirectory Thư mục đích để lưu file backup
     * @return true nếu backup thành công
     */
    public static boolean backupDatabase(String targetDirectory) {
        try {
            String dbPath = DatabaseConfig.getDatabasePath();
            File sourceFile = new File(dbPath);
            
            if (!sourceFile.exists()) {
                JOptionPane.showMessageDialog(null, 
                    "File database không tồn tại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Tạo tên file backup với timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = dateFormat.format(new Date());
            String backupFileName = "daythem_backup_" + timestamp + ".db";
            
            File targetFile = new File(targetDirectory, backupFileName);
            
            // Copy file
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            JOptionPane.showMessageDialog(null, 
                "Backup thành công!\nFile: " + targetFile.getAbsolutePath(), 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi backup: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Restore database từ file backup
     * @param backupFilePath Đường dẫn đến file backup
     * @return true nếu restore thành công
     */
    public static boolean restoreDatabase(String backupFilePath) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Restore sẽ ghi đè database hiện tại. Bạn có chắc chắn?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return false;
        }

        try {
            String dbPath = DatabaseConfig.getDatabasePath();
            File sourceFile = new File(backupFilePath);
            File targetFile = new File(dbPath);
            
            if (!sourceFile.exists()) {
                JOptionPane.showMessageDialog(null, 
                    "File backup không tồn tại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Copy file backup vào vị trí database
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            JOptionPane.showMessageDialog(null, 
                "Restore thành công! Vui lòng khởi động lại ứng dụng.", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi restore: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Hiển thị dialog chọn thư mục để backup
     */
    public static void showBackupDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Chọn thư mục lưu backup");
        
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            backupDatabase(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Hiển thị dialog chọn file backup để restore
     */
    public static void showRestoreDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Chọn file backup để restore");
        
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            restoreDatabase(selectedFile.getAbsolutePath());
        }
    }
}
