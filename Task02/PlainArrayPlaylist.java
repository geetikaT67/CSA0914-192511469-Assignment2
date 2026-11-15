package Task02;

import java.util.Arrays;

/**
 * Task02 - Alternative Concept: Plain Array Playlist with Manual Shifting
 * 
 * Implements a playlist using a plain Song[] array with manual boundary
 * checking, geometric resizing, and System.arraycopy() element shifting.
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class PlainArrayPlaylist {

    private Song[] elements;
    private int size;

    public PlainArrayPlaylist(int initialCapacity) {
        this.elements = new Song[initialCapacity];
        this.size = 0;
    }

    public PlainArrayPlaylist() {
        this(10);
    }

    public int size() {
        return size;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    // Add at end: O(1) amortized
    public void add(Song song) {
        ensureCapacity(size + 1);
        elements[size++] = song;
    }

    // Insert at index: O(n) due to manual System.arraycopy shifting
    public void add(int index, Song song) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        ensureCapacity(size + 1);
        // Shift elements to the right
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = song;
        size++;
    }

    // Remove from index: O(n) due to manual shifting
    public Song remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        Song oldValue = elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // Prevent memory leak
        return oldValue;
    }

    // Random access: O(1)
    public Song get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        return elements[index];
    }

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println(" Task02: Plain Array Playlist with Manual Shifting");
        System.out.println(" Demonstrating equivalence and complexity vs Java's ArrayList");
        System.out.println("=========================================================================\n");

        PlainArrayPlaylist playlist = new PlainArrayPlaylist(5);
        playlist.add(new Song("S1", "Song A", "Artist 1", 200));
        playlist.add(new Song("S2", "Song B", "Artist 2", 210));
        playlist.add(new Song("S3", "Song C", "Artist 3", 195));

        System.out.println("Initial playlist size: " + playlist.size());
        System.out.println("Element at 1: " + playlist.get(1));

        // Insert at front
        playlist.add(0, new Song("S0", "Song Intro", "Artist 0", 60));
        System.out.println("After insert at 0, size: " + playlist.size() + " | Head: " + playlist.get(0));

        // Remove from middle
        Song removed = playlist.remove(2);
        System.out.println("Removed middle item: " + removed);
        System.out.println("New size: " + playlist.size());

        System.out.println("\n--- Complexity Comparison: Plain Array vs ArrayList vs LinkedList ---");
        System.out.println("Operation              | Plain Array         | ArrayList           | LinkedList");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Add at End             | O(1) amortized      | O(1) amortized      | O(1)");
        System.out.println("Insert at Front        | O(n) manual copy    | O(n) arraycopy      | O(1)");
        System.out.println("Insert in Middle       | O(n) manual copy    | O(n) arraycopy      | O(n) (traversal)");
        System.out.println("Remove from Front      | O(n) manual copy    | O(n) arraycopy      | O(1)");
        System.out.println("Remove from Middle     | O(n) manual copy    | O(n) arraycopy      | O(n) (traversal)");
        System.out.println("Get(k) Random Access   | O(1) direct index   | O(1) direct index   | O(n) pointer hop");
        System.out.println("Iteration              | O(n) contiguous     | O(n) contiguous     | O(n) pointer hop");
        System.out.println("Space Per Element      | 4-8 bytes reference | 4-8 bytes reference | 24-32 bytes (Node)");
        System.out.println("=========================================================================");
    }
}
