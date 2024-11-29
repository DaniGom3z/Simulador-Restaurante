package models;

public class Order {
    private String customerName;
    private OrderStatus status;
    public enum OrderStatus {
        PENDIENTE,
        EN_PROCESO,
        LISTA
    }

    public Order(String customerName) {
        this.customerName = customerName;
        this.status = OrderStatus.PENDIENTE;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Orden de: " + customerName + " | Estado: " + status;
    }
}