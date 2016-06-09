/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ias.suraencuestas.solucionescgr.masters;

import co.com.ias.suraencuestas.solucionescgr.beans.BeanEmployee;
import co.com.ias.suraencuestas.solucionescgr.beans.BeanTags;
import co.com.ias.suraencuestas.solucionescgr.dao.DAOEmployee;
import co.com.ias.suraencuestas.solucionescgr.utils.Conexion;
import co.com.ias.suraencuestas.solucionescgr.utils.Constants;
import co.com.ias.suraencuestas.solucionescgr.utils.Message;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author user-
 */
public class MasterEmployee {

    Conexion conexion = new Conexion();
    DAOEmployee dao = new DAOEmployee();
    Constants constants = new Constants();

    public HSSFWorkbook getExcelEmployee(String idCompany) {

        try {
            String[] tagsCompany = conexion.getTagsFormEmployee(idCompany);
            String nameCompany = conexion.getNameCompany(idCompany);
            String[] fieldsForm = {"Tipo de documento", "Número de documento", "Nombre(s)", "Apellidos", "Correo electrónico"};
            String[] fields = new String[fieldsForm.length + tagsCompany.length];
            System.arraycopy(fieldsForm, 0, fields, 0, fieldsForm.length);
            System.arraycopy(tagsCompany, 0, fields, fieldsForm.length, tagsCompany.length);
            String[] typeDocuemnts = conexion.getTypeDocuments();

            int init = 2;
            int limit = 5000;

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Employees");
            HSSFFont font = wb.createFont();

            sheet.addMergedRegion(createCellMerge(0, 0, 0, fields.length - 1));
            createTitles(font, sheet, nameCompany);

            addFields(sheet, fields, 1, 0);

            sheet.addValidationData(createListSeleted(wb, "typeDocuemnts", typeDocuemnts, getRegion(init, limit, 0, 0)));
            createSheetHidden(wb, "idcompany", new String[]{idCompany}, getRegion(0, 0, 0, 0));

            wb.setActiveSheet(0);
            wb.setSheetHidden(1, 2);
            wb.setSheetHidden(2, 2);

            wb.close();
            conexion.closeConnectionMongo();
            return wb;

        } catch (Exception ex) {
            conexion.closeConnectionMongo();
            return null;
        }
    }

    public String getValidIdCompany(String idCompanyParameter) throws Exception {
        if (idCompanyParameter == null) {
            return "No se encontro el ID";
        } else {
            return null;
        }
    }
    
    public String getIdCompany(HSSFWorkbook wb, String idCompanyParameter) throws Exception {
        if (idCompanyParameter == null) {
            return "No se encontro el ID";
        } else {
            HSSFSheet sheet = wb.getSheet("idcompany");
            try {
                String idC = sheet.getRow(0).getCell(0).getStringCellValue();
                if (!idC.equals(idCompanyParameter)) {
                    return "El archivo que intenta cargar, no pertenece a esta empresa";
                } else {
                    return null;
                }
            } catch (Exception e) {
                return "Este archivo no es válido. Por favor descarga el archivo y vuelva a ingresar los datos";
            }
        }
    }

    public HSSFWorkbook readExcelEmployee(HSSFWorkbook workbook, String idCompany) throws IOException, Exception {
        Message message = new Message();
        String[] tagsCompany = conexion.getTagsFormEmployee(idCompany);

        HSSFSheet sheet = workbook.getSheet("Employees");

        Iterator<Row> rowIterator = sheet.iterator();
        Row row;

        List<BeanEmployee> listEmployeeValid = new ArrayList<BeanEmployee>();
        List<BeanEmployee> listEmployeeInvalid = new ArrayList<BeanEmployee>();
        int NumMaximumCols = 0;

        List<String> tagsForm = new ArrayList<String>();

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            if (row.getRowNum() > 1) {
                BeanEmployee beanEmployee = new BeanEmployee();
                List<BeanTags> tags = new ArrayList<BeanTags>();
                int cont = 1;
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell celda;
                while (cellIterator.hasNext()) {
                    celda = cellIterator.next();
                    if (celda.getColumnIndex() + 1 != cont) {
                        while (celda.getColumnIndex() + 1 > cont) {
                            beanEmployee.setComment(beanEmployee.getComment() + "El campo " + message.getMessageEmployee(cont) + " esta vacío. ");
                            beanEmployee.setValue(cont, "");
                            cont++;
                        }
                    }
                    String value = getValueCell(celda);
                    if (cont > 5) {
                        if (!validFieldNull(value) && (cont - 6) != -1 && (cont - 6) < tagsForm.size()) {
                            tags.add(new BeanTags(value, tagsForm.get(cont - 6)));
                        }
                    } else {
                        if (validFieldNull(value)) {
                            beanEmployee.setComment(beanEmployee.getComment() + "El campo " + message.getMessageEmployee(cont) + " esta vacío. ");
                            beanEmployee.setValue(cont, "");
                        } else {
                            if (celda.getColumnIndex() + 1 == 5) {
                                if (ValidatorEmail(value)) {
                                    beanEmployee.setComment(beanEmployee.getComment() + "El " + message.getMessageEmployee(cont) + " no es válido. ");
                                }
                            }
                            beanEmployee.setValue(cont, value);
                        }
                    }
                    cont++;
                }
                if (tags.size() == tagsCompany.length) {
                    beanEmployee.setTags(tags);
                } else {
                    beanEmployee.setComment(beanEmployee.getComment() + "Faltan las etiquetas para ser diligenciadas.");
                }
                if(beanEmployee.validAddEmployee()){
                    if (validFieldNull(beanEmployee.getComment())) {
                        listEmployeeValid.add(beanEmployee);
                    } else {
                        listEmployeeInvalid.add(beanEmployee);
                    }
                    if (cont > NumMaximumCols) {
                        NumMaximumCols = cont;
                    }
                }
            } else if (row.getRowNum() == 1) {
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell celda;
                while (cellIterator.hasNext()) {
                    celda = cellIterator.next();
                    String value = getValueCell(celda);
                    if (celda.getColumnIndex() + 1 > 5) {
                        if (!validFieldNull(value)) {
                            tagsForm.add(value);
                        }
                    }
                }
            }
        }
        return getExcelErrorsAndSuccess(listEmployeeInvalid, listEmployeeValid, NumMaximumCols, idCompany);
    }

    private boolean validFieldNull(String value) {
        if (value == null) {
            return true;
        }
        return value.replaceAll(" ", "").isEmpty();
    }

    private boolean ValidatorEmail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    public HSSFWorkbook getExcelErrorsAndSuccess(List<BeanEmployee> listEmployeeInvalid, List<BeanEmployee> listEmployeeValid, int NumMaximumCols, String idCompany) throws Exception {

        String nameCompany = conexion.getNameCompany(idCompany);
        String[] tagsCompany = conexion.getTagsFormEmployee(idCompany);
        String[] fieldsForm = {"Tipo de documento", "Número de documento", "Nombre(s)", "Apellidos", "Correo electrónico"};
        String[] fields = new String[fieldsForm.length + tagsCompany.length];
        System.arraycopy(fieldsForm, 0, fields, 0, fieldsForm.length);
        System.arraycopy(tagsCompany, 0, fields, fieldsForm.length, tagsCompany.length);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet errors = wb.createSheet("Employees_errors");
        HSSFFont font = wb.createFont();

        errors.addMergedRegion(createCellMerge(0, 0, 0, 10));
        errors.addMergedRegion(createCellMerge(0, 0, 11, 13));
        createTitles(font, errors, nameCompany);

        addFields(errors, fields, 1, 0);

        wb.setActiveSheet(0);
        
        List<BeanEmployee> listEmployees = dao.saveCompanies(listEmployeeValid, idCompany);
        List<BeanEmployee> lei = new ArrayList<BeanEmployee>();

        for (BeanEmployee beanEmployee : listEmployeeInvalid) {
            lei.add(beanEmployee);
        }
        for (BeanEmployee beanEmployee : listEmployees) {
            lei.add(beanEmployee);
        }
        addValueFields(errors, lei, true, NumMaximumCols, font, fields.length);
        conexion.closeConnectionMongo();
        return wb;
    }

    private void addValueFields(HSSFSheet sheet, List<BeanEmployee> listEmployees, boolean error, int NumMaximumCols, HSSFFont font, int NumFileds) {
        int cont = 2;
        for (BeanEmployee beanEmployee : listEmployees) {
            HSSFRow row = sheet.createRow(cont);
            int cellNum = 0;
            List<BeanTags> tags = beanEmployee.getTags();
            for (int i = 0; i < NumFileds - tags.size(); i++, cellNum++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(beanEmployee.getValue(i + 1));
                sheet.setColumnWidth(i, 8000);
            }
            int k = 0;
            for (int i = cellNum; i < NumFileds; i++, k++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(tags.get(k).getValue());
                sheet.setColumnWidth(i, 8000);
            }
            HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
            style.setWrapText(true);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setBorderBottom((short) 1);
            if (error) {
                style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            } else {
                font.setColor(HSSFColor.WHITE.index);
                style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                style.setFont(font);
            }
            HSSFCell cell = row.createCell(NumMaximumCols - 1);
            cell.setCellValue(beanEmployee.getComment());
            cell.setCellStyle(style);
            sheet.setColumnWidth(NumMaximumCols - 1, 10000);
            cont++;
        }
    }

    private String getValueCell(Cell celda) {
        switch (celda.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(celda)) {
                    return "" + celda.getDateCellValue();
                } else {
                    return "" + new DecimalFormat("0").format(celda.getNumericCellValue());
                }
            case Cell.CELL_TYPE_STRING:
                return "" + celda.getStringCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return "" + celda.getBooleanCellValue();
            default:
                return "";
        }
    }

    private void addFields(HSSFSheet sheet, String[] fields, int firstRow, int firstCol) {
        HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        HSSFRow row = sheet.createRow(firstRow);
        row.setHeightInPoints(35);

        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        for (int i = 0; i < fields.length; i++) {
            HSSFCell cell = row.createCell(i + firstCol);
            cell.setCellValue(fields[i]);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 8000);
        }
    }

    private CellRangeAddress createCellMerge(int firstRow, int LastRow, int firstCol, int LastCol) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, LastRow, firstCol, LastCol);
        return cellRangeAddress;
    }

    private void createTitles(HSSFFont font, HSSFSheet sheet, String nameCompany) {
        HSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(35);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Información del empleado - Empresa: " + nameCompany);

        HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        font.setColor(HSSFColor.WHITE.index);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFont(font);
        style.setBorderLeft((short) 1);

        cell.setCellStyle(style);
    }

    private HSSFDataValidation createListSeleted(HSSFWorkbook wb, String nameSheet, String[] list, CellRangeAddressList regions) {
        HSSFSheet hiddenActivity = wb.createSheet(nameSheet);
        hiddenActivity.protectSheet(constants.getPASS_EXCEL());
        CellStyle unlockedCellStyle = wb.createCellStyle();
        unlockedCellStyle.setLocked(true);
        for (int i = 0, length = list.length; i < length; i++) {
            String name = list[i];
            HSSFRow row = hiddenActivity.createRow(i);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(name);
            cell.setCellStyle(unlockedCellStyle);
        }
        Name namedCell = wb.createName();
        namedCell.setNameName(nameSheet);
        namedCell.setRefersToFormula(nameSheet + "!$A$1:$A$" + list.length);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(nameSheet);
        HSSFDataValidation validation = new HSSFDataValidation(regions, constraint);
        return validation;
    }
    
    private void createSheetHidden(HSSFWorkbook wb, String nameSheet, String[] list, CellRangeAddressList regions){
        HSSFSheet hiddenActivity = wb.createSheet(nameSheet);
        hiddenActivity.protectSheet(constants.getPASS_EXCEL());
        CellStyle unlockedCellStyle = wb.createCellStyle();
        unlockedCellStyle.setLocked(true);
        for (int i = 0, length = list.length; i < length; i++) {
            String name = list[i];
            HSSFRow row = hiddenActivity.createRow(i);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(name);
            cell.setCellStyle(unlockedCellStyle);
        }
        Name namedCell = wb.createName();
        namedCell.setNameName(nameSheet);
        namedCell.setRefersToFormula(nameSheet + "!$A$1:$A$" + list.length);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(nameSheet);
        HSSFDataValidation validation = new HSSFDataValidation(regions, constraint);
    }

    private CellRangeAddressList getRegion(int firstRow, int LastRow, int firstCol, int LastCol) {
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, LastRow, firstCol, LastCol);
        return regions;
    }

    public HSSFColor setColor(HSSFWorkbook workbook, byte r, byte g, byte b) {
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor hssfColor = null;
        try {
            hssfColor = palette.findColor(r, g, b);
            if (hssfColor == null) {
                palette.setColorAtIndex(HSSFColor.LAVENDER.index, r, g, b);
                hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hssfColor;
    }

}
