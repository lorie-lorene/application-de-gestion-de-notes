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

import com.app.sygen.entities.Filiere;
import com.app.sygen.repositories.FiliereRepository;

@Service
public class FiliereService {
    @Autowired
    private FiliereRepository filiereRepository;

    public Filiere createFiliereXLSX(Row row, Map<String, Integer> columnIndexMap) {
        String code = getColumnValue(row, columnIndexMap, "code");
        String libelle = getColumnValue(row, columnIndexMap, "libelle");
        Filiere filiere = new Filiere();
        filiere.setCode(code);
        filiere.setLibelle(libelle);
        return filiere;

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
