package Task04;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Task04 - Alternative Concept: Sorted Array with Arrays.binarySearch()
 * 
 * Implements an inventory using a contiguous sorted array:
 *  - Lookup: Arrays.binarySearch() -> O(log n)
 *  - Insertion: Binary search for index + System.arraycopy shift -> O(n)
 *  - Code Order Report: Direct linear traversal -> O(n) with 0 memory allocation!
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class SortedArrayInventory {

    private Medicine[] data;
    private int size;

    public SortedArrayInventory(int capacity) {
        this.data = new Medicine[capacity];
        this.size = 0;
    }

    public int size() { return size; }

    // O(log n) lookup using binary search
    public Medicine get(String code) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = data[mid].getCode().compareTo(code);
            if (cmp < 0) low = mid + 1;
            else if (cmp > 0) high = mid - 1;
            else return data[mid];
        }
        return null;
    }

    // O(n) insertion maintaining sorted order
    public void put(Medicine medicine) {
        int low = 0;
        int high = size - 1;
        int insertIdx = size;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = data[mid].getCode().compareTo(medicine.getCode());
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                // Key exists: replace in-place O(1)
                data[mid] = medicine;
                return;
            }
        }
        insertIdx = low;

        // Ensure capacity
        if (size == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }

        // Shift elements to make room: O(n)
        System.arraycopy(data, insertIdx, data, insertIdx + 1, size - insertIdx);
        data[insertIdx] = medicine;
        size++;
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task04: Alternative Concept - Sorted Array with Arrays.binarySearch()");
        System.out.println("=========================================================================\n");

        int size = 10000;
        SortedArrayInventory inv = new SortedArrayInventory(size + 10);

        System.out.println("Inserting " + size + " medicines into sorted array...");
        long startInsert = System.nanoTime();
        for (int i = 0; i < size; i++) {
            String code = String.format("MED-%06d", (i * 37 + 101) % 1_000_000);
            inv.put(new Medicine(code, "Drug-" + i, 100, 25.0));
        }
        double insertTimeMs = (System.nanoTime() - startInsert) / 1_000_000.0;
        System.out.printf("Time to insert %d items (O(n^2) total due to shifting): %.2f ms%n", size, insertTimeMs);

        // 100,000 lookups via binary search
        int lookups = 100000;
        String testKey = String.format("MED-%06d", (500 * 37 + 101) % 1_000_000);
        long startLookup = System.nanoTime();
        for (int i = 0; i < lookups; i++) {
            Medicine m = inv.get(testKey);
            if (m == null) System.out.print("");
        }
        double lookupTimeMs = (System.nanoTime() - startLookup) / 1_000_000.0;
        System.out.printf("Time for %d binary search lookups (O(log n)): %.2f ms%n", lookups, lookupTimeMs);

        // Code Order Report
        long startReport = System.nanoTime();
        int count = 0;
        for (int i = 0; i < inv.size(); i++) {
            if (inv.data[i] != null) count++;
        }
        double reportTimeMs = (System.nanoTime() - startReport) / 1_000_000.0;
        System.out.printf("Time for Code-Order Report (Sequential array scan): %.4f ms (0 extra memory!)%n%n", reportTimeMs);

        System.out.println("--- Architectural & Complexity Comparison ---");
        System.out.println("Metric                 | HashMap             | TreeMap             | Sorted Array");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Lookup (Get)           | O(1) average        | O(log n) tree       | O(log n) binary search");
        System.out.println("Insertion (Put)        | O(1) average        | O(log n) tree       | O(n) array shift");
        System.out.println("Code-Order Report      | O(n log n) [sort]   | O(n) [in-order]     | O(n) [direct scan]");
        System.out.println("Insertion-Order Report | Impossible directly | Impossible directly | Impossible directly");
        System.out.println("Memory Footprint       | ~32 bytes/entry     | ~40 bytes/node      | 4-8 bytes/slot (compact)");
        System.out.println("Cache Locality         | Poor (pointer hops) | Poor (pointer hops) | Excellent (contiguous)");
        System.out.println("=========================================================================");
    }
}
