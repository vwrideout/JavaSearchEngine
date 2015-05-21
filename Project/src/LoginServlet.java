import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jetty Servlet implementing the login process for user accounts.
 * @author Vincent
 *
 */
public class LoginServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		String status = request.getParameter("status");
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Login</title><body>");
		if(status != null){
			if(status.equals("badusername")){
				out.println("Bad username. Please try again.<br>");
			}
			if(status.equals("badpassword")){
				out.println("Incorrect password. Please try again.<br>");
			}
		}
		out.println("<form action=\"login\" method=\"post\">");
		out.println("Username: <br/>");
		out.println("<input type=\"text\" name=\"username\"><br/>");
		out.println("Password: <br/>");
		out.println("<input type=\"text\" name=\"password\"><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"></form>");
		out.println("<a href=\"search\">nevermind, back to search.</a>");
		out.println("</body></html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(username == null || username.equals("")){
			response.sendRedirect(response.encodeRedirectURL("/login?status=badusername"));
			return;
		}
		ResultSet results = MyJDBC.executeJDBCQuery("SELECT password FROM login WHERE username=\"" + username + "\";");
		try {
			if(!results.first()){
				response.sendRedirect(response.encodeRedirectURL("/login?status=badusername"));
				return;
			}
			if(!results.getString("password").equals(password)){
				response.sendRedirect(response.encodeRedirectURL("/login?status=badpassword"));
			}
			request.getSession().setAttribute("username", username);
			response.sendRedirect(response.encodeRedirectURL("/search"));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			response.sendRedirect(response.encodeRedirectURL("/login?status=badpassword"));
		}
	}
}
