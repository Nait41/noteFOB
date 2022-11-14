package fileView;

import data.InfoList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SpeciesTableTSV {
    Scanner scanner;

    public SpeciesTableTSV(File file) throws IOException, InvalidFormatException {
        scanner = new Scanner(file);
    }

    public void getClose() throws IOException {
        scanner.close();
    }

    public void getSpeciesTable(InfoList infoList){
        scanner.nextLine();
        for(int k = 0;scanner.hasNextLine();k++){
            String line;
            line = scanner.nextLine();
            String[] elements = line.split("\t");
            infoList.speciesTable.add(new ArrayList<>());
            for (int i = 0; i < elements.length; i++){
                if(elements[i].length()>0) {
                    infoList.speciesTable.get(k).add(elements[i]);
                }
            }
        }
    }

}