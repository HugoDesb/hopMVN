package semantic;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelWriter {

    private static final String IF_LINE_TITLE = "IF";
    private static final String ELSE_LINE_TITLE = "ELSE";
    private ArrayList<Rule> rules;
    private Workbook workbook;
    /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
    private CreationHelper createHelper;

    // Create a Sheet
    private Sheet sheet;

    // Create a Font for styling the top sentence
    private CellStyle sentenceCellStyle;
    private CellStyle ifCellStyle;
    private CellStyle elseCellStyle;
    private CellStyle normalCellStyle;
    private CellStyle smallHeaderCellStyle;


    public ExcelWriter(ArrayList<Rule> rules) {
        this.rules = rules;

        // Create a Workbook
        this.workbook = new HSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        this.createHelper = workbook.getCreationHelper();
        // Create a Sheet
        this.sheet = workbook.createSheet("Rules");
        this.sheet.setDefaultRowHeight((short)400);

        initCellStyles();
    }

    private void initCellStyles() {
        // Create a Font for styling the top sentence
        Font sentenceFont = workbook.createFont();
        sentenceFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        sentenceFont.setFontHeightInPoints((short) 14);
        sentenceFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a Font for styling the 'if' header
        Font ifFont = workbook.createFont();
        ifFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        ifFont.setFontHeightInPoints((short) 14);
        ifFont.setColor(IndexedColors.RED.getIndex());

        // Create a Font for styling the 'else' header
        Font elseFont = workbook.createFont();
        elseFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        elseFont.setFontHeightInPoints((short) 14);
        elseFont.setColor(IndexedColors.BLUE.getIndex());

        // Create a Font for styling the regular text
        Font normalFont = workbook.createFont();
        normalFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        normalFont.setFontHeightInPoints((short) 12);
        normalFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a Font for styling the small headers
        Font smallHeaderFont = workbook.createFont();
        smallHeaderFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        smallHeaderFont.setFontHeightInPoints((short) 12);
        smallHeaderFont.setColor(IndexedColors.BLACK.getIndex());

        //Create cell styles
        sentenceCellStyle = workbook.createCellStyle();
        sentenceCellStyle.setFont(sentenceFont);
        ifCellStyle = workbook.createCellStyle();
        ifCellStyle.setFont(ifFont);
        elseCellStyle = workbook.createCellStyle();
        elseCellStyle.setFont(elseFont);
        normalCellStyle = workbook.createCellStyle();
        normalCellStyle.setFont(normalFont);
        smallHeaderCellStyle = workbook.createCellStyle();
        smallHeaderCellStyle.setFont(smallHeaderFont);
    }

    public void write(String output) {

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create Other rows and cells with employees data
        int rowNum = 0;
        for (Rule r : rules) {
            if(r.getConclusionsToStrings().size()!=0) {
                Row row = sheet.createRow(rowNum++);

                // Write Sentence
                //row.setRowStyle(sentenceCellStyle);
                Cell c = row.createCell(0);
                c.setCellValue(r.getSentence().toString());
                c.setCellStyle(sentenceCellStyle);


                // IF line
                row = sheet.createRow(rowNum++);
                writeIfOrElseLine(row, IF_LINE_TITLE);

                //Write all premises
                for (String p : r.getPremisesToStrings()) {
                    row = sheet.createRow(rowNum++);
                    writeRuleLine(row, rowNum, p);
                }

                // ELSE line
                row = sheet.createRow(rowNum++);
                writeIfOrElseLine(row, ELSE_LINE_TITLE);

                // Write all conclusions
                for (String concl : r.getConclusionsToStrings()) {
                    row = sheet.createRow(rowNum++);
                    writeRuleLine(row, rowNum, concl);
                }
            }
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < rowNum; i++) {
            //sheet.autoSize
        }

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(output);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void writeRuleLine(Row row, int rowNum, String rule){
        Cell c = row.createCell(1);
        c.setCellValue(rule);
        c.setCellStyle(normalCellStyle);

        CellRangeAddressList addressList = new CellRangeAddressList(rowNum-1, rowNum-1, 2, 5);
        DVConstraint dvConstraint = DVConstraint
                .createExplicitListConstraint(new String[] { "Yes", "No" });
        DataValidation dataValidation = new HSSFDataValidation(addressList,
                dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        sheet.addValidationData(dataValidation);
    }

    public void writeIfOrElseLine (Row row, String title){
        // IF line
        Cell c = row.createCell(0);
        c.setCellValue(title);
        if(title == IF_LINE_TITLE){
            c.setCellStyle(ifCellStyle);
        }else{
            c.setCellStyle(elseCellStyle);
        }

        c = row.createCell(2);
        c.setCellValue("Yes");
        c.setCellStyle(smallHeaderCellStyle);

        c = row.createCell(3);
        c.setCellValue("No");
        c.setCellStyle(smallHeaderCellStyle);

        c = row.createCell(4);
        c.setCellValue("Neutral");
        c.setCellStyle(smallHeaderCellStyle);

        c = row.createCell(5);
        c.setCellValue("mHealth");
        c.setCellStyle(smallHeaderCellStyle);
    }
}
