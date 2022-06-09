package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class OrderDTO {
    private String orderId;
    private String orderDate;
    private String customerId;
    private BigDecimal orderTotal;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String orderDate, String customerId, BigDecimal orderTotal) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.orderTotal = orderTotal;
    }

    public OrderDTO(String orderId, String customerId, BigDecimal orderTotal) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderTotal = orderTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", customerId='" + customerId + '\'' +
                ", orderTotal=" + orderTotal +
                '}';
    }
}
