import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Login</title><body>");
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
		if(username == null || username == ""){
			response.sendRedirect(response.encodeRedirectURL("/login?status=badusername"));
			return;
		}
		ResultSet results = MyJDBC.executeJDBCQuery("SELECT password FROM login WHERE username=" + username);
		try {
			results.next();
			if(results.getString("password") != password){
				throw new SQLException();
			}
			
		} catch (SQLException e) {
			response.sendRedirect(response.encodeRedirectURL("/login?status=badpassword"));
		}
	}
}
