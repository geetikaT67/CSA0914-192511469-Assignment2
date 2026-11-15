package Task04;

import java.util.Objects;

/**
 * Task04 - Medicine Model for Hospital Pharmacy Inventory
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class Medicine implements Comparable<Medicine> {
    private final String code;
    private final String name;
    private int stock;
    private final double unitPrice;

    public Medicine(String code, String name, int stock, double unitPrice) {
        this.code = code;
        this.name = name;
        this.stock = stock;
        this.unitPrice = unitPrice;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public double getUnitPrice() { return unitPrice; }

    public void setStock(int stock) { this.stock = stock; }

    @Override
    public int compareTo(Medicine other) {
        return this.code.compareTo(other.code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicine)) return false;
        Medicine medicine = (Medicine) o;
        return Objects.equals(code, medicine.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return "[" + code + "] " + name + " (Qty: " + stock + ", Rs." + unitPrice + ")";
    }
}
