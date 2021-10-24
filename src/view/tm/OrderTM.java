package view.tm;

import java.time.LocalDate;

public class OrderTM {
    private String orderId;
    private LocalDate orderDate;
    private String orderTime;
    private String customerId;
    private double coust;

    public OrderTM() {
    }

    public OrderTM(String orderId, LocalDate orderDate, String orderTime, String customerId, double coust) {
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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
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
                ", orderTime='" + orderTime + '\'' +
                ", customerId='" + customerId + '\'' +
                ", coust=" + coust +
                '}';
    }
}
