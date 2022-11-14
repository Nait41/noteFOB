package fileView;

import data.InfoList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class BioIndexTableTSV {
    Scanner scanner;

    public BioIndexTableTSV(File file) throws IOException, InvalidFormatException {
        scanner = new Scanner(file);
    }

    public void getClose() throws IOException {
        scanner.close();
    }

    public void getBioIndexTable(InfoList infoList){
        scanner.nextLine();
        for(int k = 0;scanner.hasNextLine();k++){
            String line;
            line = scanner.nextLine();
            String[] elements = line.split("\t");
            infoList.bioIndexTable.add(elements[6]);
        }
    }

}