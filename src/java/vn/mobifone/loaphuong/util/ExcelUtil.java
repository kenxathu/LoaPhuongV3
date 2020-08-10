package vn.mobifone.loaphuong.util;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package mobifone.marketplace.lib.util;
//
//import java.io.InputStream;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//import mobifone.marketplace.lib.SystemLogger;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.primefaces.model.UploadedFile;
//
///**
// *
// * @author ChienDX
// */
////Util đọc excel
//public class ExcelUtil {
//
//    //Kiểm tra có phải row trống không
//    private static boolean isRowEmpty(Row row) {
//        for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
//            Cell cell = row.getCell(c);
//            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    //Đọc file
//    public static List read(UploadedFile file) throws Exception {
//        return read(file.getInputstream(), file.getFileName());
//    }
//
//    //Đọc file
//    public static List read(InputStream is, String fileName) throws Exception {
//        if (fileName.lastIndexOf(".xlsx") != -1) {
//            return readXlsx(is);
//        }
//
//        List listReturn = new ArrayList();
//        DecimalFormat df = new DecimalFormat("#.######");
//
//        try {
//            HSSFWorkbook workbook = new HSSFWorkbook(is);
//            HSSFSheet sheet = workbook.getSheetAt(0);
//            int colCount = sheet.getRow(0).getLastCellNum();
//
//            for (Row row : sheet) {
//                if (isRowEmpty(row)) {
//                    continue;
//                }
//
//                List<ExcelCellEtt> listCellRow = new ArrayList<ExcelCellEtt>();
//
//                for (int cn = 0; cn < colCount; cn++) {
//                    Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
//                    
//
//                    ExcelCellEtt tmpCell = new ExcelCellEtt();
//
//                    if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
//                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//                            tmpCell.setStringValue(cell.getStringCellValue().trim());
//
//                        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            tmpCell.setNumberValue(cell.getNumericCellValue());
//
//                            if (cell.getNumericCellValue() > 0 && cell.getDateCellValue() != null) {
//                                tmpCell.setDateValue(cell.getDateCellValue());
//                            }
//                        }
//
//                        if (tmpCell.getStringValue() == null) {
//                            tmpCell.setStringValue(String.valueOf(df.format(cell.getNumericCellValue())));
//                        }
//                    }
//
//                    listCellRow.add(tmpCell);
//                }
//
//                listReturn.add(listCellRow);
//            }
//
//        } catch (Exception e) {
//            SystemLogger.getLogger().error(e,e);
//            throw e;
//
//        } finally {
//            is.close();
//        }
//
//        return listReturn;
//    }
//
//    //Đọc file xlsx
//    public static List readXlsx(InputStream is) throws Exception {
//        List listReturn = new ArrayList();
//        DecimalFormat df = new DecimalFormat("#.#######");
//
//        try {
//            XSSFWorkbook workbook = new XSSFWorkbook(is);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            int colCount = sheet.getRow(0).getLastCellNum();
//
//            for (Row row : sheet) {
//                List<ExcelCellEtt> listCellRow = new ArrayList<ExcelCellEtt>();
//
//                for (int cn = 0; cn < colCount; cn++) {
//                    Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
//
//                    ExcelCellEtt tmpCell = new ExcelCellEtt();
//
//                    if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
//                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//                            tmpCell.setStringValue(cell.getStringCellValue());
//
//                        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            tmpCell.setNumberValue(cell.getNumericCellValue());
//
//                            if (cell.getNumericCellValue() >0 && cell.getDateCellValue() != null) {
//                                tmpCell.setDateValue(cell.getDateCellValue());
//                            }
//                        }
//
//                        if (tmpCell.getStringValue() == null) {
//                            tmpCell.setStringValue(String.valueOf(df.format(cell.getNumericCellValue())));
//                        }
//                    }
//
//                    listCellRow.add(tmpCell);
//                }
//
//                listReturn.add(listCellRow);
//            }
//
//        } catch (Exception e) {
//            SystemLogger.getLogger().error(e);
//            throw e;
//
//        } finally {
//            is.close();
//        }
//
//        return listReturn;
//    }
//}
