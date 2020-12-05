package wood.elliot;

import java.sql.*;
import java.util.Scanner;

public class AppMain {

    public static void main(String[] args) {
        try {
            createDatabase();

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println( "Press 1 to play or 2 to register" );
                String option = scanner.nextLine();

                if( option == "1" ) {
                    System.out.println("1 entered");
                }
                else if ( option == "2" ) {
                    System.out.println("2 entered");
                }
                else {
                    System.out.println("entered: " + option );
                }

                System.out.print("Enter your username : ");
                String name = scanner.nextLine();
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private static void createDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection( "jdbc:h2:./ewoodyr11DB" );
        Statement statement = conn.createStatement();
        statement.execute( "create table if not exists users(username varchar(10))" );
    }

}
