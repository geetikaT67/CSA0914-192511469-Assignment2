package Task03;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Task03 - Q3: Comprehensive Investigation of the hashCode-equals Contract
 * 
 * Tests the 3 designed scenarios:
 *  Test 1: Appears to work correctly (Re-adding same instance reference)
 *  Test 2: Deliberately exposes duplicates (Distinct instances, identical regNo, no hashCode override)
 *  Test 3A: Mutating a key field (regNo) after insertion into HashSet
 *  Test 3B: TreeSet whose compareTo() is inconsistent with equals()
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class HashCodeEqualsInvestigation {

    // Mutable Student class for testing mutation after insertion
    static class MutableStudent {
        String regNo;
        String name;

        MutableStudent(String regNo, String name) {
            this.regNo = regNo;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MutableStudent)) return false;
            return Objects.equals(regNo, ((MutableStudent) o).regNo);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(regNo);
        }

        @Override
        public String toString() {
            return regNo + "(" + name + ")";
        }
    }

    // Inconsistent Comparable Student: compares by name, equals compares by regNo!
    static class InconsistentStudent implements Comparable<InconsistentStudent> {
        String regNo;
        String name;

        InconsistentStudent(String regNo, String name) {
            this.regNo = regNo;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InconsistentStudent)) return false;
            return Objects.equals(regNo, ((InconsistentStudent) o).regNo);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(regNo);
        }

        @Override
        public int compareTo(InconsistentStudent o) {
            // Flawed compareTo: orders ONLY by name!
            return this.name.compareTo(o.name);
        }

        @Override
        public String toString() {
            return regNo + "(" + name + ")";
        }
    }

    static class TestCaseRecord {
        String testName;
        int predictedSize;
        int actualSize;
        boolean outcomeMatchesPrediction;
        String explanation;

        TestCaseRecord(String testName, int predictedSize, int actualSize,
                       boolean matches, String explanation) {
            this.testName = testName;
            this.predictedSize = predictedSize;
            this.actualSize = actualSize;
            this.outcomeMatchesPrediction = matches;
            this.explanation = explanation;
        }
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task03 - Q3: Engineer the hashCode-equals Contract");
        System.out.println(" Student: Geetika T | Reg No: 192511469 | Java Version: 21");
        System.out.println("=========================================================================\n");

        List<TestCaseRecord> records = new ArrayList<>();

        // -------------------------------------------------------------------------
        // Test 1: One set of scans for which HashSet appears to work correctly
        // -------------------------------------------------------------------------
        System.out.println("--- Test 1: Scans where HashSet APPEARS to work correctly ---");
        System.out.println("Scenario: Adding the SAME object reference twice (Junior dev's Student without hashCode).");
        int pred1 = 2;
        System.out.println("Prediction: Size = " + pred1);
        System.out.println("Reason: When the exact same reference is added, s1.equals(s1) and s1 == s1 evaluate to true,");
        System.out.println("and its identity hashCode is obviously identical to itself. The duplicate is rejected.");

        Set<StudentUncompleted> set1 = new HashSet<>();
        StudentUncompleted s1 = new StudentUncompleted("192311001", "Arun");
        StudentUncompleted s2 = new StudentUncompleted("192311002", "Divya");
        set1.add(s1);
        set1.add(s2);
        set1.add(s1); // Same reference re-scanned
        int actual1 = set1.size();
        System.out.println("Actual Size: " + actual1);
        System.out.println("Elements: " + set1);
        records.add(new TestCaseRecord("Test 1: Same Object Reference", pred1, actual1, pred1 == actual1,
                "Identical reference has identical identity hashCode and == equality; appears to work"));
        System.out.println();

        // -------------------------------------------------------------------------
        // Test 2: Deliberately designed to expose duplicate entries
        // -------------------------------------------------------------------------
        System.out.println("--- Test 2: Deliberately designed to EXPOSE duplicate entries ---");
        System.out.println("Scenario: Adding two DISTINCT instances with the SAME regNo and name (no hashCode override).");
        int pred2 = 3;
        System.out.println("Prediction: Size = " + pred2);
        System.out.println("Reason: System.identityHashCode() assigns different hash codes based on memory address.");
        System.out.println("They land in different hash buckets. HashSet never calls equals(), creating duplicate entries.");

        Set<StudentUncompleted> set2 = new HashSet<>();
        set2.add(new StudentUncompleted("192311001", "Arun"));
        set2.add(new StudentUncompleted("192311002", "Divya"));
        set2.add(new StudentUncompleted("192311001", "Arun")); // Distinct instance, identical data
        int actual2 = set2.size();
        System.out.println("Actual Size: " + actual2);
        System.out.println("Elements: " + set2);
        records.add(new TestCaseRecord("Test 2: Distinct Instances Without hashCode", pred2, actual2, pred2 == actual2,
                "Different identity hashCodes map to different buckets; equals() never checked; duplicate admitted"));
        System.out.println();

        // -------------------------------------------------------------------------
        // Test 3A: Custom Test - Mutable Field (regNo) mutated after insertion
        // -------------------------------------------------------------------------
        System.out.println("--- Test 3A: Custom Test - Mutating regNo after insertion into HashSet ---");
        System.out.println("Scenario: Insert Student with regNo '192311001'. Then mutate regNo to '192311999'.");
        int pred3A = 1;
        System.out.println("Prediction: Size stays 1, but contains() returns FALSE! Element becomes an unremovable ghost!");
        System.out.println("Reason: When added, bucket index = hash('192311001') % table.length.");
        System.out.println("When contains() is called after mutation, HashSet looks in bucket = hash('192311999') % table.length.");
        System.out.println("That bucket is empty! So contains() returns false and remove() fails silently.");

        Set<MutableStudent> set3A = new HashSet<>();
        MutableStudent mutStudent = new MutableStudent("192311001", "Arun");
        set3A.add(mutStudent);
        System.out.println("Before mutation: set contains mutStudent? " + set3A.contains(mutStudent));

        // Mutate key field
        mutStudent.regNo = "192311999";
        boolean foundAfterMutation = set3A.contains(mutStudent);
        System.out.println("After mutating regNo: set contains mutStudent? " + foundAfterMutation);
        boolean removed = set3A.remove(mutStudent);
        System.out.println("Attempting remove(mutStudent): removed? " + removed);
        System.out.println("Set size after failed removal: " + set3A.size());
        records.add(new TestCaseRecord("Test 3A: Mutable Key Field in HashSet", 1, set3A.size(),
                !foundAfterMutation && !removed,
                "Hash code changed while in set; mapped to wrong bucket; element trapped and unsearchable"));
        System.out.println();

        // -------------------------------------------------------------------------
        // Test 3B: Custom Test - TreeSet with compareTo() inconsistent with equals()
        // -------------------------------------------------------------------------
        System.out.println("--- Test 3B: Custom Test - TreeSet with compareTo() inconsistent with equals() ---");
        System.out.println("Scenario: Two students have DIFFERENT regNos ('192311001' and '192311005') but SAME name ('Arun').");
        System.out.println("equals() checks regNo (they are distinct). But compareTo() compares ONLY name!");
        int pred3B = 1; // TreeSet drops second student because compareTo() returns 0!
        System.out.println("Prediction: TreeSet size = " + pred3B + " (Silent Data Loss!)");
        System.out.println("Reason: TreeSet adheres to compareTo(), NOT equals(). Since compareTo() returns 0,");
        System.out.println("TreeSet treats them as identical keys and silently drops the second distinct student!");

        Set<InconsistentStudent> treeSet = new TreeSet<>();
        treeSet.add(new InconsistentStudent("192311001", "Arun"));
        treeSet.add(new InconsistentStudent("192311005", "Arun")); // Different student, same name!
        int actual3B = treeSet.size();
        System.out.println("Actual TreeSet Size: " + actual3B);
        System.out.println("TreeSet elements: " + treeSet);
        records.add(new TestCaseRecord("Test 3B: Inconsistent TreeSet compareTo", pred3B, actual3B, pred3B == actual3B,
                "compareTo returned 0 for different regNos; TreeSet silently dropped legitimate unique student"));
        System.out.println();

        // Print Summary Table
        System.out.println("===============================================================================================================");
        System.out.printf("%-35s | %-14s | %-12s | %-12s | %-25s%n",
                "Test Case", "Predicted Size", "Actual Size", "Prediction", "Result Summary");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        for (TestCaseRecord r : records) {
            System.out.printf("%-35s | %-14d | %-12d | %-12s | %-25s%n",
                    r.testName, r.predictedSize, r.actualSize,
                    r.outcomeMatchesPrediction ? "MATCH" : "MISMATCH", r.explanation);
        }
        System.out.println("===============================================================================================================\n");

        saveToCsv(records, "ExperimentalData/q3_attendance_tests.csv");
    }

    private static void saveToCsv(List<TestCaseRecord> records, String path) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("TestCase,PredictedSize,ActualSize,PredictionMatches,Explanation");
            for (TestCaseRecord r : records) {
                pw.printf("\"%s\",%d,%d,%b,\"%s\"%n",
                        r.testName, r.predictedSize, r.actualSize, r.outcomeMatchesPrediction, r.explanation);
            }
            System.out.println("Experimental results successfully written to: " + path);
        } catch (IOException e) {
            System.err.println("Error saving CSV: " + e.getMessage());
        }
    }
}
