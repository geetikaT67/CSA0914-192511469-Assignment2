package Task06;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Task06 - Q6: Hospital Emergency Triage System
 * 
 * Demonstrates PriorityQueue enforcing lowest severity number first (S1 to S5),
 * and earliest arrival counter for equal severity tie-breaking.
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
class Patient {
    final String name;
    final int severity;
    final long arrival;

    Patient(String name, int severity, long arrival) {
        this.name = name;
        this.severity = severity;
        this.arrival = arrival;
    }

    @Override
    public String toString() {
        return name + "(S" + severity + ")";
    }
}

public class Triage {
    private long counter = 0;
    // PriorityQueue serving lowest severity first, then earliest arrival first
    private final PriorityQueue<Patient> queue = new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                      .thenComparingLong(p -> p.arrival)
    );

    public void admit(String name, int severity) {
        queue.add(new Patient(name, severity, counter++));
    }

    public Patient next() {
        return queue.poll();
    }

    public PriorityQueue<Patient> getInternalQueue() {
        return queue;
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task06 - Q6: Hospital Emergency Triage Demo");
        System.out.println(" Student: Geetika T | Reg No: 192511469 | Java Version: 21");
        System.out.println("=========================================================================\n");

        Triage er = new Triage();
        er.admit("Ravi", 3);
        er.admit("Meena", 1);
        er.admit("John", 3);
        er.admit("Asha", 5);
        er.admit("Kumar", 1);
        er.admit("Lata", 2);

        System.out.println("Admitted 6 patients: Ravi(S3), Meena(S1), John(S3), Asha(S5), Kumar(S1), Lata(S2)");
        
        StringBuilder order = new StringBuilder();
        Patient p;
        while ((p = er.next()) != null) {
            order.append(p).append(' ');
        }
        String actualOutput = order.toString().trim();
        System.out.println("\nActual Served Order:   " + actualOutput);
        System.out.println("Expected Served Order: Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)");
        System.out.println("Matches Expected? " + actualOutput.equals("Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)"));

        System.out.println("\n--- Arrival Counter Tie-Breaker Explanation ---");
        System.out.println("1. Meena and Kumar both have Severity 1.");
        System.out.println("   Meena arrived at counter = 1, Kumar arrived at counter = 4.");
        System.out.println("   Comparator compares arrival counter: Meena (1) < Kumar (4) -> Meena served first.");
        System.out.println("2. Ravi and John both have Severity 3.");
        System.out.println("   Ravi arrived at counter = 0, John arrived at counter = 2.");
        System.out.println("   Comparator compares arrival counter: Ravi (0) < John (2) -> Ravi served first.");
        System.out.println("   This guarantees strict FIFO fairness within the exact same severity tier.");
        System.out.println("=========================================================================");
    }
}
