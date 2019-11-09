import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import domain.Goods;
import domain.Order;
import domain.OrderStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    private List<Order> allOrders = new ArrayList<>();
    private ObservableList<Order> orders = FXCollections.observableArrayList();

    @FXML
    private TableView<Order> tableView;

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private TableColumn<Order, String> orderNameColumn;

    @FXML
    private TableColumn<Order, String> orderGoodsColumn;

    @FXML
    private TableColumn<Order, String> orderAddressColumn;

    @FXML
    private TableColumn<Order, Date> orderDateColumn;

    @FXML
    private TableColumn<Order, OrderStatus> orderStatusColumn;

    @FXML
    private TableColumn orderActionColumn;

    @FXML
    private Button allOrdersBtn;

    @FXML
    private Button newOrders;

    @FXML
    private Button completedOrders;

    @FXML
    private Button addOrder;

    @FXML
    private void initialize() {
        initData();

        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        orderGoodsColumn.setCellValueFactory(new PropertyValueFactory<>("good"));
        orderAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(orders);
    }

    private void initData() {
        orderDateColumn.setCellFactory(column -> new TableCell<Order, Date>() {
            private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            @Override
            protected void updateItem(Date date, boolean isEmpty) {
                super.updateItem(date, isEmpty);
                if (isEmpty) {
                    setText(null);
                } else {
                    setText(format.format(date));
                }
            }
        });

        new Thread(() -> {
            File file = new File(Main.ini.get("paths", "orders"));

            try {
                Gson gson = GsonInstance.getGson();
                JsonReader reader = new JsonReader(new FileReader(file));
                List<Order> ordersFromFile = gson.fromJson(reader, new TypeToken<List<Order>>() {}.getType());
                allOrders.addAll(ordersFromFile);

                //TODO del when configurate ini file
                openAllOrders();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void openAllOrders() {
        orders.clear();
        orders.addAll(allOrders);
    }

    @FXML
    private void openActiveOrders() {
        orders.clear();
        orders.addAll(
                allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.ACTIVE)
                .collect(Collectors.toList())
        );
    }

    @FXML
    private void openCompletedOrders() {
        orders.clear();
        orders.addAll(
                allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .collect(Collectors.toList())
        );
    }

    @FXML
    private void addNewOrder() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modalOrder.fxml"));

        Stage popup = new Stage();
        popup.setTitle("����� �����");
        popup.setScene(new Scene(loader.load()));
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        refreshData();
    }

    @FXML
    private MenuItem saveJsonButton;

    @FXML
    private void saveJson() throws Exception {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files", ".json");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(addOrder.getScene().getWindow());
        Main.createFile(file);

        File ordersJsonFile = new File(Main.ini.get("paths", "orders"));

        Gson gson = GsonInstance.getGson();
        JsonReader reader = new JsonReader(new FileReader(ordersJsonFile));
        List<Order> ordersFromFile = gson.fromJson(reader, new TypeToken<List<Order>>() {}.getType());

        String path = file.getPath();
        String lyboi = GsonInstance.getGson().toJson(ordersFromFile);

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
        out.append(lyboi);
        out.close();
    }

    public void refreshData () {
        new Thread(() -> {
            File file = new File(Main.ini.get("paths", "orders"));

            try {
                Gson gson = GsonInstance.getGson();
                JsonReader reader = new JsonReader(new FileReader(file));
                List<Order> ordersFromFile = gson.fromJson(reader, new TypeToken<List<Order>>() {
                }.getType());
                allOrders.clear();
                allOrders.addAll(ordersFromFile);

                orders.clear();
                orders.addAll(ordersFromFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
