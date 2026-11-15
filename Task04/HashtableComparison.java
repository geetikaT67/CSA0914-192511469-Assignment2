package Task04;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Task04 - Scalability Inquiry: Why Hashtable is Obsolete in Modern Java
 * 
 * Demonstrates:
 *  1. Synchronization bottleneck: Hashtable synchronizes EVERY single method call,
 *     creating severe thread contention.
 *  2. Null hostility: Hashtable throws NullPointerException on null keys and null values.
 *  3. Modern alternatives: HashMap for single-threaded / thread-confined;
 *     ConcurrentHashMap for high-throughput thread-safe lookups.
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class HashtableComparison {

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task04: Scalability Inquiry - Why Hashtable is Not Preferred in Modern Code");
        System.out.println("=========================================================================\n");

        // 1. Null handling comparison
        System.out.println("--- 1. Null Key & Value Handling ---");
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(null, "Allowed in HashMap");
        hashMap.put("Key1", null);
        System.out.println("HashMap handles null keys and null values cleanly: " + hashMap);

        Hashtable<String, String> hashtable = new Hashtable<>();
        try {
            hashtable.put(null, "Test");
        } catch (NullPointerException e) {
            System.out.println("Hashtable.put(null, value) fails with NullPointerException: " + e.getClass().getSimpleName());
        }
        try {
            hashtable.put("Key1", null);
        } catch (NullPointerException e) {
            System.out.println("Hashtable.put(key, null) fails with NullPointerException: " + e.getClass().getSimpleName());
        }
        System.out.println();

        // 2. Concurrency and Synchronization Benchmark
        System.out.println("--- 2. Single-Thread Throughput (100,000 puts & gets) ---");
        int ops = 100000;

        // Warm up
        for (int i = 0; i < 500; i++) {
            hashMap.put("k" + i, "v" + i);
            hashtable.put("k" + i, "v" + i);
        }

        long startHm = System.nanoTime();
        Map<String, Integer> hm = new HashMap<>(ops * 2);
        for (int i = 0; i < ops; i++) hm.put("MED-" + i, i);
        for (int i = 0; i < ops; i++) hm.get("MED-" + i);
        double hmTime = (System.nanoTime() - startHm) / 1_000_000.0;

        long startHt = System.nanoTime();
        Hashtable<String, Integer> ht = new Hashtable<>(ops * 2);
        for (int i = 0; i < ops; i++) ht.put("MED-" + i, i);
        for (int i = 0; i < ops; i++) ht.get("MED-" + i);
        double htTime = (System.nanoTime() - startHt) / 1_000_000.0;

        System.out.printf("HashMap time   : %8.2f ms%n", hmTime);
        System.out.printf("Hashtable time : %8.2f ms (overhead due to redundant synchronization locks)%n", htTime);

        System.out.println("\n--- Why Hashtable is Deprecated in Modern Architecture ---");
        System.out.println("1. Method-Level Synchronization: Every method in Hashtable is synchronized on 'this'.");
        System.out.println("   Even read operations (get) block concurrent reads and writes, crippling multicore scaling.");
        System.out.println("2. Legacy Inheritance: Extends obsolete Dictionary class rather than clean Collections architecture.");
        System.out.println("3. Modern Replacements:");
        System.out.println("   - Single-threaded / Thread-confined: HashMap (fastest, unsynchronized).");
        System.out.println("   - Concurrent Multi-threaded: ConcurrentHashMap (lock-free volatile reads, bucket-level CAS/striping).");
        System.out.println("=========================================================================");
    }
}
