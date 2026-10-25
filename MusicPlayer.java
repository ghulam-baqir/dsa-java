import java.util.Scanner;

public class MusicPlayer {
    // 1. Song Node Structure (Inside the same file)
    static class Song {
        String title;
        String artist;
        Song next; // Pointer to next song
        Song prev; // Pointer to previous song

        Song(String title, String artist) {
            this.title = title;
            this.artist = artist;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString() {
            return "'" + title + "' by " + artist;
        }
    }

    // Pointers for our Doubly Linked List Playlist
    private static Song head = null;
    private static Song tail = null;
    private static Song currentSong = null;

    // 2. Music Player Operations
    public static void addSong(String title, String artist) {
        Song newSong = new Song(title, artist);
        if (head == null) {
            head = newSong;
            tail = newSong;
            currentSong = head;
        } else {
            tail.next = newSong;
            newSong.prev = tail;
            tail = newSong;
        }
        System.out.println("🎵 Added: " + newSong);
    }

    public static void playCurrent() {
        if (currentSong == null) {
            System.out.println("❌ Playlist empty hai! Pehle gaane add karein.");
        } else {
            System.out.println("▶️ Now Playing: " + currentSong);
        }
    }

    public static void nextSong() {
        if (currentSong == null) {
            System.out.println("❌ Playlist empty hai!");
        } else if (currentSong.next == null) {
            System.out.println("🔁 Aap last gaane par hain. Agla gaana nahi hai.");
        } else {
            currentSong = currentSong.next;
            System.out.println("⏭️ Switched to next song:");
            playCurrent();
        }
    }

    public static void previousSong() {
        if (currentSong == null) {
            System.out.println("❌ Playlist empty hai!");
        } else if (currentSong.prev == null) {
            System.out.println("⏮️ Aap pehle hi sab se pehle gaane par hain.");
        } else {
            currentSong = currentSong.prev;
            System.out.println("⏪ Switched to previous song:");
            playCurrent();
        }
    }

    public static void displayPlaylist() {
        if (head == null) {
            System.out.println("📂 Playlist empty hai.");
            return;
        }
        System.out.println("\n--- 🎶 Current Playlist ---");
        Song temp = head;
        int count = 1;
        while (temp != null) {
            String pointer = (temp == currentSong) ? " <-- [CURRENTLY PLAYING]" : "";
            System.out.println(count + ". " + temp + pointer);
            temp = temp.next;
            count++;
        }
        System.out.println("---------------------------");
    }

    // 3. Simple Menu Runner
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Auto-add 3 starting songs
        addSong("Dil Dil Pakistan", "Vital Signs");
        addSong("Afreen Afreen", "Nusrat Fateh Ali Khan");
        addSong("Tajdar-e-Haram", "Atif Aslam");

        int choice = 0;
        do {
            System.out.println("\n====== 📻 LINKEDLIST MUSIC PLAYER ======");
            System.out.println("1. Play Current Song");
            System.out.println("2. Next Song (⏭️)");
            System.out.println("3. Previous Song (⏪)");
            System.out.println("4. Add New Song");
            System.out.println("5. Show Entire Playlist");
            System.out.println("6. Exit");
            System.out.print("Enter choice (1-6): ");
            
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                
                switch (choice) {
                    case 1: playCurrent(); break;
                    case 2: nextSong(); break;
                    case 3: previousSong(); break;
                    case 4:
                        System.out.print("Enter Song Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter Artist Name: ");
                        String artist = scanner.nextLine();
                        addSong(title, artist);
                        break;
                    case 5: displayPlaylist(); break;
                    case 6: System.out.println("👋 Closing Player. Goodbye!"); break;
                    default: System.out.println("❌ Invalid option! 1-6 choose karein.");
                }
            } catch (Exception e) {
                System.out.println("❌ Please enter a valid number.");
                scanner.nextLine(); // Clear input
                choice = 0;
            }
        } while (choice != 6);

        scanner.close();
    }
}
