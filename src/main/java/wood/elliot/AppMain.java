package wood.elliot;

import java.sql.*;
import java.util.Scanner;

public class AppMain {

    public static final String PLAY = "1";
    public static final String REGISTER = "2";
    public static final int MAX_OPTION_ATTEMPTS = 10;
    public static final int MAX_AUTHENTICATE_ATTEMPTS = 3;
    public static final int EXIT_GAME = 0;
    private static final String CREATE_USER_TABLE = "create table if not exists users(username varchar(10))";
    private static final String FIND_USER = "select * from users where username = ?";
    private static final String ADD_USER = "insert into users (username) values( ? )";

    private Connection conn;
    private Scanner scanner;

    public static void main(String[] args) {
        new AppMain();
    }

    public AppMain() {
        try {
            scanner = new Scanner(System.in);
            createConnection();
            createDatabase();
            Integer registerOrPlay = registerOrPlay();
            switch (registerOrPlay.toString()) {
                case PLAY -> startGame();
                case REGISTER -> register();
                default -> System.out.println("Too many chances, quitting");
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private Integer registerOrPlay() {
        for(int i = 0; i < MAX_OPTION_ATTEMPTS; i++ ) {
            System.out.println("Press 1 to play or 2 to register");
            String option = scanner.nextLine();
            if (option.equals(PLAY) || option.equals(REGISTER)) {
                return Integer.valueOf( option );
            } else {
                System.out.println( "Unrecognised value entered '" + option + "'" );
            }
        }
        return EXIT_GAME;
    }

    private void startGame() throws Exception{
        System.out.println( "Starting game" );
        String username = authenticate();
        if( username != null ) {
            new SongGame( scanner, username ).playGame();
        }
    }

    private String authenticate() throws SQLException {
        System.out.println( "Authenticating" );
        System.out.println("Please enter your username");
        for(int i = 0; i < MAX_AUTHENTICATE_ATTEMPTS; i++ ) {
            String username = scanner.next();
            if( userExists( username ) ) {
                System.out.println( "Welcome back " + username );
                return username;
            }
            else {
                System.out.println( "User not found, you have " + ( MAX_AUTHENTICATE_ATTEMPTS - (i+1) ) + " attempts remaining" );
            }
        }
        return null;
    }

    private boolean userExists( String username ) throws SQLException {
        PreparedStatement statement = conn.prepareStatement( FIND_USER );
        statement.setString( 1,  username );
        return statement.executeQuery().next();
    }

    private void register() throws Exception {
        System.out.println( "Registering" );
        System.out.println( "Enter your username" );
        String username = scanner.next();
        if( userExists( username ) ) {
            System.out.println( "User already exists" );
        }
        else {
            PreparedStatement statement = conn.prepareStatement( ADD_USER );
            statement.setString( 1, username );
            statement.execute();
            System.out.println( "User registered" );
        }
        startGame();
    }

    private void createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection( "jdbc:h2:./ewoodyr11DB" );
    }

    private void createDatabase() throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute( CREATE_USER_TABLE );
    }
}
