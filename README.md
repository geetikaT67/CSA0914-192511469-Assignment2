# Assignment 2 – CO2: Strings and Collections

## Student Details
- **Student Name:** Geetika T
- **Register Number:** 192511469
- **Degree / Course:** Computer Science and Engineering (CSA0914 – Programming in Java)
- **Java Version:** Java SE 21 (build `21.0.4+8-LTS-274`, 64-Bit Server VM)
- **Repository:** CSA0914-192511469-Assignment2
- **GitHub Link:** [https://github.com/geetikaT67/CSA0914-192511469-Assignment2.git](https://github.com/geetikaT67/CSA0914-192511469-Assignment2.git)

---

## Project Overview

This repository contains the complete, rigorous implementation, empirical benchmarking, and architectural evaluation for **Java Assignment 2 (CO2 – Strings and Collections)**. The project covers Sessions 15–26 of the curriculum, addressing:
- String immutability, `StringBuilder`, `StringBuffer`, and multithreaded logging.
- List implementations (`ArrayList`, `LinkedList`) and double-ended queues (`ArrayDeque`).
- The `hashCode()` and `equals()` contract, hash-bucket collisions, and key mutability hazards.
- Map hierarchies (`HashMap`, `TreeMap`, `LinkedHashMap`), lookup latencies, and report ordering.
- Generics, Comparator chaining, multi-tier tie-breaking, and bounded type wildcards (`? extends T`, `? super T`).
- Priority queues (`PriorityQueue`), min-heap mechanics, FIFO arrival preservation, and starvation mitigation via dynamic aging.
- Day-4 Design Challenge: Diagnosing the $O(n \log n)$ priority-aging bottleneck in `PriorityQueue` and engineering an $O(1)$ Multi-Level Bucket Queue architecture.

All experimental data is generated from genuine physical runs, averaged over repeated trials, and preserved as raw CSV files.

---

## Folder Structure

```
CSA0914-192511469-Assignment2/
│
├── CompleteTheCode/                # Official supplied skeleton files with TODOs completed
│   ├── Attendance.java             # Q3: Completed hashCode() override
│   ├── LeaderboardDemo.java        # Q5: Completed ranking() copy and multi-tier Comparator
│   └── Triage.java                 # Q6: Completed PriorityQueue with compound Comparator
│
├── Task01/                         # Q1: String, StringBuilder and StringBuffer
│   ├── TicketBuilderBenchmark.java # Benchmark of 3 workloads, nanoTime(), memory & intermediate strings
│   ├── MultithreadedLogDemo.java   # Concurrency race-condition demo (StringBuilder vs StringBuffer)
│   └── AlternativeTicketFormats.java # Comparison with String.join(), StringJoiner, and String.format()
│
├── Task02/                         # Q2: ArrayList, LinkedList and ArrayDeque
│   ├── Song.java                   # Music track domain model
│   ├── PlaylistBenchmark.java      # Comprehensive operations benchmark across 1k, 10k, and 50k items
│   ├── UpNextQueueEvaluation.java  # "Up Next" queue simulation (ArrayDeque vs LinkedList vs ArrayList)
│   └── PlainArrayPlaylist.java     # Alternative implementation using raw array and System.arraycopy
│
├── Task03/                         # Q3: hashCode() and equals()
│   ├── AttendanceUncompleted.java  # Run 1: Missing hashCode() demonstrating duplicate bug
│   ├── AttendanceCompleted.java    # Run 2: Overridden hashCode() demonstrating correct deduplication
│   ├── HashCodeEqualsInvestigation.java # 4 In-depth test cases (references, duplicates, mutation, TreeSet)
│   └── AlternativeDeduplication.java    # Comparison with TreeSet and HashMap<String, Student>
│
├── Task04/                         # Q4: HashMap, TreeMap and Inventory
│   ├── Medicine.java               # Pharmaceutical medicine domain entity
│   ├── PharmacyInventoryBenchmark.java # 100k lookups & dual report generation across 1k, 10k, 100k
│   ├── SortedArrayInventory.java   # Alternative implementation with Arrays.binarySearch()
│   └── HashtableComparison.java    # Demonstration of why Hashtable is obsolete
│
├── Task05/                         # Q5: Generics, Comparable, Comparator and Leaderboard
│   ├── LeaderboardDemo.java        # Core team ranking with verified output
│   ├── GenericLeaderboardTest.java # Multi-level ties, faulty comparators, and Player/College reuse
│   └── ThreePassStableSortComparison.java # Single-pass composite sort vs 3-pass stable TimSort
│
├── Task06/                         # Q6: PriorityQueue and Hospital Emergency Triage
│   ├── Triage.java                 # Core triage implementation with arrival counter tie-breaker
│   ├── TriageInvestigation.java    # 8-patient hand check, heap inspection, starvation aging & 5k sizing
│   └── TriageArchitectureComparison.java # Comparison: Heap vs Sorted LinkedList vs 5-FIFO Queues
│
├── Task07/                         # Q7: Day-4 Design Challenge (Diagnose, Redesign and Validate)
│   ├── OriginalTriageSystem.java   # Original PriorityQueue triage with O(n log n) aging bottleneck
│   ├── RedesignedTriageSystem.java # Redesigned 5-Level Bucket ArrayDeque Triage with O(1) aging
│   └── TriageRedesignBenchmark.java# Comparative benchmark under identical test conditions (1k to 100k)
│
├── ExperimentalData/               # Raw measurement CSV files
│   ├── q1_string_benchmark.csv     # Q1 timings, intermediate string estimates, and memory delta
│   ├── q2_playlist_benchmark.csv   # Q2 operation timings across 7 operations and 3 list sizes
│   ├── q3_attendance_tests.csv     # Q3 predicted vs actual set sizes across all test scenarios
│   ├── q4_pharmacy_benchmark.csv   # Q4 lookup times and report generation latencies
│   ├── q5_ranking_benchmark.csv    # Q5 ranking outputs and tie-breaker validation
│   ├── q6_triage_benchmark.csv     # Q6 admission and polling scaling data
│   └── q7_redesign_benchmark.csv   # Q7 before-and-after redesign performance metrics
│
├── Screenshots/                    # Full execution outputs and console logs
│   ├── Q1_benchmark_output.txt
│   ├── Q1_multithreaded_output.txt
│   ├── Q1_alternatives_output.txt
│   ├── Q2_benchmark_output.txt
│   ├── Q2_upnext_output.txt
│   ├── Q2_plainarray_output.txt
│   ├── Q3_attendance_uncompleted.txt
│   ├── Q3_attendance_completed.txt
│   ├── Q3_complete_the_code.txt
│   ├── Q3_investigation_output.txt
│   ├── Q3_alternatives_output.txt
│   ├── Q4_pharmacy_benchmark.txt
│   ├── Q4_sorted_array.txt
│   ├── Q4_hashtable_comparison.txt
│   ├── Q5_complete_the_code.txt
│   ├── Q5_leaderboard_demo.txt
│   ├── Q5_investigation_output.txt
│   ├── Q5_threepass_output.txt
│   ├── Q6_complete_the_code.txt
│   ├── Q6_triage_demo.txt
│   ├── Q6_investigation_output.txt
│   ├── Q6_comparison_output.txt
│   └── Q7_redesign_output.txt
│
├── Report/                         # Formal Assignment Report
│   └── Assignment2_Report.md       # Comprehensive 7-question analysis, diagrams, and derivations
│
└── README.md                       # Master index and execution guide
```

---

## Compilation and Execution Instructions

All commands are structured to run from the root workspace directory: `CSA0914-192511469-Assignment2`.

### Environment Configuration
Ensure Java 21 or higher is installed and accessible:
```bash
java -version
javac -version
```

---

### Task 01: Building Text Under Load (Q1)
Compile:
```bash
javac -d . Task01/TicketBuilderBenchmark.java Task01/MultithreadedLogDemo.java Task01/AlternativeTicketFormats.java
```
Execute:
```bash
# 1. Main benchmark across 3 workloads (String vs StringBuilder vs StringBuffer)
java Task01.TicketBuilderBenchmark

# 2. Multithreaded shared log race condition demo
java Task01.MultithreadedLogDemo

# 3. Alternative formats benchmark (String.join, StringJoiner, String.format)
java Task01.AlternativeTicketFormats
```

---

### Task 02: Music Playlist Editor (Q2)
Compile:
```bash
javac -d . Task02/Song.java Task02/PlaylistBenchmark.java Task02/UpNextQueueEvaluation.java Task02/PlainArrayPlaylist.java
```
Execute:
```bash
# 1. Main 7-operation benchmark across 1,000, 10,000, and 50,000 songs
java Task02.PlaylistBenchmark

# 2. Changed condition: "Up Next" double-ended queue evaluation
java Task02.UpNextQueueEvaluation

# 3. Plain array playlist with manual shifting
java Task02.PlainArrayPlaylist
```

---

### Task 03: hashCode() and equals() Contract (Q3)
Compile:
```bash
javac -d . Task03/AttendanceUncompleted.java Task03/AttendanceCompleted.java Task03/HashCodeEqualsInvestigation.java Task03/AlternativeDeduplication.java CompleteTheCode/Attendance.java
```
Execute:
```bash
# 1. Supplied code Run 1 (BEFORE completing TODO 1 - exposes bug)
java Task03.AttendanceUncompleted

# 2. Supplied code Run 2 (AFTER completing TODO 1 - verified output: 2)
java Task03.AttendanceCompleted

# 3. Official CompleteTheCode standalone verification
java CompleteTheCode.Attendance

# 4. In-depth contract tests (reference equality, duplicates, key mutation, TreeSet flaw)
java Task03.HashCodeEqualsInvestigation

# 5. Alternative architectures (TreeSet vs HashMap)
java Task03.AlternativeDeduplication
```

---

### Task 04: Choosing a Map for Pharmacy Inventory (Q4)
Compile:
```bash
javac -d . Task04/Medicine.java Task04/PharmacyInventoryBenchmark.java Task04/SortedArrayInventory.java Task04/HashtableComparison.java
```
Execute:
```bash
# 1. Main benchmark (100k lookups & code/insertion reports across 1k, 10k, 100k medicines)
java Task04.PharmacyInventoryBenchmark

# 2. Alternative concept: Sorted array with Arrays.binarySearch()
java Task04.SortedArrayInventory

# 3. Scalability inquiry: Why Hashtable is obsolete
java Task04.HashtableComparison
```

---

### Task 05: Generic Ranking Component (Q5)
Compile:
```bash
javac -d . Task05/LeaderboardDemo.java Task05/GenericLeaderboardTest.java Task05/ThreePassStableSortComparison.java CompleteTheCode/LeaderboardDemo.java
```
Execute:
```bash
# 1. Supplied code execution (Expected: [Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)])
java CompleteTheCode.LeaderboardDemo

# 2. Task05 LeaderboardDemo with detailed reasoning
java Task05.LeaderboardDemo

# 3. Multi-tier ties, faulty comparators, and Player/College reuse
java Task05.GenericLeaderboardTest

# 4. Single-pass composite sort vs 3-pass stable TimSort
java Task05.ThreePassStableSortComparison
```

---

### Task 06: Priority, Order and Fairness in a Queue (Q6)
Compile:
```bash
javac -d . Task06/Triage.java Task06/TriageInvestigation.java Task06/TriageArchitectureComparison.java CompleteTheCode/Triage.java
```
Execute:
```bash
# 1. Supplied code execution (Expected: Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5))
java CompleteTheCode.Triage

# 2. Task06 Triage demo with tie-breaker mechanics
java Task06.Triage

# 3. 8-Patient hand check, heap layout, starvation aging, and 5,000 patient sizing
java Task06.TriageInvestigation

# 4. Architectural comparison: Heap vs Sorted LinkedList vs 5 FIFO Queues
java Task06.TriageArchitectureComparison
```

---

### Task 07: Day-4 Design Challenge (Diagnose, Redesign and Validate) (Q7)
Compile:
```bash
javac -d . Task07/OriginalTriageSystem.java Task07/RedesignedTriageSystem.java Task07/TriageRedesignBenchmark.java
```
Execute:
```bash
# Comparative benchmark comparing Original PriorityQueue vs Redesigned MultiLevelQueue
java Task07.TriageRedesignBenchmark
```

---

## Short Result Summary for Every Question

| Question | Assessed Area | Key Empirical / Conceptual Finding |
|---|---|---|
| **Q1** | String, StringBuilder & StringBuffer | Repeated `+` creates $O(n^2)$ character copies and over 1,000,000 intermediate objects on 10k tickets. `StringBuilder` runs in 55.15 ms ($O(n)$) creating 0 intermediate buffers. In multithreaded logging, `StringBuilder` lost 50,880 characters to race conditions; `StringBuffer` was 100% thread-safe. `String.format()` was 7.26x slower than `StringBuilder`. |
| **Q2** | ArrayList, LinkedList & ArrayDeque | On 50,000 songs, `ArrayList.get(k)` took 0.63 ms vs `LinkedList.get(k)` at 1,909.91 ms (over 3000x slower!). `ArrayList` middle insertion took 37.38 ms vs `LinkedList` at 5,684.35 ms (152x slower) due to $O(n)$ pointer traversal. For the "Up Next" queue, `ArrayDeque` was fastest with zero node allocation and cache-friendly contiguous ring buffering. |
| **Q3** | hashCode() & equals() Contract | Without `hashCode()`, identical `Student` instances landed in different buckets, producing duplicate entries (`Unique students: 3`). Once `hashCode()` was overridden, duplicates were rejected (`Unique students: 2`). Mutating `regNo` after set insertion caused elements to become permanently unfindable ghosts. |
| **Q4** | HashMap, TreeMap & LinkedHashMap | For 100,000 lookups, `HashMap` took 54.00 ms (0.54 $\mu$s/lookup) vs `TreeMap` at 136.11 ms ($O(\log n)$ tree traversal). `TreeMap` naturally generated Code Order reports in 13.24 ms, whereas `HashMap` required 112.39 ms of sorting overhead. `LinkedHashMap` was the only structure preserving true insertion order. `Hashtable` is obsolete due to method-level lock contention. |
| **Q5** | Generics, Comparable & Comparator | Completed `LeaderboardDemo` verified output: `[Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]`. Bisons precedes Panthers alphabetically on tie. Subtraction `(b.points - a.points)` broke with integer overflow; `Integer.compare()` satisfied transitivity. The generic class seamlessly ranked `Player` and `College` objects. Single-pass composite sort outperformed 3-pass stable sort by 1.43x. |
| **Q6** | PriorityQueue & Hospital Triage | Completed `Triage` verified output: `Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)`. Hand-worked 8-patient trace matched program output perfectly. Printing `PriorityQueue` directly showed binary heap array order rather than priority order. Starvation was solved via dynamic priority aging. Sizing confirmed that 5,000 patients/day consumes $<0.01\%$ CPU and ~250 KB RAM. |
| **Q7** | Day-4 Design Challenge | Diagnosed the $O(n \log n)$ bottleneck of priority aging in `PriorityQueue` (draining and re-heapifying). Redesigned into a 5-Level Bucket Queue using `ArrayDeque<Patient>[]`. Under 100,000 patients, aging was accelerated from 148.94 ms to 19.75 ms (7.5x speedup), next-patient polling was accelerated from 107.68 ms to 20.97 ms (5x speedup), with zero comparator calls and strict FIFO fidelity. |

---

## Author & Verification
- **Geetika T** (Register Number: `192511469`)
- Verified on Java SE 21 LTS, Windows PowerShell environment.
- All code compiles cleanly with zero warnings or errors.
