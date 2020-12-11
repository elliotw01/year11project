package wood.elliot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SongGame {

    private final static String SONG_FILE = "songs.txt";
    private final static String RESULTS_FILE = "results.txt";
    private final static int MAX_GUESSES = 2;
    public static final String DELIMITER = "=";

    Scanner scanner;
    String username;
    Map<String, String> songs;

    public SongGame( Scanner scan, String user ) throws Exception {
        scanner = scan;
        username = user;
        songs = loadFile( SONG_FILE );
    }

    private Map<String, String> loadFile(String fileName ) throws Exception{
        String rootPath = getClass().getClassLoader().getResource( "" ).getPath();
        File file = new File( rootPath + fileName );

        Map<String, String> map = new HashMap<>();

        try (Stream<String> lines = Files.lines( file.toPath() ) ) {
            lines.filter(line -> line.contains(DELIMITER)).forEach(
                    line -> map.putIfAbsent(line.split(DELIMITER)[0], line.split(DELIMITER)[1])
            );
        }
        return map;
    }

    public void playGame() throws Exception {
        System.out.println( "Playing game" );

        boolean play = true;
        int totalPoints = 0;
        while( play && !songs.isEmpty() ) {
            String song = getRandomSong();
            String songInitials = splitSong(song);
            String artist = songs.get(song);
            songs.remove(song);
            for ( int i = 0 ; i < MAX_GUESSES ; i++ ) {
                System.out.println( "Guess the song, you have " + (MAX_GUESSES - i) + " attempts remaining" );
                System.out.println( "The artist is: " + artist );
                System.out.println( "The first letters of the song are: " + songInitials );
                String userGuess = scanner.nextLine();
                if ( userGuess.toLowerCase().equals( song.toLowerCase() ) ) {
                    int points = MAX_GUESSES - i;
                    totalPoints += points;
                    break;
                }
                else {
                    System.out.println( "Incorrect" );
                    if( i == MAX_GUESSES - 1 ) {
                        play = false;
                    }
                }
            }
        }
        System.out.println("Game over, you scored " + totalPoints );
        updateResults( totalPoints );
        showTop();
    }

    private void showTop() throws Exception {
        Map<String, String> results = loadFile( RESULTS_FILE );
        results.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(5)
                .forEach(System.out::println);
    }

    private void updateResults(int totalPoints) throws IOException {
        String rootPath = getClass().getClassLoader().getResource( "" ).getPath();
        FileWriter fw = new FileWriter(rootPath + RESULTS_FILE, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(username + "=" + totalPoints);
        bw.newLine();
        bw.close();
    }

    private String getRandomSong() {
        int songNumber = new Random().nextInt( songs.size() );
        int i = 0;
        String randomSong = "";
        for( String song : songs.keySet() ) {
            if( i == songNumber ) {
                randomSong = song;
                break;
            }
            i++;
        }
        return randomSong;
    }

    private String splitSong( String song ) {
        String[] myName = song.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String s : myName) {
            initials.append( s.charAt(0) + " " );
        }
        return initials.toString().trim();
    }

}
