import data.InfoList;
import fileView.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {
    public InfoList infoList;
    List<File> tsvDirectory;
    String xlsxDirectoryPath;
    boolean checkLoad, checkUnload, checkStart = false;
    int counter;
    public static String errorMessageStr = "";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button dirLoadButton;

    @FXML
    private Button dirUnloadButton;

    @FXML
    private Text numberTSVFile;

    @FXML
    private Text loadStatus_end;

    @FXML
    private Text loadStatusFileNumber;

    @FXML
    private Button startButton;

    @FXML
    public Button closeButton;

    public MainController() throws IOException, InvalidFormatException {
    }

    int getCounter(int rowCount, int currentNumber) {
        Double temp = new Double(100/rowCount);
        return temp.intValue() + currentNumber;
    }

    public void addHinds(){

        Tooltip tipLoad = new Tooltip();
        tipLoad.setText("Выберите папку, в которой находятся tsv и xlsx файлы");
        tipLoad.setStyle("-fx-text-fill: turquoise;");
        dirLoadButton.setTooltip(tipLoad);

        Tooltip tipUnLoad = new Tooltip();
        tipUnLoad.setText("Выберите папку, в которую должны сохраняться новые образцы");
        tipUnLoad.setStyle("-fx-text-fill: turquoise;");
        dirUnloadButton.setTooltip(tipUnLoad);

        Tooltip tipStart = new Tooltip();
        tipStart.setText("Нажмите, для того, чтобы получить новые образцы");
        tipStart.setStyle("-fx-text-fill: turquoise;");
        startButton.setTooltip(tipStart);

        Tooltip closeStart = new Tooltip();
        closeStart.setText("Нажмите, для того, чтобы закрыть приложение");
        closeStart.setStyle("-fx-text-fill: turquoise;");
        closeButton.setTooltip(closeStart);

    }

    public void removeHinds(){
        dirLoadButton.setTooltip(null);
        dirUnloadButton.setTooltip(null);
        startButton.setTooltip(null);
        closeButton.setTooltip(null);
    }

    public static boolean tempHints = true;

    @FXML
    void initialize() throws FileNotFoundException, InterruptedException {
        addHinds();

        FileInputStream loadStream = new FileInputStream(Application.rootDirPath + "\\load.png");
        Image loadImage = new Image(loadStream);
        ImageView loadView = new ImageView(loadImage);
        dirLoadButton.graphicProperty().setValue(loadView);

        FileInputStream unloadStream = new FileInputStream(Application.rootDirPath + "\\unload.png");
        Image unloadImage = new Image(unloadStream);
        ImageView unloadView = new ImageView(unloadImage);
        dirUnloadButton.graphicProperty().setValue(unloadView);

        FileInputStream startStream = new FileInputStream(Application.rootDirPath + "\\start.png");
        Image startImage = new Image(startStream);
        ImageView startView = new ImageView(startImage);
        startButton.graphicProperty().setValue(startView);

        FileInputStream closeStream = new FileInputStream(Application.rootDirPath + "\\logout.png");
        Image closeImage = new Image(closeStream);
        ImageView closeView = new ImageView(closeImage);
        closeButton.graphicProperty().setValue(closeView);

        int r = 60;
        startButton.setShape(new Circle(r));
        startButton.setMinSize(r*2, r*2);
        startButton.setMaxSize(r*2, r*2);

        checkLoad = false;
        checkUnload = false;

        closeButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });

        dirLoadButton.setOnAction(actionEvent -> {
            if(!checkStart)
            {
                loadStatus_end.setText("");
                loadStatusFileNumber.setText("");
                numberTSVFile.setText("");
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File dir = directoryChooser.showDialog(new Stage());
                File[] file = dir.listFiles();
                tsvDirectory = Arrays.asList(file);
                checkLoad = true;
            }
            else
            {
                errorMessageStr = "Происходит формирование файлов. Повторите попытку попытку позже...";
                ErrorController errorController = new ErrorController();
                try {
                    errorController.start(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        dirUnloadButton.setOnAction(actionEvent -> {
                    if(!checkStart)
                    {
                        loadStatus_end.setText("");
                        loadStatusFileNumber.setText("");
                        numberTSVFile.setText("");
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        File dir = directoryChooser.showDialog(new Stage());
                        xlsxDirectoryPath = dir.getPath();
                        checkUnload = true;

                    }
                    else
                    {
                        errorMessageStr = "Происходит формирование файлов. Повторите попытку попытку позже...";
                        ErrorController errorController = new ErrorController();
                        try {
                            errorController.start(new Stage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        startButton.setOnAction(actionEvent -> {
                    if(!checkStart){
                        loadStatus_end.setText("");
                        loadStatusFileNumber.setText("");
                        numberTSVFile.setText("");
                        if(checkLoad && checkUnload){
                                if(tsvDirectory.size() != 0)
                                {
                                    checkStart = true;
                                    counter=0;
                                    new Thread(){
                                        @Override
                                        public void run(){
                                            infoList = new InfoList();
                                            for (int i = 0; i<tsvDirectory.size();i++)
                                            {
                                                if(tsvDirectory.get(i).getPath().contains(".tsv"))
                                                {
                                                    numberTSVFile.setText("Сбор данных файла " + tsvDirectory.get(i).getName().replace(".tsv", ""));
                                                    if(tsvDirectory.get(i).getName().contains("Phylum")){
                                                        try {
                                                            TSVOpen tsvOpen = new TSVOpen(tsvDirectory.get(i));
                                                            PhylumTableTSV phylumTableTSV = new PhylumTableTSV(tsvDirectory.get(i));
                                                            tsvOpen.getIdFileName(infoList);
                                                            tsvOpen.getClose();
                                                            phylumTableTSV.getPhylumTable(infoList);
                                                            phylumTableTSV.getClose();
                                                            tsvOpen.getClose();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InvalidFormatException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else if(tsvDirectory.get(i).getName().contains("Class")){
                                                        try {
                                                            ClassTableTSV classTableTSV = new ClassTableTSV(tsvDirectory.get(i));
                                                            classTableTSV.getClassTable(infoList);
                                                            classTableTSV.getClose();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InvalidFormatException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else if(tsvDirectory.get(i).getName().contains("Genus")){
                                                        try {
                                                            GenusTableTSV genusTableTSV = new GenusTableTSV(tsvDirectory.get(i));
                                                            genusTableTSV.getGenusTable(infoList);
                                                            genusTableTSV.getClose();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InvalidFormatException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else if(tsvDirectory.get(i).getName().contains("Species")){
                                                        try {
                                                            SpeciesTableTSV speciesTableTSV = new SpeciesTableTSV(tsvDirectory.get(i));
                                                            speciesTableTSV.getSpeciesTable(infoList);
                                                            speciesTableTSV.getClose();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InvalidFormatException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else if(tsvDirectory.get(i).getName().contains("Family")){
                                                        try {
                                                            FamilyTableTSV familyTableTSV = new FamilyTableTSV(tsvDirectory.get(i));
                                                            familyTableTSV.getFamilyTable(infoList);
                                                            familyTableTSV.getClose();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InvalidFormatException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else if(tsvDirectory.get(i).getName().contains("alpha")){
                                                        try {
                                                            BioIndexTableTSV bioIndexTableTSV = new BioIndexTableTSV(tsvDirectory.get(i));
                                                            bioIndexTableTSV.getBioIndexTable(infoList);
                                                            bioIndexTableTSV.getClose();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InvalidFormatException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                            numberTSVFile.setText("");
                                            for (int i = 0; i < infoList.idFileName.size(); i++){
                                                loadStatusFileNumber.setText("Формирование файла " + infoList.idFileName.get(i));
                                                try {
                                                    MainLoader mainLoader = new MainLoader(xlsxDirectoryPath, i);
                                                    mainLoader.setPhylum(infoList);
                                                    mainLoader.setClass(infoList);
                                                    mainLoader.setGenus(infoList);
                                                    mainLoader.setSpecies(infoList);
                                                    mainLoader.setFamily(infoList);
                                                    mainLoader.setBioIndex(infoList);
                                                    mainLoader.saveFile(infoList);
                                                    mainLoader.getClose();
                                                    counter++;
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (InvalidFormatException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            loadStatusFileNumber.setText("");
                                            loadStatus_end.setText("Успешно сформировано " + counter + " файла(ов)!");
                                            checkStart = false;
                                        }
                                    }.start();
                                } else
                                {
                                    errorMessageStr = "Выбранная папка загрузки является пустой...";
                                    ErrorController errorController = new ErrorController();
                                    try {
                                        errorController.start(new Stage());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                            }
                        } else {
                            errorMessageStr = "Вы не указаали директорию загрузки или директорию выгрузки...";
                            ErrorController errorController = new ErrorController();
                            try {
                                errorController.start(new Stage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else
                    {
                        errorMessageStr = "Происходит обработка файлов. Повторите попытку попытку позже...";
                        ErrorController errorController = new ErrorController();
                        try {
                            errorController.start(new Stage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
