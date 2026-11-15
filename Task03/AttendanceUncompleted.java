package Task03;

import java.util.HashSet;
import java.util.Set;

/**
 * Task03 - Supplied Code Run 1 (BEFORE completing TODO 1)
 * hashCode() is NOT overridden, inheriting Object.hashCode() (identity hash code).
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
class StudentUncompleted {
    private final String regNo;
    private final String name;

    StudentUncompleted(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentUncompleted)) return false;
        return regNo.equals(((StudentUncompleted) o).regNo);
    }

    // TODO 1: Override hashCode() so that it is consistent with equals()
    // [UNCOMPLETED - Left as default Object.hashCode()]

    @Override
    public String toString() {
        return regNo + " " + name;
    }
}

public class AttendanceUncompleted {
    public static void main(String[] args) {
        System.out.println("=== Attendance BEFORE completing TODO 1 (Missing hashCode) ===");
        Set<StudentUncompleted> present = new HashSet<>();
        present.add(new StudentUncompleted("192311001", "Arun")); // scanned at Gate 1
        present.add(new StudentUncompleted("192311002", "Divya"));
        present.add(new StudentUncompleted("192311001", "Arun")); // scanned again at Gate 3
        
        System.out.println("Unique students: " + present.size());
        System.out.println("Set elements: " + present);
        System.out.println("Explanation: Even though equals() returns true for identical regNos,");
        System.out.println("two separate 'new Student()' instances have different identity hashCodes");
        System.out.println("from Object.hashCode(). Therefore, they get placed into different hash buckets");
        System.out.println("in the HashSet and equals() is never even invoked during insertion!");
    }
}
