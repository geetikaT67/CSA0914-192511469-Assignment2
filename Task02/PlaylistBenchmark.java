package Task02;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Task02 - Q2: When Is ArrayList an Appropriate Engineering Choice?
 * 
 * Comprehensive empirical comparison of ArrayList, LinkedList, and ArrayDeque
 * for a Music Streaming App Playlist Editor.
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class PlaylistBenchmark {

    static class OpResult {
        int playlistSize;
        String operation;
        double arrayListTimeMs;
        double linkedListTimeMs;
        double arrayDequeTimeMs;

        OpResult(int playlistSize, String operation, double alTime, double llTime, double adTime) {
            this.playlistSize = playlistSize;
            this.operation = operation;
            this.arrayListTimeMs = alTime;
            this.linkedListTimeMs = llTime;
            this.arrayDequeTimeMs = adTime;
        }
    }

    private static Song createDummySong(int index) {
        return new Song("TRK-" + index, "SongTitle-" + index, "Artist-" + (index % 50), 180 + (index % 120));
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task02 - Q2: Music Playlist Editor: ArrayList vs LinkedList vs ArrayDeque");
        System.out.println(" Student: Geetika T | Reg No: 192511469 | Java Version: 21");
        System.out.println("=========================================================================\n");

        int[] sizes = { 1000, 10000, 50000 };
        List<OpResult> allResults = new ArrayList<>();

        // Warm up JIT
        System.out.println("Warming up JIT compiler...");
        for (int i = 0; i < 500; i++) {
            List<Song> al = new ArrayList<>();
            List<Song> ll = new LinkedList<>();
            al.add(createDummySong(i));
            ll.add(createDummySong(i));
        }
        System.out.println("Warm up complete.\n");

        for (int size : sizes) {
            System.out.println("=========================================================================");
            System.out.println(" BENCHMARKING PLAYLIST SIZE: " + size + " SONGS");
            System.out.println("=========================================================================");

            int testOps = Math.min(2000, size); // Number of modifications/lookups to sample
            int trials = 5;

            // 1. Add at end (Building playlist)
            {
                double alTime = timeOperation(trials, () -> {
                    List<Song> list = new ArrayList<>();
                    for (int i = 0; i < size; i++) list.add(createDummySong(i));
                });
                double llTime = timeOperation(trials, () -> {
                    List<Song> list = new LinkedList<>();
                    for (int i = 0; i < size; i++) list.add(createDummySong(i));
                });
                double adTime = timeOperation(trials, () -> {
                    ArrayDeque<Song> deque = new ArrayDeque<>();
                    for (int i = 0; i < size; i++) deque.addLast(createDummySong(i));
                });
                allResults.add(new OpResult(size, "Add at End (" + size + " items)", alTime, llTime, adTime));
                printOp("Add at End (" + size + " items)", alTime, llTime, adTime);
            }

            // 2. Insert at Front (0)
            {
                double alTime = timeOperation(trials, () -> {
                    List<Song> list = populateArrayList(size);
                    for (int i = 0; i < testOps; i++) list.add(0, createDummySong(100000 + i));
                });
                double llTime = timeOperation(trials, () -> {
                    List<Song> list = populateLinkedList(size);
                    for (int i = 0; i < testOps; i++) list.add(0, createDummySong(100000 + i));
                });
                double adTime = timeOperation(trials, () -> {
                    ArrayDeque<Song> deque = populateArrayDeque(size);
                    for (int i = 0; i < testOps; i++) deque.addFirst(createDummySong(100000 + i));
                });
                allResults.add(new OpResult(size, "Insert at Front (" + testOps + " ops)", alTime, llTime, adTime));
                printOp("Insert at Front (" + testOps + " ops)", alTime, llTime, adTime);
            }

            // 3. Insert in Middle (size / 2)
            {
                double alTime = timeOperation(trials, () -> {
                    List<Song> list = populateArrayList(size);
                    for (int i = 0; i < testOps; i++) list.add(list.size() / 2, createDummySong(200000 + i));
                });
                double llTime = timeOperation(trials, () -> {
                    List<Song> list = populateLinkedList(size);
                    for (int i = 0; i < testOps; i++) list.add(list.size() / 2, createDummySong(200000 + i));
                });
                allResults.add(new OpResult(size, "Insert in Middle (" + testOps + " ops)", alTime, llTime, -1.0));
                printOp("Insert in Middle (" + testOps + " ops)", alTime, llTime, -1.0);
            }

            // 4. Remove from Front (0)
            {
                double alTime = timeOperation(trials, () -> {
                    List<Song> list = populateArrayList(size);
                    for (int i = 0; i < testOps; i++) list.remove(0);
                });
                double llTime = timeOperation(trials, () -> {
                    List<Song> list = populateLinkedList(size);
                    for (int i = 0; i < testOps; i++) list.remove(0);
                });
                double adTime = timeOperation(trials, () -> {
                    ArrayDeque<Song> deque = populateArrayDeque(size);
                    for (int i = 0; i < testOps; i++) deque.pollFirst();
                });
                allResults.add(new OpResult(size, "Remove from Front (" + testOps + " ops)", alTime, llTime, adTime));
                printOp("Remove from Front (" + testOps + " ops)", alTime, llTime, adTime);
            }

            // 5. Remove from Middle (size / 2)
            {
                double alTime = timeOperation(trials, () -> {
                    List<Song> list = populateArrayList(size);
                    for (int i = 0; i < testOps; i++) list.remove(list.size() / 2);
                });
                double llTime = timeOperation(trials, () -> {
                    List<Song> list = populateLinkedList(size);
                    for (int i = 0; i < testOps; i++) list.remove(list.size() / 2);
                });
                allResults.add(new OpResult(size, "Remove from Middle (" + testOps + " ops)", alTime, llTime, -1.0));
                printOp("Remove from Middle (" + testOps + " ops)", alTime, llTime, -1.0);
            }

            // 6. Access k-th element (Random Get)
            {
                Random rng = new Random(42);
                int[] indices = new int[testOps];
                for (int i = 0; i < testOps; i++) indices[i] = rng.nextInt(size);

                List<Song> al = populateArrayList(size);
                List<Song> ll = populateLinkedList(size);

                double alTime = timeOperation(trials, () -> {
                    for (int idx : indices) {
                        Song s = al.get(idx);
                        if (s == null) System.out.print("");
                    }
                });
                double llTime = timeOperation(trials, () -> {
                    for (int idx : indices) {
                        Song s = ll.get(idx);
                        if (s == null) System.out.print("");
                    }
                });
                allResults.add(new OpResult(size, "Random Access Get(k) (" + testOps + " ops)", alTime, llTime, -1.0));
                printOp("Random Access Get(k) (" + testOps + " ops)", alTime, llTime, -1.0);
            }

            // 7. Iterate through entire list
            {
                List<Song> al = populateArrayList(size);
                List<Song> ll = populateLinkedList(size);
                ArrayDeque<Song> ad = populateArrayDeque(size);

                double alTime = timeOperation(trials, () -> {
                    long totalDuration = 0;
                    for (Song s : al) totalDuration += s.getDurationSeconds();
                    if (totalDuration == 0) System.out.print("");
                });
                double llTime = timeOperation(trials, () -> {
                    long totalDuration = 0;
                    for (Song s : ll) totalDuration += s.getDurationSeconds();
                    if (totalDuration == 0) System.out.print("");
                });
                double adTime = timeOperation(trials, () -> {
                    long totalDuration = 0;
                    for (Song s : ad) totalDuration += s.getDurationSeconds();
                    if (totalDuration == 0) System.out.print("");
                });
                allResults.add(new OpResult(size, "Iterate Whole List (" + size + " items)", alTime, llTime, adTime));
                printOp("Iterate Whole List (" + size + " items)", alTime, llTime, adTime);
            }
            System.out.println();
        }

        // Save CSV
        saveCsv(allResults, "ExperimentalData/q2_playlist_benchmark.csv");
    }

    private static void printOp(String opName, double al, double ll, double ad) {
        String adStr = (ad >= 0) ? String.format("%10.4f ms", ad) : "       N/A";
        System.out.printf("  %-35s | AL: %10.4f ms | LL: %10.4f ms | AD: %s%n",
                opName, al, ll, adStr);
    }

    private static double timeOperation(int trials, Runnable task) {
        long totalNanos = 0;
        for (int t = 0; t < trials; t++) {
            System.gc();
            long start = System.nanoTime();
            task.run();
            totalNanos += (System.nanoTime() - start);
        }
        return (totalNanos / (double) trials) / 1_000_000.0;
    }

    private static ArrayList<Song> populateArrayList(int size) {
        ArrayList<Song> list = new ArrayList<>(size + 2000);
        for (int i = 0; i < size; i++) list.add(createDummySong(i));
        return list;
    }

    private static LinkedList<Song> populateLinkedList(int size) {
        LinkedList<Song> list = new LinkedList<>();
        for (int i = 0; i < size; i++) list.add(createDummySong(i));
        return list;
    }

    private static ArrayDeque<Song> populateArrayDeque(int size) {
        ArrayDeque<Song> deque = new ArrayDeque<>(size + 2000);
        for (int i = 0; i < size; i++) deque.addLast(createDummySong(i));
        return deque;
    }

    private static void saveCsv(List<OpResult> results, String path) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("PlaylistSize,Operation,ArrayListTimeMs,LinkedListTimeMs,ArrayDequeTimeMs");
            for (OpResult r : results) {
                pw.printf("%d,\"%s\",%.4f,%.4f,%.4f%n",
                        r.playlistSize, r.operation, r.arrayListTimeMs, r.linkedListTimeMs, r.arrayDequeTimeMs);
            }
            System.out.println("Experimental data successfully written to: " + path);
        } catch (IOException e) {
            System.err.println("Error saving CSV: " + e.getMessage());
        }
    }
}
