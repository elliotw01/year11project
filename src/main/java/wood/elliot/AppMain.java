package wood.elliot;

import java.sql.*;

public class AppMain {

    private static final String DEFAULT_USERNAME = "elliotw";
    private static final String DEFAULT_PASSWORD = "3Ll10Tw";

    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection( "jdbc:h2:./ewoodyr11DB" );
            Statement statement = conn.createStatement();
            statement.execute( "create table if not exists users(username varchar(10), password varchar(20))" );
            loadUsers( conn );

            new ArtistAndSong().populateMap();
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private static void loadUsers( Connection conn ) throws SQLException {
        PreparedStatement ps1 = conn.prepareStatement( "select count(*) from users where username = ?" );
        ps1.setString( 1, DEFAULT_USERNAME );
        ResultSet rs = ps1.executeQuery();
        rs.next();
        if( rs.getInt(1) != 1 ) {
            System.out.println( "Adding default user" );
            PreparedStatement ps2 = conn.prepareStatement( "insert into users ( username, password ) values( ?, ? )" );
            ps2.setString( 1, DEFAULT_USERNAME );
            ps2.setString( 2, DEFAULT_PASSWORD );
            ps2.execute();
        }
    }
}
