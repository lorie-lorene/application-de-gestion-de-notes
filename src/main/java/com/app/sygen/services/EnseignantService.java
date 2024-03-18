package com.app.sygen.services;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sygen.entities.Enseignant;
import com.app.sygen.entities.Users;
import com.app.sygen.repositories.UserRepository;

@Service
public class EnseignantService {

    @Autowired
    private UserRepository userRepository;

    public Enseignant createEnseignantXLSX(Row row, Map<String, Integer> columnIndexMap) {
        Boolean actif = Boolean.parseBoolean(getColumnValue(row, columnIndexMap, "actif"));
        String login = getColumnValue(row, columnIndexMap, "login");
        Users user = userRepository.findByLogin(login);
        Enseignant enseignant = new Enseignant();
        enseignant.setActif(actif);
        enseignant.setUser(user);
        return enseignant;
    }

    public String getColumnValue(Row row, Map<String, Integer> columnIndexMap, String columnName) {
        Integer columnIndex = columnIndexMap.get(columnName);
        if (columnIndex != null) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                cell.setCellType(CellType.STRING); // Convertir la cellule en type chaîne de caractères
                // cell.setCellType(CellType.STRING);
                return cell.getStringCellValue();
            }
        }
        return null;
    }

    public Map<String, Integer> getColumnIndexMap(Row headerRow) {
        Map<String, Integer> columnIndexMap = new HashMap<>();
        Iterator<Cell> cellIterator = headerRow.cellIterator();
        int columnIndex = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            columnIndexMap.put(cell.getStringCellValue(), columnIndex);
            columnIndex++;
        }
        return columnIndexMap;
    }
}
