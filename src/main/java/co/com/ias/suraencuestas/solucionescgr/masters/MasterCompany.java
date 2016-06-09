/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ias.suraencuestas.solucionescgr.masters;

import co.com.ias.suraencuestas.solucionescgr.beans.BeanCompany;
import co.com.ias.suraencuestas.solucionescgr.dao.DAOCompany;
import co.com.ias.suraencuestas.solucionescgr.utils.Conexion;
import co.com.ias.suraencuestas.solucionescgr.utils.Constants;
import co.com.ias.suraencuestas.solucionescgr.utils.Message;
import java.io.FileNotFoundException;
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
public class MasterCompany {

    Conexion conexion = new Conexion();
    DAOCompany dao = new DAOCompany();
    Constants constants = new Constants();

    public HSSFWorkbook getExcelCompany() {

        String[] fields = {"NIT", "Razón Social", "Abreviatura", "Dirección de la sede principal", "Teléfono", "Correo electrónico", "Número de empleados", "Actividad Econónica", "ARL", "País", "Departamento", "Ciudad", "Nombre del contacto", "Teléfono del contacto", "Correo electrónico del contacto", "Etiquetas"};
        String[] countries = conexion.getCountries();
        String[] departments = conexion.getDepartments();
        String[] cities = conexion.getCities();
        String[] numEmployees = conexion.getNumEmployees();
        String[] ActivityEconomic = conexion.getActivityEconomic();
        String[] arls = conexion.getARLs();

        int init = 2;
        int limit = 300;

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Companies");
        HSSFFont font = wb.createFont();

        sheet.addMergedRegion(createCellMerge(0, 0, 0, 11));
        sheet.addMergedRegion(createCellMerge(0, 0, 12, 15));
        createTitles(font, sheet);

        addFields(sheet, fields, 1, 0);

        sheet.addValidationData(createListSeleted(wb, "numEmployees", numEmployees, getRegion(init, limit, 6, 6)));
        sheet.addValidationData(createListSeleted(wb, "Activityeconomic", ActivityEconomic, getRegion(init, limit, 7, 7)));
        sheet.addValidationData(createListSeleted(wb, "alrs", arls, getRegion(init, limit, 8, 8)));
        sheet.addValidationData(createListSeleted(wb, "country", countries, getRegion(init, limit, 9, 9)));
        sheet.addValidationData(createListSeleted(wb, "department", departments, getRegion(init, limit, 10, 10)));
        sheet.addValidationData(createListSeleted(wb, "city", cities, getRegion(init, limit, 11, 11)));

        wb.setActiveSheet(0);
        wb.setSheetHidden(1, 2);
        wb.setSheetHidden(2, 2);
        wb.setSheetHidden(3, 2);
        wb.setSheetHidden(4, 2);
        wb.setSheetHidden(5, 2);
        wb.setSheetHidden(6, 2);

        try {
            wb.close();
        } catch (IOException ex) {
        }
        conexion.closeConnectionMongo();
        return wb;
    }

    public HSSFWorkbook readExcelCompany(HSSFWorkbook workbook) throws FileNotFoundException, IOException {
        Message message = new Message();

        HSSFSheet sheet = workbook.getSheet("Companies");

        Iterator<Row> rowIterator = sheet.iterator();
        Row row;

        List<BeanCompany> listCompanyValid = new ArrayList<BeanCompany>();
        List<BeanCompany> listCompanyInvalid = new ArrayList<BeanCompany>();
        int NumMaximumCols = 0;

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            if (row.getRowNum() > 1) {
                BeanCompany beanCompany = new BeanCompany();
                List<String> tags = new ArrayList<String>();
                int cont = 1;
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell celda;
                while (cellIterator.hasNext()) {
                    celda = cellIterator.next();
                    if (celda.getColumnIndex() + 1 != cont) {
                        while (celda.getColumnIndex() + 1 > cont) {
                            beanCompany.setComment(beanCompany.getComment() + "El campo " + message.getMessageCompany(cont) + " esta vacío. ");
                            beanCompany.setValue(cont, "");
                            cont++;
                        }
                    }
                    String value = getValueCell(celda);
                    if (cont > 15) {
                        if (!validFieldNull(value)) {
                            tags.add(value);
                        }
                    } else {
                        if (validFieldNull(value)) {
                            beanCompany.setComment(beanCompany.getComment() + "El campo " + message.getMessageCompany(cont) + " esta vacío. ");
                            beanCompany.setValue(cont, "");
                        } else {
                            if (celda.getColumnIndex() + 1 == 6) {
                                if (ValidatorEmail(value)) {
                                    beanCompany.setComment(beanCompany.getComment() + "El correo electrónico de la empresa no es válido. ");
                                }
                            } else if (celda.getColumnIndex() + 1 == 15) {
                                if (ValidatorEmail(value)) {
                                    beanCompany.setComment(beanCompany.getComment() + "El correo electrónico del contacto no es válido. ");
                                }
                            }
                            beanCompany.setValue(cont, value);
                        }
                    }
                    cont++;
                }
                beanCompany.setTags(tags);
                if(beanCompany.validAddCompany()){
                    if (validFieldNull(beanCompany.getComment())) {
                        listCompanyValid.add(beanCompany);
                    } else {
                        listCompanyInvalid.add(beanCompany);
                    }
                    if (cont > NumMaximumCols) {
                        NumMaximumCols = cont;
                    }
                }
            }
        }
        return getExcelErrorsAndSuccess(listCompanyInvalid, listCompanyValid, NumMaximumCols);
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

    private void createTitles(HSSFFont font, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(35);

        HSSFCell cell = row.createCell(0);
        HSSFCell cell2 = row.createCell(12);
        cell.setCellValue("Información de la empresa");
        cell2.setCellValue("Información del contacto de la empresa");

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
        cell2.setCellStyle(style);
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

    private boolean validFieldNull(String value) {
        if (value == null) {
            return true;
        }
        return value.replaceAll(" ", "").isEmpty();
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

    private boolean ValidatorEmail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    public HSSFWorkbook getExcelErrorsAndSuccess(List<BeanCompany> listCompaniesInvalid, List<BeanCompany> listCompaniesValid, int NumMaximumCols) {

        String[] fields = {"NIT", "Razón Social", "Abreviatura", "Dirección de la sede principal", "Teléfono", "Correo electrónico", "Número de empleados", "Actividad Econónica", "ARL", "País", "Departamento", "Ciudad", "Nombre del contacto", "Teléfono del contacto", "Correo electrónico del contacto", "Etiquetas"};

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet errors = wb.createSheet("Companies_errors");
        HSSFFont font = wb.createFont();

        errors.addMergedRegion(createCellMerge(0, 0, 0, 11));
        errors.addMergedRegion(createCellMerge(0, 0, 12, 15));
        createTitles(font, errors);

        addFields(errors, fields, 1, 0);

        wb.setActiveSheet(0);
        
        List<BeanCompany> listCompanies = dao.saveCompanies(listCompaniesValid);
        List<BeanCompany> lci = new ArrayList<BeanCompany>();
        
        for (BeanCompany beanCompany : listCompaniesInvalid) {
            lci.add(beanCompany);
        }
        for (BeanCompany beanCompany : listCompanies) {
            lci.add(beanCompany);
        }
        addValueFields(errors, lci, true, NumMaximumCols, font);
        conexion.closeConnectionMongo();
        return wb;
    }

    private void addValueFields(HSSFSheet sheet, List<BeanCompany> listCompanies, boolean error, int NumMaximumCols, HSSFFont font) {
        int cont = 2;
        for (BeanCompany beanCompany : listCompanies) {
            HSSFRow row = sheet.createRow(cont);
            int cellNum = 0;
            for (int i = 0; i < beanCompany.getNumFields(); i++, cellNum++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(beanCompany.getValue(i + 1));
                sheet.setColumnWidth(i, 8000);
            }
            List<String> tags = beanCompany.getTags();
            for (int i = cellNum - 1; i < beanCompany.getNumFields() + tags.size() - 1; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(tags.get(i - beanCompany.getNumFields() + 1));
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
            cell.setCellValue(beanCompany.getComment());
            cell.setCellStyle(style);
            sheet.setColumnWidth(NumMaximumCols - 1, 10000);
            cont++;
        }
    }

}
