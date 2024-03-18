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
import com.app.sygen.entities.Ue;
import com.app.sygen.repositories.FiliereRepository;
// import com.app.sygen.repositories.UeRepository;

@Service
public class UeService {
    @Autowired
    FiliereRepository filiereRepository;

    public Ue createUeXLSX(Row row, Map<String, Integer> columnIndexMap) {
        String code = getColumnValue(row, columnIndexMap, "code");
        String intitule = getColumnValue(row, columnIndexMap, "intitule");
        Integer credit = Integer.parseInt(getColumnValue(row, columnIndexMap, "credit"));
        String codeF = getColumnValue(row, columnIndexMap, "codeFil");
        Filiere filiere = filiereRepository.findFirstByCode(codeF);

        Ue ue = new Ue();
        ue.setCode(code);
        ue.setCredit(credit);
        ue.setFiliere(filiere);
        ue.setIntitule(intitule);
        return ue;

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
