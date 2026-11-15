package Task02;

import java.util.Objects;

/**
 * Task02 - Song Model for Music Playlist Editor
 * 
 * Student: Geetika T (Reg No: 192511469)
 */
public class Song {
    private final String id;
    private final String title;
    private final String artist;
    private final int durationSeconds;

    public Song(String id, String title, String artist, int durationSeconds) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationSeconds = durationSeconds;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDurationSeconds() { return durationSeconds; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + ": " + title + " - " + artist + " (" + durationSeconds + "s)";
    }
}
