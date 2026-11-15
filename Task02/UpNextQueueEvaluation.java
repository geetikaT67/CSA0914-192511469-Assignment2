package Task02;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Task02 - Changed Condition: Up Next Queue Evaluation
 * 
 * Re-evaluates ArrayList vs LinkedList vs ArrayDeque when songs are frequently:
 *  - Added to the front (play next / priority user override)
 *  - Added to the end (queue next song)
 *  - Removed from the front (song finishes, next song plays)
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class UpNextQueueEvaluation {

    private static Song createSong(int id) {
        return new Song("UPNEXT-" + id, "Track " + id, "Artist " + id, 210);
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task02: Changed Condition - 'Up Next' Double-Ended Queue Evaluation");
        System.out.println(" Comparing ArrayList, LinkedList, and ArrayDeque");
        System.out.println("=========================================================================\n");

        int operations = 50000;
        int trials = 5;
        System.out.println("Simulating " + operations + " streaming playback operations:");
        System.out.println(" - 40% addLast (User enqueues song at tail)");
        System.out.println(" - 20% addFirst (User hits 'Play Next' at head)");
        System.out.println(" - 40% pollFirst (Player consumes next track)\n");

        // 1. ArrayDeque
        double adTimeMs = measurePerformance(trials, () -> {
            ArrayDeque<Song> queue = new ArrayDeque<>();
            for (int i = 0; i < operations; i++) {
                int op = i % 5;
                if (op < 2) {
                    queue.addLast(createSong(i));
                } else if (op == 2) {
                    queue.addFirst(createSong(i));
                } else {
                    if (!queue.isEmpty()) queue.pollFirst();
                }
            }
        });

        // 2. LinkedList
        double llTimeMs = measurePerformance(trials, () -> {
            LinkedList<Song> queue = new LinkedList<>();
            for (int i = 0; i < operations; i++) {
                int op = i % 5;
                if (op < 2) {
                    queue.addLast(createSong(i));
                } else if (op == 2) {
                    queue.addFirst(createSong(i));
                } else {
                    if (!queue.isEmpty()) queue.pollFirst();
                }
            }
        });

        // 3. ArrayList (Emulating double-ended queue)
        double alTimeMs = measurePerformance(trials, () -> {
            ArrayList<Song> queue = new ArrayList<>();
            for (int i = 0; i < operations; i++) {
                int op = i % 5;
                if (op < 2) {
                    queue.add(createSong(i));
                } else if (op == 2) {
                    queue.add(0, createSong(i)); // Expensive shift!
                } else {
                    if (!queue.isEmpty()) queue.remove(0); // Expensive shift!
                }
            }
        });

        System.out.println("--- Benchmark Results (Averaged over " + trials + " trials) ---");
        System.out.printf("%-20s : %10.4f ms (1.0x Baseline)%n", "ArrayDeque", adTimeMs);
        System.out.printf("%-20s : %10.4f ms (%.2fx slower than ArrayDeque)%n", "LinkedList", llTimeMs, llTimeMs / adTimeMs);
        System.out.printf("%-20s : %10.4f ms (%.2fx slower than ArrayDeque)%n", "ArrayList", alTimeMs, alTimeMs / adTimeMs);

        System.out.println("\n--- Architectural Analysis ---");
        System.out.println("1. ArrayDeque is backed by a circular array buffer (head & tail indices).");
        System.out.println("   Both addFirst/pollFirst and addLast/pollLast run in amortized O(1) time");
        System.out.println("   with ZERO node allocations and contiguous CPU cache locality.");
        System.out.println("2. LinkedList also achieves O(1) head/tail operations, but allocates a 24-32 byte");
        System.out.println("   Node object for every single song added, creating heavy GC churn and pointer chasing.");
        System.out.println("3. ArrayList degrades severely because add(0) and remove(0) require shifting ALL");
        System.out.println("   remaining elements by 1 position (O(n) per operation, leading to O(n^2) total work).");
        System.out.println(">> CONCLUSION: ArrayDeque is the definitively superior engineering choice for 'Up Next'.");
        System.out.println("=========================================================================");
    }

    private static double measurePerformance(int trials, Runnable task) {
        long totalNanos = 0;
        for (int t = 0; t < trials; t++) {
            System.gc();
            long start = System.nanoTime();
            task.run();
            totalNanos += (System.nanoTime() - start);
        }
        return (totalNanos / (double) trials) / 1_000_000.0;
    }
}
