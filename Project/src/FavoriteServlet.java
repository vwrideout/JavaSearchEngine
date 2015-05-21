import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jetty Servlet that implements the storing, viewing and clearing of a "favorites" list for search links.
 * @author Vincent Rideout
 *
 */
public class FavoriteServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String link = request.getParameter("link");
		String username = (String)request.getSession().getAttribute("username");
		if(link != null){
			if(username != null){
				MyJDBC.executeJDBCUpdate("INSERT INTO favorite VALUES (\"" + username + "\",\"" + link + "\");");
			}
			response.sendRedirect(response.encodeRedirectURL("search?query=" + request.getParameter("query")));
			return;
		}
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		if(request.getParameter("clear") != null && request.getParameter("clear").equals("true")){
			MyJDBC.executeJDBCUpdate("DELETE FROM favorite WHERE username=\"" + username + "\";");
		}
		ResultSet results = MyJDBC.executeJDBCQuery("SELECT url FROM favorite WHERE username=\"" + username + "\";");
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Favorites</title><body>");
		out.println("Favorite links for " + username + ":<br>");
		try {
			if(!results.last()){
				out.println("You haven't chosen any favorites.<br>");
			}
			else{
				out.println("<a href=\"" + results.getString("url") + "\">" + results.getString("url") + "</a><br>");
				while(results.previous()){
					out.println("<a href=\"" + results.getString("url") + "\">" + results.getString("url") + "</a><br>");				}
			}
			out.println("-----------------<br>");
			out.println("<a href=\"favorite?clear=true\">Clear favorites</a>");
			out.println("<a href=\"search\">Home</a>");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
