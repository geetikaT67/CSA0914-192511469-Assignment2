package Task07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Task07 - Original Triage System (Q6 Baseline)
 * 
 * Uses a single java.util.PriorityQueue (Binary Min-Heap).
 * Demonstrates the limitation when implementing Priority Aging:
 * Updating priorities requires draining and re-heapifying the entire queue,
 * resulting in an O(n log n) bottleneck!
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class OriginalTriageSystem {

    public static class TriagePatient {
        public final String name;
        public int severity;
        public final long arrivalTime;

        public TriagePatient(String name, int severity, long arrivalTime) {
            this.name = name;
            this.severity = severity;
            this.arrivalTime = arrivalTime;
        }

        @Override
        public String toString() {
            return name + "(S" + severity + ", arr:" + arrivalTime + ")";
        }
    }

    private final PriorityQueue<TriagePatient> heap;
    private long admitCount = 0;

    public OriginalTriageSystem() {
        this.heap = new PriorityQueue<>(
                Comparator.comparingInt((TriagePatient p) -> p.severity)
                          .thenComparingLong(p -> p.arrivalTime)
        );
    }

    // O(log n) sift-up
    public void admit(String name, int severity, long currentTime) {
        heap.add(new TriagePatient(name, severity, currentTime));
        admitCount++;
    }

    // O(log n) sift-down
    public TriagePatient nextPatient() {
        return heap.poll();
    }

    public int size() {
        return heap.size();
    }

    /**
     * Aging Implementation in Original PriorityQueue:
     * PriorityQueue does not support decreaseKey. Mutating elements in-place
     * corrupts the heap. To promote starving patients, the entire heap
     * MUST be extracted into a list, modified, and completely re-heapified!
     * Complexity: O(n log n) + O(n) memory allocations!
     */
    public int applyAging(long currentTime, long waitThreshold) {
        int promotedCount = 0;
        List<TriagePatient> tempList = new ArrayList<>(heap.size());

        // Drain entire heap: O(n log n)
        while (!heap.isEmpty()) {
            tempList.add(heap.poll());
        }

        // Apply aging to eligible patients
        for (TriagePatient p : tempList) {
            if (p.severity > 1 && (currentTime - p.arrivalTime) >= waitThreshold) {
                p.severity--; // Promote to higher priority
                promotedCount++;
            }
        }

        // Re-insert all elements into heap: O(n log n)
        heap.addAll(tempList);

        return promotedCount;
    }
}
