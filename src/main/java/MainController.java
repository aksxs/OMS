import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import domain.Order;
import domain.OrderStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainController  {

    private List<Order> allOrders = new ArrayList();
    private ObservableList<Order> orders = FXCollections.observableArrayList();

    @FXML
    private TableView<Order> tableView;

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private TableColumn<Order, String> orderNameColumn;

    @FXML
    private TableColumn<Order, String> orderAddressColumn;

    @FXML
    private TableColumn<Order, Date> orderDateColumn;

    @FXML
    private TableColumn orderActionColumn;

    @FXML
    private Button newOrders;

    @FXML
    private Button completedOrders;

    @FXML
    private Button acceptedOrders;

    @FXML
    private Button addOrder;

    @FXML
    private void initialize() {
        initData();

        orderIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        orderNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        orderAddressColumn.setCellValueFactory(new PropertyValueFactory("address"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory("date"));

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
                }
                else {
                    setText(format.format(date));
                }
            }
        });

        new Thread(() -> {

            File file = new File(getClass().getClassLoader().getResource("orders.json").getFile().replace("%20", " "));

            try {
                Gson gson = GsonInstance.getGson();
                JsonReader reader = new JsonReader(new FileReader(file));
                List<Order> ordersFromFile = gson.fromJson(reader, new TypeToken<List<Order>>() {}.getType());
                allOrders.addAll(ordersFromFile);

                //TODO del when configurate ini file
                orders.addAll(ordersFromFile);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    @FXML
    private void openNewOrders() {
        orders.clear();
        orders.addAll(
                allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.NEW)
                .collect(Collectors.toList())
        );
    }

    @FXML
    private void openAcceptedOrders() {
        orders.clear();
        orders.addAll(
                allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.ACCEPTED)
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
    private void addOrder() {

    }

}
