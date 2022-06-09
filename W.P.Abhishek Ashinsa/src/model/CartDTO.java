package model;

import java.math.BigDecimal;

public class CartDTO {
    private String OrderId;
    private String CustId;
    private String itemCode;
    private int orQty;
    private BigDecimal total;

    public CartDTO() {
    }

    public CartDTO(String orderId, String custId, String itemCode, int orQty, BigDecimal total) {
        OrderId = orderId;
        CustId = custId;
        this.itemCode = itemCode;
        this.orQty = orQty;
        this.total = total;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getOrQty() {
        return orQty;
    }

    public void setOrQty(int orQty) {
        this.orQty = orQty;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CartDTO{" +
                "OrderId='" + OrderId + '\'' +
                ", CustId='" + CustId + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", orQty=" + orQty +
                ", total=" + total +
                '}';
    }
}
