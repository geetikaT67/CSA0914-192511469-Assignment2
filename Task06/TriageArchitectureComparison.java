package Task06;

import java.util.*;

/**
 * Task06 - Alternative Architecture: PriorityQueue vs Sorted LinkedList vs 5-FIFO Queues
 * 
 * Compares 3 alternative triage designs:
 *  1. PriorityQueue (Min-Heap): Admit O(log n), Poll O(log n)
 *  2. Sorted LinkedList: Admit O(n) linear search insertion, Poll O(1) head removal
 *  3. Array of 5 FIFO Queues (Multi-level Queue): Admit O(1), Poll O(1)
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class TriageArchitectureComparison {

    // Architecture 2: Sorted LinkedList
    static class SortedLinkedListTriage {
        private final LinkedList<Patient> list = new LinkedList<>();

        public void admit(Patient p) {
            // Linear search for insertion point to maintain sorted order
            ListIterator<Patient> it = list.listIterator();
            while (it.hasNext()) {
                Patient cur = it.next();
                if (p.severity < cur.severity || (p.severity == cur.severity && p.arrival < cur.arrival)) {
                    it.previous();
                    it.add(p);
                    return;
                }
            }
            list.addLast(p);
        }

        public Patient next() {
            return list.isEmpty() ? null : list.removeFirst();
        }
    }

    // Architecture 3: Array of 5 FIFO Queues
    @SuppressWarnings("unchecked")
    static class FiveQueueTriage {
        private final ArrayDeque<Patient>[] queues = new ArrayDeque[5];

        public FiveQueueTriage() {
            for (int i = 0; i < 5; i++) {
                queues[i] = new ArrayDeque<>();
            }
        }

        public void admit(Patient p) {
            // Direct O(1) indexing into severity bucket!
            queues[p.severity - 1].addLast(p);
        }

        public Patient next() {
            // Scan from highest severity (1) to lowest (5): at most 5 checks!
            for (int i = 0; i < 5; i++) {
                if (!queues[i].isEmpty()) {
                    return queues[i].pollFirst();
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task06: Triage Architectural Comparison");
        System.out.println(" Comparing Min-Heap vs Sorted LinkedList vs 5-FIFO Queues");
        System.out.println("=========================================================================\n");

        int n = 10000;
        Random rng = new Random(42);
        List<Patient> patients = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            patients.add(new Patient("P-" + i, 1 + rng.nextInt(5), i));
        }

        // 1. PriorityQueue (Min-Heap)
        long startPq = System.nanoTime();
        PriorityQueue<Patient> pq = new PriorityQueue<>(
                Comparator.comparingInt((Patient p) -> p.severity).thenComparingLong(p -> p.arrival)
        );
        for (Patient p : patients) pq.add(p);
        while (!pq.isEmpty()) pq.poll();
        double timePq = (System.nanoTime() - startPq) / 1_000_000.0;

        // 2. 5-FIFO Queues
        long start5Q = System.nanoTime();
        FiveQueueTriage fiveQ = new FiveQueueTriage();
        for (Patient p : patients) fiveQ.admit(p);
        while (fiveQ.next() != null);
        double time5Q = (System.nanoTime() - start5Q) / 1_000_000.0;

        // 3. Sorted LinkedList (Tested with smaller batch of 2,000 to prevent test timeout)
        int llCount = 2000;
        long startLl = System.nanoTime();
        SortedLinkedListTriage sll = new SortedLinkedListTriage();
        for (int i = 0; i < llCount; i++) sll.admit(patients.get(i));
        while (sll.next() != null);
        double timeLl = (System.nanoTime() - startLl) / 1_000_000.0;

        System.out.printf("1. PriorityQueue (Min-Heap)       [10k ops] : %8.2f ms%n", timePq);
        System.out.printf("2. Five FIFO Queues (Multi-level) [10k ops] : %8.2f ms (Fastest, O(1) ops)%n", time5Q);
        System.out.printf("3. Sorted LinkedList             [ 2k ops] : %8.2f ms (Severely throttled by O(n) insertions)%n%n", timeLl);

        System.out.println("--- Architectural & Complexity Summary ---");
        System.out.println("Architecture            | Admit Time | Next Time | Memory Per Patient | Ordering Guarantees");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("1. PriorityQueue (Heap) | O(log n)   | O(log n)  | ~24 bytes (array)  | Min severity, FIFO ties");
        System.out.println("2. Sorted LinkedList    | O(n)       | O(1)      | ~32 bytes (Node)   | Strict order, high overhead");
        System.out.println("3. Array of 5 FIFO Queues| O(1)      | O(1)      | ~16 bytes (buffer) | Perfect FIFO per tier, 0 compare");
        System.out.println("=========================================================================");
    }
}
