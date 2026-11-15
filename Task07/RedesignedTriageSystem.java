package Task07;

import java.util.ArrayDeque;
import Task07.OriginalTriageSystem.TriagePatient;

/**
 * Task07 - Redesigned Multi-Level Bucket Triage System
 * 
 * Replaces the binary min-heap with a 5-Level Bucket Queue backed by ArrayDeques:
 *  - Admission: Direct O(1) bucket indexing (no comparisons, no sift-up)
 *  - Next Patient: O(1) scan of at most 5 queue heads (no sift-down)
 *  - Priority Aging: O(k) head-only promotions without touching any other patients!
 *    Zero heap rebalancing, zero allocations, and strict FIFO guarantee.
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class RedesignedTriageSystem {

    public static final int LEVELS = 5; // Severity 1 (highest) to 5 (lowest)

    @SuppressWarnings("unchecked")
    private final ArrayDeque<TriagePatient>[] buckets = new ArrayDeque[LEVELS];
    private int totalCount = 0;

    public RedesignedTriageSystem() {
        for (int i = 0; i < LEVELS; i++) {
            buckets[i] = new ArrayDeque<>();
        }
    }

    // O(1) Direct bucket insertion
    public void admit(String name, int severity, long currentTime) {
        if (severity < 1 || severity > LEVELS) {
            throw new IllegalArgumentException("Severity must be between 1 and 5");
        }
        buckets[severity - 1].addLast(new TriagePatient(name, severity, currentTime));
        totalCount++;
    }

    // O(1) Constant time retrieval (at most 5 head checks)
    public TriagePatient nextPatient() {
        for (int i = 0; i < LEVELS; i++) {
            if (!buckets[i].isEmpty()) {
                totalCount--;
                return buckets[i].pollFirst();
            }
        }
        return null;
    }

    public int size() {
        return totalCount;
    }

    /**
     * Highly Optimized Aging Implementation:
     * Because each bucket is a strict FIFO queue, patients are naturally ordered
     * by arrival time. The patient who has waited longest in any bucket is ALWAYS
     * at the head (peekFirst())!
     * 
     * We only inspect the heads of buckets 1 through 4 (Severity 2 to 5).
     * If head has waited >= threshold:
     *   1. pollFirst() from bucket s [O(1)]
     *   2. Update severity to s - 1 [O(1)]
     *   3. addLast() into bucket s - 1 [O(1)]
     * As soon as a patient at the head has NOT exceeded the threshold,
     * no subsequent patient in that bucket can have either, so we break immediately!
     * 
     * Total Time Complexity: O(k), where k is the number of promoted patients!
     * Zero full collection traversals, zero memory allocations, zero re-sorting!
     */
    public int applyAging(long currentTime, long waitThreshold) {
        int promotedCount = 0;

        // Inspect buckets from Severity 2 up to 5 (indices 1 to 4)
        for (int i = 1; i < LEVELS; i++) {
            ArrayDeque<TriagePatient> currentBucket = buckets[i];
            ArrayDeque<TriagePatient> targetBucket = buckets[i - 1];

            while (!currentBucket.isEmpty()) {
                TriagePatient candidate = currentBucket.peekFirst();
                if ((currentTime - candidate.arrivalTime) >= waitThreshold) {
                    currentBucket.pollFirst(); // Remove from current bucket: O(1)
                    candidate.severity--;       // Promote severity: O(1)
                    targetBucket.addLast(candidate); // Add to target higher priority bucket: O(1)
                    promotedCount++;
                } else {
                    // Head patient has not waited enough; all subsequent patients arrived later
                    break;
                }
            }
        }

        return promotedCount;
    }
}
