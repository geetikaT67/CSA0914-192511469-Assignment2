package Task05;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Task05 - Comprehensive Testing of Generic Leaderboard & Comparators
 * 
 * Tests:
 *  1. Multi-level ties (identical points, identical goalDiff, name ties)
 *  2. Deliberately faulty comparators:
 *     - Faulty Comparator A: Integer subtraction (t2.points - t1.points) leading to overflow/underflow
 *     - Faulty Comparator B: Incomplete comparator ignoring name tie-breaker (violates transitivity/consistency)
 *  3. Generics reuse:
 *     - Ranking Player instances
 *     - Ranking College instances
 *  4. Bounded wildcards: Comparator<? super T> and List<? extends T>
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class GenericLeaderboardTest {

    // Domain Model: Player
    static class Player {
        final String name;
        final int goals;
        final int assists;

        Player(String name, int goals, int assists) {
            this.name = name;
            this.goals = goals;
            this.assists = assists;
        }

        @Override
        public String toString() {
            return name + "(G:" + goals + ",A:" + assists + ")";
        }
    }

    // Domain Model: College
    static class College {
        final String name;
        final int goldMedals;
        final int silverMedals;

        College(String name, int goldMedals, int silverMedals) {
            this.name = name;
            this.goldMedals = goldMedals;
            this.silverMedals = silverMedals;
        }

        @Override
        public String toString() {
            return name + "(Gold:" + goldMedals + ",Silv:" + silverMedals + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task05: In-Depth Comparator Contract & Generic Leaderboard Testing");
        System.out.println(" Student: Geetika T | Reg No: 192511469 | Java Version: 21");
        System.out.println("=========================================================================\n");

        // -------------------------------------------------------------------------
        // 1. Multi-Level Tie Testing
        // -------------------------------------------------------------------------
        System.out.println("--- 1. Testing Data Set with Several Ties at Each Level ---");
        Comparator<Team> correctTeamComparator = Comparator
                .comparingInt((Team t) -> t.points).reversed()
                .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed())
                .thenComparing(t -> t.name);

        Leaderboard<Team> teamBoard = new Leaderboard<>(correctTeamComparator);
        List<Team> complexTeams = Arrays.asList(
                new Team("Wolves", 12, 5),
                new Team("Hawks", 12, 5),   // Tie on points and goalDiff -> Hawks < Wolves
                new Team("Tigers", 10, 8),
                new Team("Lions", 10, 4),   // Same points (10), lower goalDiff (4 vs 8) -> Tigers > Lions
                new Team("Bears", 10, 4),   // Same points (10), same goalDiff (4) -> Bears < Lions
                new Team("Sharks", 6, -2)
        );
        teamBoard.addAll(complexTeams);
        List<Team> rankedComplex = teamBoard.ranking();
        System.out.println("Complex Tie-Breaker Ranking:");
        for (int i = 0; i < rankedComplex.size(); i++) {
            System.out.println(" Rank " + (i + 1) + ": " + rankedComplex.get(i));
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 2. Deliberately Faulty Comparators
        // -------------------------------------------------------------------------
        System.out.println("--- 2. Exposing Deliberately Faulty Comparators ---");

        // Faulty Comparator A: Integer Subtraction Overflow Bug
        System.out.println("A. Testing Subtraction-based Comparator: (t2.points - t1.points)");
        Team tExtreme1 = new Team("HighScorer", 2_000_000_000, 10);
        Team tExtreme2 = new Team("LowScorer", -1_500_000_000, 5);

        Comparator<Team> subtractionComparator = (a, b) -> (b.points - a.points);
        int subtractionResult = subtractionComparator.compare(tExtreme1, tExtreme2);
        int correctResult = Integer.compare(tExtreme2.points, tExtreme1.points);

        System.out.println("Comparing HighScorer (2B pts) vs LowScorer (-1.5B pts):");
        System.out.println("Subtraction (b.pts - a.pts): " + subtractionResult + " (OVERFLOW: returns positive, ranking LowScorer ABOVE HighScorer!)");
        System.out.println("Integer.compare(b.pts, a.pts): " + correctResult + " (CORRECT: returns negative, ranking HighScorer first)");
        System.out.println();

        // Faulty Comparator B: Incomplete Comparator (Ignores Name Tie-Breaker)
        System.out.println("B. Testing Incomplete Comparator (Ignores Name Tie-Breaker)");
        Comparator<Team> incompleteComparator = Comparator
                .comparingInt((Team t) -> t.points).reversed()
                .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed());
        // Lacks .thenComparing(t -> t.name)

        Team tAlpha = new Team("Alpha", 9, 4);
        Team tBeta = new Team("Beta", 9, 4);
        System.out.println("Incomplete compare(Alpha, Beta): " + incompleteComparator.compare(tAlpha, tBeta) + " (Equal!)");
        System.out.println("Alpha.equals(Beta): " + tAlpha.equals(tBeta) + " (Distinct objects!)");
        System.out.println("Principle Violation: Compare returns 0 (equal) for completely distinct teams!");
        System.out.println("In a TreeSet or binary search, Beta would be deleted or overwritten as a duplicate!");
        System.out.println();

        // -------------------------------------------------------------------------
        // 3. Generics Reusability: Players and Colleges
        // -------------------------------------------------------------------------
        System.out.println("--- 3. Demonstrating Generics Reusability ---");

        // Player Leaderboard
        Comparator<Player> playerComparator = Comparator
                .comparingInt((Player p) -> p.goals).reversed()
                .thenComparing(Comparator.comparingInt((Player p) -> p.assists).reversed())
                .thenComparing(p -> p.name);
        Leaderboard<Player> playerBoard = new Leaderboard<>(playerComparator);
        playerBoard.add(new Player("Messi", 30, 15));
        playerBoard.add(new Player("Ronaldo", 32, 5));
        playerBoard.add(new Player("Neymar", 20, 18));
        playerBoard.add(new Player("Mbappe", 30, 15)); // Tie broken by name: Mbappe < Messi
        System.out.println("Player Ranking  : " + playerBoard.ranking());

        // College Leaderboard
        Comparator<College> collegeComparator = Comparator
                .comparingInt((College c) -> c.goldMedals).reversed()
                .thenComparing(Comparator.comparingInt((College c) -> c.silverMedals).reversed())
                .thenComparing(c -> c.name);
        Leaderboard<College> collegeBoard = new Leaderboard<>(collegeComparator);
        collegeBoard.add(new College("IIT Madras", 15, 10));
        collegeBoard.add(new College("IISc Bangalore", 18, 5));
        collegeBoard.add(new College("BITS Pilani", 12, 14));
        collegeBoard.add(new College("IIT Delhi", 15, 12));
        System.out.println("College Ranking : " + collegeBoard.ranking());
        System.out.println();

        saveRankingBenchmarkToCsv("ExperimentalData/q5_ranking_benchmark.csv", rankedComplex);
    }

    private static void saveRankingBenchmarkToCsv(String path, List<Team> teams) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("Rank,TeamName,Points,GoalDifference");
            for (int i = 0; i < teams.size(); i++) {
                Team t = teams.get(i);
                pw.printf("%d,\"%s\",%d,%d%n", (i + 1), t.name, t.points, t.goalDiff);
            }
            System.out.println("Ranking benchmark successfully saved to: " + path);
        } catch (IOException e) {
            System.err.println("Error saving CSV: " + e.getMessage());
        }
    }
}
