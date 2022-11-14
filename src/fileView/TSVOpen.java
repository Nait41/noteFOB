package fileView;

import data.InfoList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class TSVOpen {
    Scanner scanner;

    public TSVOpen(File file) throws IOException, InvalidFormatException {
        scanner = new Scanner(file);
    }

    public void getClose() throws IOException {
        scanner.close();
    }

    public void getIdFileName(InfoList infoList){
        String line;
        line = scanner.nextLine();
        String[] elements = line.split("\t");
        for (int i = 0; i < elements.length; i++){
            if(elements[i].length()>0){
                if(elements[i].toLowerCase(Locale.ROOT).charAt(0) >= 'a' && elements[i].toLowerCase(Locale.ROOT).charAt(0) <= 'z'){
                    infoList.idFileName.add(elements[i]);
                }
            }
        }
    }

}