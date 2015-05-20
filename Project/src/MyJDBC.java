import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MyJDBC {
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
			stmt.executeUpdate(update);
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 
	}
	
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
			ResultSet output = stmt.executeQuery(query);
			con.close();
			return output;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		} 
	}

}
