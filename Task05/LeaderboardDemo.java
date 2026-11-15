package Task05;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Task05 - Q5: Derive a Generic Ranking Component from a Principle
 * 
 * Complete implementation of LeaderboardDemo with detailed explanation.
 * Expected output: [Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
class Team {
    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }

    @Override
    public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}

class Leaderboard<T> {
    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }

    void add(T item) {
        items.add(item);
    }

    // Accepts any collection whose element type is a subtype of T (PECS principle)
    void addAll(List<? extends T> newItems) {
        items.addAll(newItems);
    }

    List<T> ranking() {
        // TODO 1: Return a new sorted copy of items using order, without modifying items
        List<T> sortedCopy = new ArrayList<>(items);
        sortedCopy.sort(order);
        return sortedCopy;
    }
}

public class LeaderboardDemo {
    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task05 - Q5: Inter-College Sports Leaderboard Demonstration");
        System.out.println(" Student: Geetika T | Reg No: 192511469 | Java Version: 21");
        System.out.println("=========================================================================\n");

        // TODO 2: Build the comparator: points (high first), then goal difference (high
        // first), then name (A–Z)
        Comparator<Team> byRank = Comparator
                .comparingInt((Team t) -> t.points).reversed()
                .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed())
                .thenComparing(t -> t.name);

        Leaderboard<Team> board = new Leaderboard<>(byRank);
        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));

        List<Team> rankedTeams = board.ranking();
        System.out.println("Resulting Ranking:");
        System.out.println(rankedTeams);

        System.out.println("\n--- Verification of Expected Output ---");
        System.out.println("Expected: [Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]");
        System.out.println("Actual  : " + rankedTeams);
        boolean matches = rankedTeams.toString().equals("[Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]");
        System.out.println("Match Confirmed: " + matches);

        System.out.println("\n--- Why Bisons is Ranked Above Panthers ---");
        System.out.println("1. First criterion (Points): Both Bisons and Panthers have 9 points (TIE).");
        System.out.println("2. Second criterion (Goal Diff): Both Bisons and Panthers have +4 goal difference (TIE).");
        System.out.println("3. Third criterion (Name alphabetical A-Z):");
        System.out.println("   \"Bisons\".compareTo(\"Panthers\") yields a negative integer ('B' precedes 'P').");
        System.out.println("   Therefore, Bisons is definitively placed ahead of Panthers in the leaderboard.");
        System.out.println("=========================================================================");
    }
}
