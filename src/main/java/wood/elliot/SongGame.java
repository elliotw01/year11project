package wood.elliot;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

public class SongGame {

    private final static String SONG_FILE = "songs.txt";
    private final static String RESULTS_FILE = "results.txt";
    public static final String DELIMITER = "=";

    Scanner scanner;
    String username;
    Map<String, String> songs = new HashMap<>();

    public SongGame( Scanner scan, String user ) throws Exception {
        scanner = scan;
        username = user;
        loadSongs();
    }

    private void loadSongs() throws Exception{
        String rootPath = getClass().getClassLoader().getResource( "" ).getPath();
        File file = new File( rootPath + SONG_FILE );

        try (Stream<String> lines = Files.lines( file.toPath() ) ) {
            lines.filter(line -> line.contains(DELIMITER)).forEach(
                    line -> songs.putIfAbsent(line.split(DELIMITER)[0], line.split(DELIMITER)[1])
            );
        }
    }

    public void playGame( ) {
        System.out.println( "Playing game" );
        int songNumber = new Random().nextInt( songs.size() );

    }

}
