package wood.elliot;

import java.sql.*;
import java.util.Scanner;

public class AppMain {

    public static final String PLAY = "1";
    public static final String REGISTER = "2";
    public static final int MAX_OPTION_ATTEMPTS = 10;
    public static final int MAX_AUTHENTICATE_ATTEMPTS = 3;
    public static final int EXIT_GAME = 0;

    public static void main(String[] args) {
        try {
            createDatabase();
            AppMain appMain = new AppMain();
            Integer registerOrPlay = appMain.registerOrPlay();
            switch (registerOrPlay.toString()) {
                case PLAY -> appMain.startGame();
                case REGISTER -> appMain.register();
                default -> System.out.println("Too many chances, quitting");
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private Integer registerOrPlay() {
        try (Scanner scanner = new Scanner(System.in)) {
            for(int i = 0; i < MAX_OPTION_ATTEMPTS; i++ ) {
                System.out.println("Press 1 to play or 2 to register");
                String option = scanner.nextLine();
                if (option.equals(PLAY) || option.equals(REGISTER)) {
                    return Integer.valueOf( option );
                } else {
                    System.out.println( "Unrecognised value entered '" + option + "'" );
                }
            }
        }
        return EXIT_GAME;
    }

    private void startGame() {
        System.out.println( "Starting game" );
        authenticate();
    }

    private String authenticate() {
        System.out.println( "Authenticating" );
        try (Scanner scanner = new Scanner(System.in)) {
            for(int i = 0; i < MAX_AUTHENTICATE_ATTEMPTS; i++ ) {
                System.out.println("Please enter your username");
                String username = scanner.nextLine();
                if( userExists( username ) ) {
                    return username;
                }
            }
        }
        throw new UnsupportedOperationException( "User does not exist" );
    }

    private boolean userExists( String username ) {
        return false;
    }

    private void register() {
        System.out.println( "Registering" );
    }

    private static void createDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection( "jdbc:h2:./ewoodyr11DB" );
        Statement statement = conn.createStatement();
        statement.execute( "create table if not exists users(username varchar(10))" );
    }

}
