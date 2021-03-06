import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class containing static methods for interacting with the MySQL database.
 * @author Vincent Rideout
 *
 */
public class MyJDBC {
	/**
	 * Connects to the cs212 database on this computer, then executes the provided update command on that database.
	 * @param update
	 */
	public static void executeJDBCUpdate(String update){
		String username = "cs212";
		String password = "Project5";
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(Exception e){
			System.out.println("Can't find driver");
			System.exit(0);
		}
		String urlString = "jdbc:mysql://localhost:3306/";
		try {
			Connection con = DriverManager.getConnection(urlString, username, password);
			Statement stmt = con.createStatement();
			stmt.executeUpdate("USE cs212");
			stmt.executeUpdate(update);
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 
	}
	
	/**
	 * Connects to the cs212 database, then executes a query on that database and returns the result.
	 * @param query
	 * @return
	 */
	public static ResultSet executeJDBCQuery(String query){
		String username = "cs212";
		String password = "Project5";
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(Exception e){
			System.out.println("Can't find driver");
			System.exit(0);
		}
		String urlString = "jdbc:mysql://localhost:3306/";
		try {
			Connection con = DriverManager.getConnection(urlString, username, password);
			Statement stmt = con.createStatement();
			stmt.executeUpdate("USE cs212");
			ResultSet output = stmt.executeQuery(query);
			return output;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		} 
	}

}
