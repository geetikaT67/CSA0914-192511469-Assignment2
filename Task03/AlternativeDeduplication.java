package Task03;

import java.util.*;

/**
 * Task03 - Alternative Concept: De-duplication using TreeSet vs HashMap vs HashSet
 * 
 * Demonstrates:
 *  1. TreeSet<Student> with explicit Comparator on regNo: O(log n) per scan
 *  2. HashMap<String, Student> keyed by regNo: O(1) average per scan
 *  3. HashSet<Student> with properly overridden hashCode/equals: O(1) average per scan
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class AlternativeDeduplication {

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task03: Alternative De-duplication Architectures");
        System.out.println(" Comparing HashSet vs TreeSet vs HashMap");
        System.out.println("=========================================================================\n");

        List<StudentCompleted> rawScans = Arrays.asList(
                new StudentCompleted("192311001", "Arun"),
                new StudentCompleted("192311002", "Divya"),
                new StudentCompleted("192311003", "Karthik"),
                new StudentCompleted("192311001", "Arun"),    // Duplicate
                new StudentCompleted("192311004", "Pooja"),
                new StudentCompleted("192311002", "Divya")     // Duplicate
        );

        System.out.println("Total raw scans received: " + rawScans.size());

        // Approach 1: HashSet<Student>
        Set<StudentCompleted> hashSet = new HashSet<>(rawScans);
        System.out.println("1. HashSet<Student> size: " + hashSet.size() + " | Elements: " + hashSet);

        // Approach 2: TreeSet<Student> with Comparator on regNo
        Set<StudentCompleted> treeSet = new TreeSet<>(Comparator.comparing(s -> s.toString().split(" ")[0]));
        treeSet.addAll(rawScans);
        System.out.println("2. TreeSet<Student> size: " + treeSet.size() + " | Elements (Sorted): " + treeSet);

        // Approach 3: HashMap<String, Student> keyed by regNo
        Map<String, StudentCompleted> map = new HashMap<>();
        for (StudentCompleted s : rawScans) {
            String regNo = s.toString().split(" ")[0];
            map.putIfAbsent(regNo, s);
        }
        System.out.println("3. HashMap<String, Student> size: " + map.size() + " | Keys: " + map.keySet());

        System.out.println("\n--- Complexity & Architectural Trade-off Analysis ---");
        System.out.println("Structure              | Time per Scan   | Space per Unique Student | Ordering Guarantee");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("HashSet<Student>       | O(1) average    | ~32 bytes (Map.Entry)    | No ordering guaranteed");
        System.out.println("TreeSet<Student>       | O(log n)        | ~40 bytes (TreeMap.Node) | Natural/Comparator sorted");
        System.out.println("HashMap<String,Student>| O(1) average    | ~32 bytes + String key   | No ordering guaranteed");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Key Insights:");
        System.out.println("• TreeSet does not rely on hashCode() or equals(); it only uses the Comparator / compareTo().");
        System.out.println("  However, each insert takes O(log n) tree traversals and node rebalancing (Red-Black Tree).");
        System.out.println("• HashMap keyed by String regNo bypasses any flawed Student hashCode completely,");
        System.out.println("  because java.lang.String already has a robust, immutable hashCode implementation!");
        System.out.println("• But when storing Student objects directly in HashSet, correctly implementing");
        System.out.println("  hashCode() and making fields immutable (final) is the standard and cleanest design.");
        System.out.println("=========================================================================");
    }
}
