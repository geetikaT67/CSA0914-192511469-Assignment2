package Task01;

import java.util.ArrayList;
import java.util.List;

/**
 * Task01 - Multithreaded Shared Booking Log Investigation
 * 
 * Demonstrates the safety trade-off between StringBuilder and StringBuffer
 * when multiple booking threads append to a shared log concurrently.
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class MultithreadedLogDemo {

    private static final int THREAD_COUNT = 8;
    private static final int APPENDS_PER_THREAD = 1000;
    private static final String LOG_ENTRY = "BK-192511469-OK;";

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=========================================================================");
        System.out.println(" Task01: Multithreaded Shared Booking Log Design Decision");
        System.out.println(" Demonstrating StringBuilder (Unsynchronized) vs StringBuffer (Synchronized)");
        System.out.println("=========================================================================\n");

        int expectedLength = THREAD_COUNT * APPENDS_PER_THREAD * LOG_ENTRY.length();
        int expectedAppends = THREAD_COUNT * APPENDS_PER_THREAD;
        System.out.println("Threads: " + THREAD_COUNT + " | Appends per thread: " + APPENDS_PER_THREAD);
        System.out.println("Expected total log entries: " + expectedAppends);
        System.out.println("Expected final character length: " + expectedLength + "\n");

        // 1. Test StringBuilder under concurrent writes
        System.out.println("--- Test 1: Shared StringBuilder (Thread-unsafe) ---");
        StringBuilder sharedSb = new StringBuilder();
        List<Thread> sbThreads = new ArrayList<>();
        int[] sbExceptions = new int[1];

        for (int t = 0; t < THREAD_COUNT; t++) {
            Thread th = new Thread(() -> {
                try {
                    for (int i = 0; i < APPENDS_PER_THREAD; i++) {
                        sharedSb.append(LOG_ENTRY);
                    }
                } catch (Exception e) {
                    synchronized (sbExceptions) {
                        sbExceptions[0]++;
                    }
                }
            });
            sbThreads.add(th);
        }

        long sbStart = System.nanoTime();
        for (Thread th : sbThreads) th.start();
        for (Thread th : sbThreads) th.join();
        long sbDurationMs = (System.nanoTime() - sbStart) / 1_000_000;

        int actualSbLength = sharedSb.length();
        System.out.println("StringBuilder actual length : " + actualSbLength);
        System.out.println("StringBuilder expected length: " + expectedLength);
        System.out.println("Exceptions caught during execution: " + sbExceptions[0]);
        if (actualSbLength != expectedLength || sbExceptions[0] > 0) {
            System.out.println(">> RESULT: DATA CORRUPTION / RACE CONDITION DETECTED! Lost characters: " + (expectedLength - actualSbLength));
        } else {
            System.out.println(">> RESULT: Coincidental match (rare under high thread contention).");
        }
        System.out.println("Execution time: " + sbDurationMs + " ms\n");

        // 2. Test StringBuffer under concurrent writes
        System.out.println("--- Test 2: Shared StringBuffer (Thread-safe) ---");
        StringBuffer sharedSbuf = new StringBuffer();
        List<Thread> sbufThreads = new ArrayList<>();
        int[] sbufExceptions = new int[1];

        for (int t = 0; t < THREAD_COUNT; t++) {
            Thread th = new Thread(() -> {
                try {
                    for (int i = 0; i < APPENDS_PER_THREAD; i++) {
                        sharedSbuf.append(LOG_ENTRY);
                    }
                } catch (Exception e) {
                    synchronized (sbufExceptions) {
                        sbufExceptions[0]++;
                    }
                }
            });
            sbufThreads.add(th);
        }

        long sbufStart = System.nanoTime();
        for (Thread th : sbufThreads) th.start();
        for (Thread th : sbufThreads) th.join();
        long sbufDurationMs = (System.nanoTime() - sbufStart) / 1_000_000;

        int actualSbufLength = sharedSbuf.length();
        System.out.println("StringBuffer actual length  : " + actualSbufLength);
        System.out.println("StringBuffer expected length: " + expectedLength);
        System.out.println("Exceptions caught during execution: " + sbufExceptions[0]);
        if (actualSbufLength == expectedLength && sbufExceptions[0] == 0) {
            System.out.println(">> RESULT: THREAD-SAFETY CONFIRMED! Zero lost updates, perfect synchronization.");
        } else {
            System.out.println(">> RESULT: Discrepancy detected.");
        }
        System.out.println("Execution time: " + sbufDurationMs + " ms\n");

        System.out.println("=========================================================================");
        System.out.println(" Engineering Decision Summary:");
        System.out.println(" 1. Per-Ticket Generation: Recommend StringBuilder.");
        System.out.println("    Reason: The ticket object is thread-confined (instantiated inside the");
        System.out.println("    request method). No other thread touches it. Synchronization overhead");
        System.out.println("    would be completely wasted.");
        System.out.println(" 2. Shared Central Booking Log: Recommend StringBuffer (or concurrent logger).");
        System.out.println("    Reason: Mutated concurrently by multiple worker threads. StringBuffer's");
        System.out.println("    synchronized methods guarantee atomic appends and buffer integrity.");
        System.out.println("=========================================================================");
    }
}
