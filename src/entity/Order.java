package entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order {
    private String orderId;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private String customerId;
    private double coust;

    public Order() {
    }

    public Order(String orderId, LocalDate orderDate, LocalTime orderTime, String customerId, double coust) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.customerId = customerId;
        this.coust = coust;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getCoust() {
        return coust;
    }

    public void setCoust(double coust) {
        this.coust = coust;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", orderTime=" + orderTime +
                ", customerId='" + customerId + '\'' +
                ", coust=" + coust +
                '}';
    }
}
