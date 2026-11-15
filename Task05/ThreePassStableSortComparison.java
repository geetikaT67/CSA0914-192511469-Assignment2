package Task05;

import java.util.*;

/**
 * Task05 - Alternative Concept: Comparator Chain vs 3-Pass Stable Sort
 * 
 * Demonstrates:
 *  1. Single-pass sort using chained Comparator: 1 * O(n log n)
 *  2. Three-pass stable sort (name -> goalDiff -> points): 3 * O(n log n)
 *  3. Comparable<Team> approach and why Comparator is superior for reusability
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class ThreePassStableSortComparison {

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task05: Comparator Chain vs 3-Pass Stable Sort Analysis");
        System.out.println("=========================================================================\n");

        int n = 50000;
        List<Team> dataset1 = new ArrayList<>(n);
        List<Team> dataset2 = new ArrayList<>(n);

        Random rng = new Random(42);
        for (int i = 0; i < n; i++) {
            String name = "Team-" + (i % 1000);
            int pts = rng.nextInt(30);
            int gd = rng.nextInt(40) - 20;
            Team t = new Team(name, pts, gd);
            dataset1.add(t);
            dataset2.add(t);
        }

        System.out.println("Dataset: " + n + " teams.");

        // Method 1: Single-pass sort with composite Comparator chain
        long start1 = System.nanoTime();
        Comparator<Team> compositeComparator = Comparator
                .comparingInt((Team t) -> t.points).reversed()
                .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed())
                .thenComparing(t -> t.name);
        dataset1.sort(compositeComparator);
        double time1 = (System.nanoTime() - start1) / 1_000_000.0;
        System.out.printf("1. Single-Pass Composite Comparator Sort : %8.2f ms (1 * O(n log n))%n", time1);

        // Method 2: Three-pass stable sort (TimSort)
        // Must sort in REVERSE order of criteria significance:
        // Pass 1: Least significant (Name A-Z)
        // Pass 2: Medium significant (Goal Diff high first)
        // Pass 3: Most significant (Points high first)
        long start2 = System.nanoTime();
        dataset2.sort(Comparator.comparing(t -> t.name));
        dataset2.sort(Comparator.comparingInt((Team t) -> t.goalDiff).reversed());
        dataset2.sort(Comparator.comparingInt((Team t) -> t.points).reversed());
        double time2 = (System.nanoTime() - start2) / 1_000_000.0;
        System.out.printf("2. Three-Pass Stable Sort (TimSort)     : %8.2f ms (3 * O(n log n))%n", time2);

        // Verify mathematical equivalence of outcomes
        boolean identical = true;
        for (int i = 0; i < n; i++) {
            if (!dataset1.get(i).toString().equals(dataset2.get(i).toString())) {
                identical = false;
                break;
            }
        }
        System.out.println("Are both sorted lists 100% identically ordered? " + identical);

        System.out.println("\n--- Complexity & Design Analysis ---");
        System.out.println("Property               | Composite Comparator Chain  | Three-Pass Stable Sort");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Time Complexity        | 1 * O(n log n)              | 3 * O(n log n)");
        System.out.println("Sort Passes            | 1 pass                      | 3 full passes");
        System.out.println("Relative Wall Clock    | 1.0x (Fastest)              | ~2.5x to 3.0x slower");
        System.out.println("Dependency on Stability| Works with any sort (stable/ | Strictly requires a STABLE sort");
        System.out.println("                       | unstable like Dual-Pivot)   | (fails on unstable quicksort!)");
        System.out.println("Reusability            | Highly decoupled: can rank  | Requires hardcoded multi-pass");
        System.out.println("                       | Players, Colleges, Teams    | sorting boilerplate");
        System.out.println("=========================================================================");
    }
}
