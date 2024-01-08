package com.example.demo.service;
import com.example.demo.entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
@Service
public class ExcelUpdateService {

    public static void updateUsersInExcel(List<User> usersToUpdate, String filePath) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileIn)) {

            Sheet sheet = workbook.getSheet("Users");

            if (sheet == null) {
                // Handle the case where the sheet is not found
                throw new IllegalArgumentException("Sheet 'Users' not found in the Excel file.");
            }

            // Find and update the data rows
            for (User userToUpdate : usersToUpdate) {
                for (Row row : sheet) {
                    Cell idCell = row.getCell(0);
                    if (idCell != null && CellType.NUMERIC.equals(idCell.getCellType())) {
                        int userId = (int) idCell.getNumericCellValue();
                        if (userId == userToUpdate.getId()) {
                            // Update the existing row with new data
                            updateRowWithData(row, userToUpdate);
                            break;
                        }
                    }
                }
            }

            // Save the changes
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

    private static void updateRowWithData(Row row, User user) {
        // Update the data in the existing row
        row.getCell(1).setCellValue(user.getUsername());
        row.getCell(2).setCellValue(user.getEmail());
        row.getCell(3).setCellValue(user.getMobileNumber());
        // Add more cells to update as needed
    }
}

