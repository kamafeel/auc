package com.auc.common.utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * excel 操作
 * @author zhangqi
 */

public class ExcelOperate {

  /**
   * 
   * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
   * 
   * @param file
   *          读取数据的源Excel
   * 
   * @param ignoreRows
   *          读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
   * 
   * @return 读出的Excel中数据的内容
   * @throws InvalidFormatException
   * 
   * @throws IOException
   * 
   */
  public static List<String[]> getData(MultipartFile file, int ignoreRows) throws IOException {
    List<String[]> result = new ArrayList<String[]>();
    Workbook wb = WorkbookFactory.create(file.getInputStream());
    if (wb instanceof XSSFWorkbook) {
      result = getXSSFResult(wb, ignoreRows);
    } else if (wb instanceof HSSFWorkbook) {
      result = getHSSFResult(wb, ignoreRows);
    }
    return result;
  }

  //Excel 2007 (.xlsx)文件操作
  private static List<String[]> getXSSFResult(Workbook wb, int ignoreRows) {
    List<String[]> result = new ArrayList<String[]>();
    XSSFCell cell = null;
    int rowSize = 0;
    XSSFSheet st = (XSSFSheet) wb.getSheetAt(0);
    //设置忽略行数
    for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
      XSSFRow row = st.getRow(rowIndex);
      if (row == null) {
        continue;
      }
      int tempRowSize = row.getLastCellNum();
      if (tempRowSize > rowSize) {
        rowSize = tempRowSize;
      }
      String[] values = new String[rowSize];
      Arrays.fill(values, "");
      boolean hasValue = false;
      for (short columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
        String value = "";
        cell = row.getCell(columnIndex);
        if (cell != null) {

          switch (cell.getCellType()) {
            case STRING:
              value = cell.getStringCellValue();
              break;
            case FORMULA:
            // 导入时如果为公式生成的数据则无值
              if (!cell.getStringCellValue().equals("")) {
                value = cell.getStringCellValue();
              } else {
                value = cell.getNumericCellValue() + "";
              }
              break;
            case NUMERIC:
              if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                value = date != null ? new SimpleDateFormat("yyyy/MM/dd").format(date) : "";
              } else {
                value = new DecimalFormat("0").format(cell.getNumericCellValue());
              }
              break;
            case BLANK:
              break;
            case ERROR:
              value = "";
              break;
            case BOOLEAN:
              value = cell.getBooleanCellValue() ? "Y" : "N";
              break;
            default:
              value = "";
          }
        }
        values[columnIndex] = rightTrim(value);
        hasValue = true;
      }
      if (hasValue) {
        result.add(values);
      }
    }
    return result;
  }
  //Excel 97(-2007)(.xls)文件操作
  @SuppressWarnings("deprecation")
  private static List<String[]> getHSSFResult(Workbook wb, int ignoreRows) {
    List<String[]> result = new ArrayList<String[]>();
    HSSFCell cell = null;
    int rowSize = 0;
    HSSFSheet st = (HSSFSheet) wb.getSheetAt(0);
    //设置忽略行数
    for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
      HSSFRow row = st.getRow(rowIndex);
      if (row == null) {
        continue;
      }
      int tempRowSize = row.getLastCellNum();
      if (tempRowSize > rowSize) {
        rowSize = tempRowSize;
      }
      String[] values = new String[rowSize];
      Arrays.fill(values, "");
      boolean hasValue = false;
      for (short columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
        String value = "";
        cell = row.getCell(columnIndex);
        if (cell != null) {
          
          switch (cell.getCellType()) {
            case STRING:
              value = cell.getStringCellValue();
              break;
            case NUMERIC:
              if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                value = date != null ? new SimpleDateFormat("yyyy/MM/dd").format(date) : "";
              } else {
                value = new DecimalFormat("0").format(cell.getNumericCellValue());
              }
              break;
            case FORMULA:
              // 导入时如果为公式生成的数据则无值
              if (!cell.getStringCellValue().equals("")) {
                value = cell.getStringCellValue();
              } else {
                value = cell.getNumericCellValue() + "";
              }
              break;
            case BLANK:
              break;
            case ERROR:
              value = "";
              break;
            case BOOLEAN:
              value = cell.getBooleanCellValue() ? "Y" : "N";
              break;
            default:
              value = "";
          }
        }
        values[columnIndex] = rightTrim(value);
        hasValue = true;
      }
      if (hasValue) {
        result.add(values);
      }
    }
    return result;
  }

  /**
   * 
   * 去掉字符串右边的空格
   * 
   * @param str
   *          要处理的字符串
   * 
   * @return 处理后的字符串
   * 
   */

  private static String rightTrim(String str) {
    if (str == null) {
      return "";

    }
    int length = str.length();
    for (int i = length - 1; i >= 0; i--) {
      if (str.charAt(i) != 0x20) {
        break;
      }
      length--;
    }
    return str.substring(0, length);
  }

}