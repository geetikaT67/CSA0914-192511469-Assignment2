package Task01;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Task01 - Alternative Concept & Complexity Analysis
 * 
 * Compares StringBuilder with:
 *  1. String.join()
 *  2. StringJoiner
 *  3. String.format()
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class AlternativeTicketFormats {

    // Approach A: String.join()
    public static String buildWithJoin(String[] fieldNames, String[] values, int count) {
        String[] pairs = new String[count];
        for (int i = 0; i < count; i++) {
            pairs[i] = fieldNames[i] + ":" + values[i];
        }
        return String.join("; ", pairs) + "; ";
    }

    // Approach B: StringJoiner
    public static String buildWithStringJoiner(String[] fieldNames, String[] values, int count) {
        StringJoiner sj = new StringJoiner("; ", "", "; ");
        for (int i = 0; i < count; i++) {
            sj.add(fieldNames[i] + ":" + values[i]);
        }
        return sj.toString();
    }

    // Approach C: String.format()
    public static String buildWithStringFormat(String[] fieldNames, String[] values, int count) {
        // String.format parses format specifiers at runtime
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(String.format("%s:%s; ", fieldNames[i], values[i]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task01: Alternative Ticket Formatting Approaches Benchmark");
        System.out.println("=========================================================================\n");

        int count = 50;
        String[] names = TicketBuilderBenchmark.FIELD_NAMES;
        String[] vals = TicketBuilderBenchmark.SAMPLE_VALUES;

        // Verify output equivalence
        String outJoin = buildWithJoin(names, vals, count);
        String outJoiner = buildWithStringJoiner(names, vals, count);
        String outFormat = buildWithStringFormat(names, vals, count);

        System.out.println("Sample Output (first 80 chars):");
        System.out.println("String.join()   : " + outJoin.substring(0, 80) + "...");
        System.out.println("StringJoiner    : " + outJoiner.substring(0, 80) + "...");
        System.out.println("String.format() : " + outFormat.substring(0, 80) + "...\n");

        int iterations = 10000;
        System.out.println("Benchmarking " + iterations + " tickets (50 fields each)...");

        // Warm up
        for (int i = 0; i < 500; i++) {
            buildWithJoin(names, vals, count);
            buildWithStringJoiner(names, vals, count);
            buildWithStringFormat(names, vals, count);
        }

        // Benchmark String.join()
        long startJoin = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            buildWithJoin(names, vals, count);
        }
        double timeJoin = (System.nanoTime() - startJoin) / 1_000_000.0;

        // Benchmark StringJoiner
        long startJoiner = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            buildWithStringJoiner(names, vals, count);
        }
        double timeJoiner = (System.nanoTime() - startJoiner) / 1_000_000.0;

        // Benchmark String.format()
        long startFormat = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            buildWithStringFormat(names, vals, count);
        }
        double timeFormat = (System.nanoTime() - startFormat) / 1_000_000.0;

        // Benchmark StringBuilder baseline
        long startSb = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            TicketBuilderBenchmark.buildTicketWithStringBuilder(count);
        }
        double timeSb = (System.nanoTime() - startSb) / 1_000_000.0;

        System.out.println("\n--- Performance Comparison (10,000 tickets, 50 fields each) ---");
        System.out.printf("%-20s : %10.2f ms (Relative: %.2fx)%n", "StringBuilder (Baseline)", timeSb, 1.0);
        System.out.printf("%-20s : %10.2f ms (Relative: %.2fx)%n", "StringJoiner", timeJoiner, timeJoiner / timeSb);
        System.out.printf("%-20s : %10.2f ms (Relative: %.2fx)%n", "String.join()", timeJoin, timeJoin / timeSb);
        System.out.printf("%-20s : %10.2f ms (Relative: %.2fx)%n", "String.format()", timeFormat, timeFormat / timeSb);

        System.out.println("\n--- Complexity & Architectural Analysis ---");
        System.out.println("1. Repeated '+' Concatenation: O(n^2) time complexity.");
        System.out.println("   Derivation: Concatenating n fields of length L copies L, 2L, 3L... nL characters.");
        System.out.println("   Total characters copied = L * n(n+1)/2 = O(n^2). Creates n intermediate strings.");
        System.out.println("2. StringBuilder: O(n) amortized time complexity.");
        System.out.println("   Derivation: Appends characters directly into an internal char/byte buffer.");
        System.out.println("   Buffer doubles geometrically when full (16 -> 34 -> 70 -> ...), copying total O(n) characters.");
        System.out.println("   Creates 0 intermediate String objects; only 1 final String upon toString().");
        System.out.println("3. StringJoiner / String.join(): O(n) time complexity.");
        System.out.println("   Internally backed by StringBuilder; slightly slower due to delimiter management overhead.");
        System.out.println("4. String.format(): O(n) time complexity but high constant factor (10x-25x slower).");
        System.out.println("   Parses regex/format specifier strings (%s) at runtime, creates Varargs arrays and Formatter instances.");
        System.out.println("=========================================================================");
    }
}
