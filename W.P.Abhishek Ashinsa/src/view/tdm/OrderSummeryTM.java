package view.tdm;

import java.math.BigDecimal;

public class OrderSummeryTM {
    private String orId;
    private String customerId;
    private String itemCode;
    private int qty;
    private BigDecimal total;

    public OrderSummeryTM() {
    }

    public OrderSummeryTM(String orId, String customerId, String itemCode, int qty, BigDecimal total) {
        this.orId = orId;
        this.customerId = customerId;
        this.itemCode = itemCode;
        this.qty = qty;
        this.total = total;
    }

    public String getOrId() {
        return orId;
    }

    public void setOrId(String orId) {
        this.orId = orId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderSummeryTM{" +
                "orId='" + orId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", qty=" + qty +
                ", total=" + total +
                '}';
    }
}
