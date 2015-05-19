import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class JDBCHelloWorld {
	public static void main(String args[]){
		String username = "user20";
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(Exception e){
			System.out.println("Can't find driver");
			System.exit(0);
		}
		String urlString = "jdbc:mysql://127.0.0.1:3306/" + username;
		try {
			Connection con = DriverManager.getConnection(urlString, username, username);
			/**Statement stmt = con.createStatement();
			stmt.executeUpdate("CREATE TABLE hello (wordone VARCHAR(50), wordtwo VARCHAR(50);");
			stmt.executeUpdate("INSERT INTO hello VALUES (\"hello\", \"world\")");
			ResultSet result = stmt.executeQuery("SELECT * FROM hello");
			System.out.println(result.getString("wordone"));
			System.out.println(result.getString("wordtwo"));
			con.close();*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql exception");
		} 
	}
}
