package Task01;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Task01 - Q1: Building Text Under Load: String, StringBuilder or StringBuffer?
 * 
 * Compares String concatenation (+), StringBuilder, and StringBuffer across
 * three contrasting workloads in a Railway e-Ticket Generation context.
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class TicketBuilderBenchmark {

    // 50 realistic railway e-ticket fields
    public static final String[] FIELD_NAMES = {
        "PNR", "TrainNo", "TrainName", "SourceStation", "DestStation",
        "JourneyDate", "ClassType", "Quota", "Coach", "BerthSeat",
        "PassengerName", "Age", "Gender", "BookingStatus", "CurrentStatus",
        "BaseFare", "ReservationFee", "SuperfastCharge", "TatkalCharge", "GST",
        "TotalFare", "PaymentID", "TxnDate", "BoardingPoint", "DepartureTime",
        "ArrivalTime", "DistanceKm", "RouteNo", "CateringOption", "IDProofType",
        "IDProofNo", "ConcessionCode", "AgentID", "PNRStatus", "ChartingStatus",
        "PlatformNo", "WLNumber", "CancelPolicyCode", "InsuranceOpted", "InsurancePolicyID",
        "MobileNo", "Email", "EmergencyName", "EmergencyPhone", "BerthPref",
        "MealPref", "SeniorCitizen", "ChildCount", "TotalPassengers", "QRSecurityToken"
    };

    public static final String[] SAMPLE_VALUES = {
        "8421904123", "12626", "Kerala Express", "NDLS", "TVC",
        "2026-12-01", "3A", "GN", "B3", "42-MB",
        "Geetika T", "21", "F", "CONFIRMED", "CNF/B3/42",
        "1850.00", "40.00", "45.00", "0.00", "96.75",
        "2031.75", "PAY-99482710", "2026-11-15 10:14:02", "New Delhi", "20:10",
        "18:00", "3035", "RT-4", "Veg Meal", "Aadhaar",
        "XXXX-XXXX-1469", "NONE", "IRCTC-AG-882", "ACTIVE", "CHART NOT PREPARED",
        "PF-5", "0", "CP-STD-2026", "YES", "POL-8839210",
        "9876543210", "geetika@example.com", "Parent", "9876543211", "MB",
        "VEG", "NO", "0", "1", "AUTH-TOKEN-SHA256-992817203"
    };

    // Approach 1: Naive String concatenation (+) inside a loop
    public static String buildTicketWithString(int fieldCount) {
        String ticket = "";
        for (int i = 0; i < fieldCount; i++) {
            ticket = ticket + FIELD_NAMES[i] + ":" + SAMPLE_VALUES[i] + "; ";
        }
        return ticket;
    }

    // Approach 2: StringBuilder append
    public static String buildTicketWithStringBuilder(int fieldCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldCount; i++) {
            sb.append(FIELD_NAMES[i]).append(':').append(SAMPLE_VALUES[i]).append("; ");
        }
        return sb.toString();
    }

    // Approach 3: StringBuffer append (synchronized)
    public static String buildTicketWithStringBuffer(int fieldCount) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < fieldCount; i++) {
            sb.append(FIELD_NAMES[i]).append(':').append(SAMPLE_VALUES[i]).append("; ");
        }
        return sb.toString();
    }

    // Benchmark Result Container
    static class BenchmarkResult {
        String workloadName;
        int ticketCount;
        int fieldsPerTicket;
        String approach;
        double avgTimeMs;
        long estimatedIntermediateStrings;
        long memoryDeltaBytes;

        BenchmarkResult(String workloadName, int ticketCount, int fieldsPerTicket,
                        String approach, double avgTimeMs,
                        long estimatedIntermediateStrings, long memoryDeltaBytes) {
            this.workloadName = workloadName;
            this.ticketCount = ticketCount;
            this.fieldsPerTicket = fieldsPerTicket;
            this.approach = approach;
            this.avgTimeMs = avgTimeMs;
            this.estimatedIntermediateStrings = estimatedIntermediateStrings;
            this.memoryDeltaBytes = memoryDeltaBytes;
        }
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task01 - Q1: Building Text Under Load: String vs StringBuilder vs StringBuffer");
        System.out.println(" Student: Geetika T | Reg No: 192511469 | Java Version: 21");
        System.out.println("=========================================================================\n");

        List<BenchmarkResult> results = new ArrayList<>();

        // Workload definitions:
        // Workload 1: 1 short ticket with 5 fields (quick SMS receipt)
        // Workload 2: 10,000 tickets with 50 fields each (heavy batch booking)
        // Workload 3: Additional custom workload: 2,000 tickets with 50 fields each (medium burst)
        
        // Warm-up JVM JIT
        System.out.println("Warming up JVM JIT compiler...");
        for (int i = 0; i < 500; i++) {
            buildTicketWithString(10);
            buildTicketWithStringBuilder(10);
            buildTicketWithStringBuffer(10);
        }
        System.out.println("Warm-up complete.\n");

        // Run Workload 1
        runWorkload("Workload 1 (1 Short Ticket, 5 Fields)", 1, 5, 10, results);

        // Run Workload 3 first before heavy workload 2
        runWorkload("Workload 3 (Additional: 2,000 Tickets, 50 Fields)", 2000, 50, 5, results);

        // Run Workload 2
        runWorkload("Workload 2 (10,000 Tickets, 50 Fields)", 10000, 50, 5, results);

        // Print Summary Table
        System.out.println("\n======================================================================================================================");
        System.out.printf("%-45s | %-15s | %-12s | %-15s | %-15s%n",
                "Workload", "Approach", "Avg Time (ms)", "Est. Inter. Str", "Memory Used (KB)");
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        for (BenchmarkResult r : results) {
            System.out.printf("%-45s | %-15s | %12.4f | %15d | %15.2f%n",
                    r.workloadName, r.approach, r.avgTimeMs, r.estimatedIntermediateStrings, r.memoryDeltaBytes / 1024.0);
        }
        System.out.println("======================================================================================================================\n");

        // Export to CSV
        saveResultsToCsv(results, "ExperimentalData/q1_string_benchmark.csv");
    }

    private static void runWorkload(String workloadName, int ticketCount, int fieldsPerTicket,
                                    int trials, List<BenchmarkResult> resultsList) {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Running: " + workloadName);
        System.out.println("Tickets: " + ticketCount + " | Fields per ticket: " + fieldsPerTicket + " | Trials: " + trials);
        System.out.println("-------------------------------------------------------------------------");

        // 1. String (+)
        double stringAvgTimeMs = 0;
        long stringMemDelta = 0;
        long stringInterStrings = (long) ticketCount * fieldsPerTicket * 2; // Each loop does 2 concatenations: name:val and ticket+elem
        {
            long totalNanos = 0;
            for (int t = 0; t < trials; t++) {
                forceGc();
                long memBefore = getUsedMemory();
                long start = System.nanoTime();
                for (int i = 0; i < ticketCount; i++) {
                    String res = buildTicketWithString(fieldsPerTicket);
                    if (res.length() == 0) System.out.print(""); // Prevent dead-code elimination
                }
                long duration = System.nanoTime() - start;
                long memAfter = getUsedMemory();
                totalNanos += duration;
                if (t == 0) stringMemDelta = Math.max(0, memAfter - memBefore);
            }
            stringAvgTimeMs = (totalNanos / (double) trials) / 1_000_000.0;
            System.out.printf("  [String (+)]        Avg Time: %10.4f ms | Est Inter. Strings: %10d%n",
                    stringAvgTimeMs, stringInterStrings);
            resultsList.add(new BenchmarkResult(workloadName, ticketCount, fieldsPerTicket,
                    "String (+)", stringAvgTimeMs, stringInterStrings, stringMemDelta));
        }

        // 2. StringBuilder
        double sbAvgTimeMs = 0;
        long sbMemDelta = 0;
        long sbInterStrings = ticketCount; // Only final toString() per ticket
        {
            long totalNanos = 0;
            for (int t = 0; t < trials; t++) {
                forceGc();
                long memBefore = getUsedMemory();
                long start = System.nanoTime();
                for (int i = 0; i < ticketCount; i++) {
                    String res = buildTicketWithStringBuilder(fieldsPerTicket);
                    if (res.length() == 0) System.out.print("");
                }
                long duration = System.nanoTime() - start;
                long memAfter = getUsedMemory();
                totalNanos += duration;
                if (t == 0) sbMemDelta = Math.max(0, memAfter - memBefore);
            }
            sbAvgTimeMs = (totalNanos / (double) trials) / 1_000_000.0;
            System.out.printf("  [StringBuilder]     Avg Time: %10.4f ms | Est Inter. Strings: %10d%n",
                    sbAvgTimeMs, sbInterStrings);
            resultsList.add(new BenchmarkResult(workloadName, ticketCount, fieldsPerTicket,
                    "StringBuilder", sbAvgTimeMs, sbInterStrings, sbMemDelta));
        }

        // 3. StringBuffer
        double sbufAvgTimeMs = 0;
        long sbufMemDelta = 0;
        long sbufInterStrings = ticketCount; // Only final toString() per ticket
        {
            long totalNanos = 0;
            for (int t = 0; t < trials; t++) {
                forceGc();
                long memBefore = getUsedMemory();
                long start = System.nanoTime();
                for (int i = 0; i < ticketCount; i++) {
                    String res = buildTicketWithStringBuffer(fieldsPerTicket);
                    if (res.length() == 0) System.out.print("");
                }
                long duration = System.nanoTime() - start;
                long memAfter = getUsedMemory();
                totalNanos += duration;
                if (t == 0) sbufMemDelta = Math.max(0, memAfter - memBefore);
            }
            sbufAvgTimeMs = (totalNanos / (double) trials) / 1_000_000.0;
            System.out.printf("  [StringBuffer]      Avg Time: %10.4f ms | Est Inter. Strings: %10d%n",
                    sbufAvgTimeMs, sbufInterStrings);
            resultsList.add(new BenchmarkResult(workloadName, ticketCount, fieldsPerTicket,
                    "StringBuffer", sbufAvgTimeMs, sbufInterStrings, sbufMemDelta));
        }
        System.out.println();
    }

    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static void forceGc() {
        System.gc();
        try {
            Thread.sleep(20);
        } catch (InterruptedException ignored) {}
    }

    private static void saveResultsToCsv(List<BenchmarkResult> results, String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("Workload,TicketCount,FieldsPerTicket,Approach,AvgTimeMs,EstimatedIntermediateStrings,MemoryUsedBytes");
            for (BenchmarkResult r : results) {
                pw.printf("\"%s\",%d,%d,\"%s\",%.4f,%d,%d%n",
                        r.workloadName, r.ticketCount, r.fieldsPerTicket, r.approach,
                        r.avgTimeMs, r.estimatedIntermediateStrings, r.memoryDeltaBytes);
            }
            System.out.println("Experimental data successfully saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving CSV: " + e.getMessage());
        }
    }
}
