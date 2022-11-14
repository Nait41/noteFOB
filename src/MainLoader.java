import data.InfoList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainLoader extends JFrame {
    Workbook workbook;
    String xlsxDirectoryPath;
    int numberFile;
    public MainLoader(String xlsxDirectoryPath, int numberFile) throws IOException, InvalidFormatException {
        this.xlsxDirectoryPath = xlsxDirectoryPath;
        this.numberFile = numberFile;
        workbook = new XSSFWorkbook();
    }

    public void setPhylum(InfoList infoList){
        workbook.createSheet("Phylum");
        for (int i = 0; i < infoList.phylumTable.size();i++){
            workbook.getSheet("Phylum").createRow(i).createCell(0).setCellValue(infoList.phylumTable.get(i).get(0));
            workbook.getSheet("Phylum").setColumnWidth(0, 10000);
            workbook.getSheet("Phylum").getRow(i).createCell(1).setCellValue(infoList.phylumTable.get(i).get(numberFile+1));
        }
    }

    public void setClass(InfoList infoList){
        workbook.createSheet("Class");
        for (int i = 0; i < infoList.classTable.size();i++){
            workbook.getSheet("Class").createRow(i).createCell(0).setCellValue(infoList.classTable.get(i).get(0));
            workbook.getSheet("Class").setColumnWidth(0, 10000);
            workbook.getSheet("Class").getRow(i).createCell(1).setCellValue(infoList.classTable.get(i).get(numberFile+1));
        }
    }

    public void setGenus(InfoList infoList){
        workbook.createSheet("Genus");
        for (int i = 0; i < infoList.genusTable.size();i++){
            workbook.getSheet("Genus").createRow(i).createCell(0).setCellValue(infoList.genusTable.get(i).get(0));
            workbook.getSheet("Genus").setColumnWidth(0, 10000);
            workbook.getSheet("Genus").getRow(i).createCell(1).setCellValue(infoList.genusTable.get(i).get(numberFile+1));
        }
    }

    public void setSpecies(InfoList infoList){
        workbook.createSheet("Species");
        for (int i = 0; i < infoList.speciesTable.size();i++){
            workbook.getSheet("Species").createRow(i).createCell(0).setCellValue(infoList.speciesTable.get(i).get(0));
            workbook.getSheet("Species").setColumnWidth(0, 10000);
            workbook.getSheet("Species").getRow(i).createCell(1).setCellValue(infoList.speciesTable.get(i).get(numberFile+1));
        }
    }

    public void setFamily(InfoList infoList){
        workbook.createSheet("Family");
        for (int i = 0; i < infoList.familyTable.size();i++){
            workbook.getSheet("Family").createRow(i).createCell(0).setCellValue(infoList.familyTable.get(i).get(0));
            workbook.getSheet("Family").setColumnWidth(0, 10000);
            workbook.getSheet("Family").getRow(i).createCell(1).setCellValue(infoList.familyTable.get(i).get(numberFile+1));
        }
    }

    public void setBioIndex(InfoList infoList){
        workbook.createSheet("BioIndex");
        workbook.getSheet("BioIndex").createRow(0).createCell(0).setCellValue("BioIndex");
        workbook.getSheet("BioIndex").getRow(0).createCell(1).setCellValue(infoList.bioIndexTable.get(numberFile));
        if(Double.parseDouble(infoList.bioIndexTable.get(numberFile)) < 3.1){
            workbook.getSheet("BioIndex").getRow(0).createCell(2).setCellValue("Низкое значение");
        } else if(Double.parseDouble(infoList.bioIndexTable.get(numberFile)) >= 3.1
            && Double.parseDouble(infoList.bioIndexTable.get(numberFile)) <= 4.2
        ) {
            workbook.getSheet("BioIndex").getRow(0).createCell(2).setCellValue("Среднее значение");
        } else {
            workbook.getSheet("BioIndex").getRow(0).createCell(2).setCellValue("Высокое значение");
        }
        workbook.getSheet("BioIndex").setColumnWidth(0, 10000);
        workbook.getSheet("BioIndex").setColumnWidth(1, 10000);
        workbook.getSheet("BioIndex").setColumnWidth(2, 10000);
    }

    public void getClose() throws IOException {
        workbook.close();
    }

    public void saveFile(InfoList infoList) throws IOException {
        workbook.write(new FileOutputStream(xlsxDirectoryPath + "\\" + infoList.idFileName.get(numberFile) + ".xlsx"));
    }
}

