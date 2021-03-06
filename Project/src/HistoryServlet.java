import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jetty Servlet that implements the storing of a users search history. 
 * Allows the user to view and clear that history.
 * @author Vincent
 *
 */
public class HistoryServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String username = (String)request.getSession().getAttribute("username");
		if(request.getParameter("clear") != null && request.getParameter("clear").equals("true")){
			MyJDBC.executeJDBCUpdate("DELETE FROM history WHERE username=\"" + username + "\";");
		}
		ResultSet results = MyJDBC.executeJDBCQuery("SELECT query FROM history WHERE username=\"" + username + "\";");
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>History</title><body>");
		out.println("Search History for " + username + ":<br>");
		try {
			if(!results.last()){
				out.println("No searches in history<br>");
			}
			else{
				out.println("<a href=\"search?query=" + results.getString("query") + "\" method=\"post\">" + results.getString("query") + "</a><br>");
				while(results.previous()){
					out.println("<a href=\"search?query=" + results.getString("query") + "\" method=\"post\">" + results.getString("query") + "</a><br>");
				}
			}
			out.println("-----------------<br>");
			out.println("<a href=\"history?clear=true\">Clear history</a>");
			out.println("<a href=\"search\">Home</a>");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
