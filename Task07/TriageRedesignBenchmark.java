package Task07;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Task07 - Day-4 Design Challenge: Empirical Validation & Comparison
 * 
 * Compares Original Solution (PriorityQueue Binary Heap) vs
 * Redesigned Solution (Multi-Level Bucket ArrayDeque Triage)
 * under identical conditions across 3 workload sizes.
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class TriageRedesignBenchmark {

    static class RedesignMetric {
        int patientCount;
        String systemType;
        double admitTimeMs;
        double pollTimeMs;
        double agingTimeMs;
        int promotedPatients;
        long memoryUsedBytes;

        RedesignMetric(int count, String systemType, double admitTimeMs,
                       double pollTimeMs, double agingTimeMs,
                       int promotedPatients, long memoryUsedBytes) {
            this.patientCount = count;
            this.systemType = systemType;
            this.admitTimeMs = admitTimeMs;
            this.pollTimeMs = pollTimeMs;
            this.agingTimeMs = agingTimeMs;
            this.promotedPatients = promotedPatients;
            this.memoryUsedBytes = memoryUsedBytes;
        }
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task07 - Q7: Day-4 Design Challenge: Diagnose, Redesign and Validate");
        System.out.println(" Hospital Emergency Triage: PriorityQueue vs Multi-Level Bucket Queue");
        System.out.println(" Student: Geetika T | Reg No: 192511469 | Java Version: 21");
        System.out.println("=========================================================================\n");

        int[] sizes = { 1000, 10000, 100000 };
        List<RedesignMetric> metrics = new ArrayList<>();

        // Warm up JIT
        System.out.println("Warming up JIT compiler...");
        for (int i = 0; i < 500; i++) {
            OriginalTriageSystem o = new OriginalTriageSystem();
            RedesignedTriageSystem r = new RedesignedTriageSystem();
            o.admit("P", 3, 0);
            r.admit("P", 3, 0);
            o.nextPatient();
            r.nextPatient();
        }
        System.out.println("Warm up complete.\n");

        for (int size : sizes) {
            System.out.println("=========================================================================");
            System.out.println(" BENCHMARKING WORKLOAD: " + size + " PATIENTS");
            System.out.println("=========================================================================");

            Random rng = new Random(192511469);
            int[] severities = new int[size];
            long[] arrivalTimes = new long[size];
            for (int i = 0; i < size; i++) {
                severities[i] = 1 + rng.nextInt(5);
                arrivalTimes[i] = i * 2; // Arriving every 2 seconds
            }
            long simulationEndTime = size * 2L + 100L;
            long agingThreshold = (size * 2L) / 3; // Oldest 33% of patients qualify for aging

            // 1. Original PriorityQueue System
            {
                forceGc();
                long memBefore = getUsedMemory();
                OriginalTriageSystem original = new OriginalTriageSystem();

                // Measure Admission
                long startAdmit = System.nanoTime();
                for (int i = 0; i < size; i++) {
                    original.admit("Patient-" + i, severities[i], arrivalTimes[i]);
                }
                double admitTimeMs = (System.nanoTime() - startAdmit) / 1_000_000.0;

                // Measure Aging Cycle (Starvation Prevention)
                long startAging = System.nanoTime();
                int promoted = original.applyAging(simulationEndTime, agingThreshold);
                double agingTimeMs = (System.nanoTime() - startAging) / 1_000_000.0;

                // Measure Next Patient (Polling entire queue)
                long startPoll = System.nanoTime();
                while (original.size() > 0) {
                    original.nextPatient();
                }
                double pollTimeMs = (System.nanoTime() - startPoll) / 1_000_000.0;

                long memAfter = getUsedMemory();
                long memUsed = Math.max(0, memAfter - memBefore);

                metrics.add(new RedesignMetric(size, "Original (PriorityQueue)",
                        admitTimeMs, pollTimeMs, agingTimeMs, promoted, memUsed));

                System.out.printf("  [Original PriorityQueue]     Admit: %8.2f ms | Poll: %8.2f ms | Aging: %8.2f ms (Promoted: %d)%n",
                        admitTimeMs, pollTimeMs, agingTimeMs, promoted);
            }

            // 2. Redesigned Multi-Level Bucket System
            {
                forceGc();
                long memBefore = getUsedMemory();
                RedesignedTriageSystem redesigned = new RedesignedTriageSystem();

                // Measure Admission
                long startAdmit = System.nanoTime();
                for (int i = 0; i < size; i++) {
                    redesigned.admit("Patient-" + i, severities[i], arrivalTimes[i]);
                }
                double admitTimeMs = (System.nanoTime() - startAdmit) / 1_000_000.0;

                // Measure Aging Cycle (Starvation Prevention)
                long startAging = System.nanoTime();
                int promoted = redesigned.applyAging(simulationEndTime, agingThreshold);
                double agingTimeMs = (System.nanoTime() - startAging) / 1_000_000.0;

                // Measure Next Patient (Polling entire queue)
                long startPoll = System.nanoTime();
                while (redesigned.size() > 0) {
                    redesigned.nextPatient();
                }
                double pollTimeMs = (System.nanoTime() - startPoll) / 1_000_000.0;

                long memAfter = getUsedMemory();
                long memUsed = Math.max(0, memAfter - memBefore);

                metrics.add(new RedesignMetric(size, "Redesigned (MultiLevelQueue)",
                        admitTimeMs, pollTimeMs, agingTimeMs, promoted, memUsed));

                System.out.printf("  [Redesigned Multi-Level]     Admit: %8.2f ms | Poll: %8.2f ms | Aging: %8.2f ms (Promoted: %d)%n",
                        admitTimeMs, pollTimeMs, agingTimeMs, promoted);
            }
            System.out.println();
        }

        // Print Summary Comparison Table
        System.out.println("======================================================================================================================");
        System.out.printf("%-8s | %-28s | %-12s | %-12s | %-14s | %-12s%n",
                "Size", "Architecture", "Admit (ms)", "Poll (ms)", "Aging (ms)", "Promoted");
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        for (RedesignMetric m : metrics) {
            System.out.printf("%-8d | %-28s | %12.2f | %12.2f | %14.2f | %12d%n",
                    m.patientCount, m.systemType, m.admitTimeMs, m.pollTimeMs, m.agingTimeMs, m.promotedPatients);
        }
        System.out.println("======================================================================================================================\n");

        saveToCsv(metrics, "ExperimentalData/q7_redesign_benchmark.csv");
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

    private static void saveToCsv(List<RedesignMetric> metrics, String path) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("PatientCount,Architecture,AdmitTimeMs,PollTimeMs,AgingTimeMs,PromotedPatients,MemoryUsedBytes");
            for (RedesignMetric m : metrics) {
                pw.printf("%d,\"%s\",%.2f,%.2f,%.2f,%d,%d%n",
                        m.patientCount, m.systemType, m.admitTimeMs, m.pollTimeMs,
                        m.agingTimeMs, m.promotedPatients, m.memoryUsedBytes);
            }
            System.out.println("Redesign benchmark data successfully saved to: " + path);
        } catch (IOException e) {
            System.err.println("Error saving CSV: " + e.getMessage());
        }
    }
}
