import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import domain.Goods;
import domain.Order;
import domain.OrderStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class ModalOrder {
    @FXML
    private TextField name;

    @FXML
    private ComboBox<String> goodComboBox;

    @FXML
    private TextField address;

    @FXML
    private DatePicker date;

    @FXML
    private TextField time;

    @FXML
    private CheckBox completed;

    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        ObservableList<String> goods = FXCollections.observableArrayList(Goods.list);
        goodComboBox.setItems(goods);
    }

    @FXML
    private void save() {
        try {
            File file = new File(Main.ini.get("paths", "orders"));

            Gson gson = GsonInstance.getGson();
            JsonReader reader = new JsonReader(new FileReader(file));
            List<Order> ordersFromFile = gson.fromJson(reader, new TypeToken<List<Order>>() {}.getType());

            Order lastOrder = ordersFromFile.stream().max(Comparator.comparingInt(Order::getId)).orElse(null);
            int id = lastOrder == null ? 0 : lastOrder.getId();

            Order order = new Order(
                    id + 1,
                    name.getText(),
                    goodComboBox.getValue(),
                    address.getText(),
                    new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(date.getValue().toString() + " " + time.getText()),
                    completed.isSelected() ? OrderStatus.COMPLETED : OrderStatus.ACTIVE
            );

            ordersFromFile.add(order);

            java.lang.String path = file.getPath();
            java.lang.String lyboi = GsonInstance.getGson().toJson(ordersFromFile);
            FileWriter fileWriter = new FileWriter(path, false);
            fileWriter.write(lyboi);
            fileWriter.flush();
            fileWriter.close();

            ((Stage) saveButton.getScene().getWindow()).close();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
