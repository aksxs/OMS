import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import domain.Order;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class MainController  {

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
    private TableColumn<Order, String> orderDateColumn;

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
        //TODO thread; read from file; json

        new Thread(() -> {

            File file = new File(getClass().getClassLoader().getResource("orders.json").getFile().replace("%20", " "));

            try {
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new FileReader(file));
                List<Order> ordersFromFile = gson.fromJson(reader, new TypeToken<List<Order>>() {}.getType());
                orders.addAll(ordersFromFile);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }


}
