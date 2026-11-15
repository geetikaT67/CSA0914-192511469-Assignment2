package Task03;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Task03 - Supplied Code Run 2 (AFTER completing TODO 1)
 * hashCode() is properly overridden to be consistent with equals().
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
class StudentCompleted {
    private final String regNo;
    private final String name;

    StudentCompleted(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentCompleted)) return false;
        return regNo.equals(((StudentCompleted) o).regNo);
    }

    // TODO 1: Override hashCode() so that it is consistent with equals()
    @Override
    public int hashCode() {
        return Objects.hashCode(regNo);
    }

    @Override
    public String toString() {
        return regNo + " " + name;
    }
}

public class AttendanceCompleted {
    public static void main(String[] args) {
        System.out.println("=== Attendance AFTER completing TODO 1 (hashCode properly overridden) ===");
        Set<StudentCompleted> present = new HashSet<>();
        present.add(new StudentCompleted("192311001", "Arun")); // scanned at Gate 1
        present.add(new StudentCompleted("192311002", "Divya"));
        present.add(new StudentCompleted("192311001", "Arun")); // scanned again at Gate 3
        
        System.out.println("Unique students: " + present.size());
        System.out.println("Set elements: " + present);
        System.out.println("Explanation: Now that hashCode() is based on regNo, identical regNos produce");
        System.out.println("identical hash codes, mapping them to the EXACT SAME bucket. HashSet then");
        System.out.println("calls equals(), finds they are equal, and discards the duplicate correctly!");
    }
}
