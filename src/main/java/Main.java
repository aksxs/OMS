import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ini4j.Ini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Locale;

public class Main extends Application {
    public static Ini ini;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("������� ���������� ��������");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        File ordersFile = new File("src/main/resources/orders.json");
        createFile(ordersFile);

        File configFile = new File("config.ini");
        createFile(configFile);
        ini = new Ini(configFile);
        if (ini.get("paths", "orders") == null) {
            try (FileWriter fw = new FileWriter(configFile, true)) {
                fw.write("[paths]\norders=" + ordersFile.getPath().replace("\\", "/"));
                fw.flush();
            }
        }
        ini.load(configFile);

        if (!fileIsJson(new File(ini.get("paths", "orders")))) {
            try (FileWriter fw = new FileWriter(ordersFile)) {
                fw.write("[]");
                fw.flush();
            }
        }

        launch(args);
    }

    public static void createFile(File file) throws Exception {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new Exception("��� ������� ��� �������� �����");
            }
        }
    }

    public static boolean fileIsJson(File file) throws Exception {
        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }

            return text.toString().matches("^\\s*\\[.*\\]\\s*$");
        }
    }
}
