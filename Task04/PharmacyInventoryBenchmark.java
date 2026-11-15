package Task04;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Task04 - Q4: Choosing a Map for a Pharmacy Inventory
 * 
 * Compares HashMap, TreeMap, and LinkedHashMap across three inventory sizes:
 *  - 100,000 lookups time
 *  - Time to produce Code-Order Report (Sorted)
 *  - Time to produce Insertion-Order Report
 *  - Order correctness verification
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class PharmacyInventoryBenchmark {

    static class MapMetric {
        int inventorySize;
        String mapType;
        double lookupTimeMs;
        double codeOrderReportTimeMs;
        boolean codeOrderNaturallyCorrect;
        double insertionOrderReportTimeMs;
        boolean insertionOrderNaturallyCorrect;

        MapMetric(int size, String mapType, double lookupTimeMs,
                  double codeOrderTime, boolean codeCorrect,
                  double insertOrderTime, boolean insertCorrect) {
            this.inventorySize = size;
            this.mapType = mapType;
            this.lookupTimeMs = lookupTimeMs;
            this.codeOrderReportTimeMs = codeOrderTime;
            this.codeOrderNaturallyCorrect = codeCorrect;
            this.insertionOrderReportTimeMs = insertOrderTime;
            this.insertionOrderNaturallyCorrect = insertCorrect;
        }
    }

    private static Medicine createMedicine(int id) {
        // Generating non-sequential random-looking codes to test sorting
        String code = String.format("MED-%06d", (id * 37 + 101) % 1_000_000);
        String name = "PharmaDrug-" + id;
        int stock = 50 + (id % 500);
        double price = 10.0 + (id % 150) * 2.5;
        return new Medicine(code, name, stock, price);
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task04 - Q4: Pharmacy Inventory: HashMap vs TreeMap vs LinkedHashMap");
        System.out.println(" Student: Geetika T | Reg No: 192511469 | Java Version: 21");
        System.out.println("=========================================================================\n");

        int[] sizes = { 1000, 10000, 100000 };
        int lookupCount = 100000;
        List<MapMetric> metricsList = new ArrayList<>();

        // Warm up JIT
        System.out.println("Warming up JIT compiler...");
        for (int i = 0; i < 500; i++) {
            Map<String, Medicine> hm = new HashMap<>();
            Map<String, Medicine> tm = new TreeMap<>();
            Medicine m = createMedicine(i);
            hm.put(m.getCode(), m);
            tm.put(m.getCode(), m);
            hm.get(m.getCode());
            tm.get(m.getCode());
        }
        System.out.println("Warm up complete.\n");

        for (int size : sizes) {
            System.out.println("=========================================================================");
            System.out.println(" BENCHMARKING INVENTORY SIZE: " + size + " MEDICINES");
            System.out.println("=========================================================================");

            // Generate source medicines in insertion order
            List<Medicine> medicineList = new ArrayList<>(size);
            List<String> lookupKeys = new ArrayList<>(lookupCount);
            Random rng = new Random(192511469);

            for (int i = 0; i < size; i++) {
                medicineList.add(createMedicine(i));
            }
            for (int i = 0; i < lookupCount; i++) {
                lookupKeys.add(medicineList.get(rng.nextInt(size)).getCode());
            }

            // 1. HashMap
            {
                Map<String, Medicine> map = new HashMap<>(size * 2);
                for (Medicine m : medicineList) map.put(m.getCode(), m);

                // 100,000 lookups
                long startLookup = System.nanoTime();
                for (String k : lookupKeys) {
                    Medicine m = map.get(k);
                    if (m == null) System.out.print("");
                }
                double lookupTime = (System.nanoTime() - startLookup) / 1_000_000.0;

                // Report 1: Code Order (Requires extra work for HashMap!)
                long startCodeReport = System.nanoTime();
                List<Map.Entry<String, Medicine>> entries = new ArrayList<>(map.entrySet());
                entries.sort(Map.Entry.comparingByKey());
                double codeReportTime = (System.nanoTime() - startCodeReport) / 1_000_000.0;

                // Check natural iteration order for HashMap
                boolean naturalCodeOrder = isSortedByCode(map.keySet());
                boolean naturalInsertOrder = isInsertionOrdered(map.values(), medicineList);

                // Report 2: Insertion Order (Cannot naturally reproduce without external log)
                double insertReportTime = 0.0; // N/A for pure HashMap

                metricsList.add(new MapMetric(size, "HashMap", lookupTime, codeReportTime,
                        naturalCodeOrder, insertReportTime, naturalInsertOrder));
                System.out.printf("  %-15s | 100k Lookups: %8.2f ms | Code Report (Sorted): %8.2f ms | Nat Code: %b | Nat Insert: %b%n",
                        "HashMap", lookupTime, codeReportTime, naturalCodeOrder, naturalInsertOrder);
            }

            // 2. TreeMap
            {
                Map<String, Medicine> map = new TreeMap<>();
                for (Medicine m : medicineList) map.put(m.getCode(), m);

                // 100,000 lookups
                long startLookup = System.nanoTime();
                for (String k : lookupKeys) {
                    Medicine m = map.get(k);
                    if (m == null) System.out.print("");
                }
                double lookupTime = (System.nanoTime() - startLookup) / 1_000_000.0;

                // Report 1: Code Order (Naturally sorted!)
                long startCodeReport = System.nanoTime();
                int count = 0;
                for (Map.Entry<String, Medicine> entry : map.entrySet()) {
                    if (entry.getValue() != null) count++;
                }
                double codeReportTime = (System.nanoTime() - startCodeReport) / 1_000_000.0;

                boolean naturalCodeOrder = isSortedByCode(map.keySet());
                boolean naturalInsertOrder = isInsertionOrdered(map.values(), medicineList);

                metricsList.add(new MapMetric(size, "TreeMap", lookupTime, codeReportTime,
                        naturalCodeOrder, 0.0, naturalInsertOrder));
                System.out.printf("  %-15s | 100k Lookups: %8.2f ms | Code Report (Sorted): %8.2f ms | Nat Code: %b | Nat Insert: %b%n",
                        "TreeMap", lookupTime, codeReportTime, naturalCodeOrder, naturalInsertOrder);
            }

            // 3. LinkedHashMap
            {
                Map<String, Medicine> map = new LinkedHashMap<>(size * 2);
                for (Medicine m : medicineList) map.put(m.getCode(), m);

                // 100,000 lookups
                long startLookup = System.nanoTime();
                for (String k : lookupKeys) {
                    Medicine m = map.get(k);
                    if (m == null) System.out.print("");
                }
                double lookupTime = (System.nanoTime() - startLookup) / 1_000_000.0;

                // Report 1: Code Order (Requires sorting copy)
                long startCodeReport = System.nanoTime();
                List<Map.Entry<String, Medicine>> entries = new ArrayList<>(map.entrySet());
                entries.sort(Map.Entry.comparingByKey());
                double codeReportTime = (System.nanoTime() - startCodeReport) / 1_000_000.0;

                // Report 2: Insertion Order (Naturally preserved!)
                long startInsertReport = System.nanoTime();
                int count = 0;
                for (Map.Entry<String, Medicine> entry : map.entrySet()) {
                    if (entry.getValue() != null) count++;
                }
                double insertReportTime = (System.nanoTime() - startInsertReport) / 1_000_000.0;

                boolean naturalCodeOrder = isSortedByCode(map.keySet());
                boolean naturalInsertOrder = isInsertionOrdered(map.values(), medicineList);

                metricsList.add(new MapMetric(size, "LinkedHashMap", lookupTime, codeReportTime,
                        naturalCodeOrder, insertReportTime, naturalInsertOrder));
                System.out.printf("  %-15s | 100k Lookups: %8.2f ms | Code Report (Sorted): %8.2f ms | Nat Code: %b | Nat Insert: %b%n",
                        "LinkedHashMap", lookupTime, codeReportTime, naturalCodeOrder, naturalInsertOrder);
            }
            System.out.println();
        }

        saveToCsv(metricsList, "ExperimentalData/q4_pharmacy_benchmark.csv");
    }

    private static boolean isSortedByCode(Collection<String> keys) {
        String prev = null;
        for (String k : keys) {
            if (prev != null && prev.compareTo(k) > 0) return false;
            prev = k;
        }
        return true;
    }

    private static boolean isInsertionOrdered(Collection<Medicine> actual, List<Medicine> expected) {
        int idx = 0;
        for (Medicine m : actual) {
            if (!m.equals(expected.get(idx++))) return false;
        }
        return true;
    }

    private static void saveToCsv(List<MapMetric> metrics, String path) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("InventorySize,MapType,100kLookupTimeMs,CodeOrderReportTimeMs,NaturallyCodeOrdered,InsertionOrderReportTimeMs,NaturallyInsertionOrdered");
            for (MapMetric m : metrics) {
                pw.printf("%d,\"%s\",%.2f,%.2f,%b,%.2f,%b%n",
                        m.inventorySize, m.mapType, m.lookupTimeMs, m.codeOrderReportTimeMs,
                        m.codeOrderNaturallyCorrect, m.insertionOrderReportTimeMs, m.insertionOrderNaturallyCorrect);
            }
            System.out.println("Experimental data successfully written to: " + path);
        } catch (IOException e) {
            System.err.println("Error saving CSV: " + e.getMessage());
        }
    }
}
