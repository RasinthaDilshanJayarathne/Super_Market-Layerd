package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OrderDTO {
    private String orderId;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private String customerId;
    private double coust;
    private double orderTotal;
    private List<OrderDetailDTO> orderDetail;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, LocalDate orderDate, LocalTime orderTime, String customerId, List<OrderDetailDTO> orderDetails, double orderTotal) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCustomerId(customerId);
        this.setOrderDetail(orderDetails);
        this.setOrderTotal(orderTotal);
    }

    public OrderDTO(String orderId, LocalDate orderDate, LocalTime orderTime, String customerId, double coust, double orderTotal, List<OrderDetailDTO> orderDetail) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.customerId = customerId;
        this.coust = coust;
        this.orderTotal = orderTotal;
        this.orderDetail = orderDetail;
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

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public List<OrderDetailDTO> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetailDTO> orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", orderTime=" + orderTime +
                ", customerId='" + customerId + '\'' +
                ", coust=" + coust +
                ", orderTotal=" + orderTotal +
                ", orderDetail=" + orderDetail +
                '}';
    }
}
