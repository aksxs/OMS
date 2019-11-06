package domain;

import java.util.Date;

public class Order {

    private int id;
    private String name;
    private String address;
    private Date date;
    private OrderStatus status;

    public Order(int id, String name, String address, Date date, OrderStatus status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
