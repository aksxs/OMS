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

public class Main extends Application {
    public static Ini ini;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Система управления заказами");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        File ordersFile = new File("src/main/resources/orders.json");
        createFileIfExists(ordersFile);

        File configFile = new File("config.ini");
        createFileIfExists(configFile);

        ini = new Ini(configFile);
        if (ini.get("paths", "orders") == null) {
            ini.put("paths", "orders", ordersFile);
            ini.store();
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

    public static void createFileIfExists(File file) throws Exception {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new Exception("Нет доступа для создания файла");
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
