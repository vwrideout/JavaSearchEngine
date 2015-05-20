import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class NewUserServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		String status = request.getParameter("status");
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Login</title><body>");
		if(status != null){
			if(status.equals("bad")){
				out.println("Please enter both a username and a password.<br>");
			}
			if(status.equals("taken")){
				out.println("That username is taken. Please choose a different one.<br>");
			}
		}
		out.println("Create your new user account:<br>");
		out.println("<form action=\"newuser\" method=\"post\">");
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
		if(username == null || username.equals("") || password == null && password.equals("")){
			response.sendRedirect(response.encodeRedirectURL("/newuser?status=bad"));
			return;
		}
		ResultSet results = MyJDBC.executeJDBCQuery("SELECT * FROM login WHERE username=\"" + username + "\";");
		try {
			if(results.first()){
				response.sendRedirect(response.encodeRedirectURL("/newuser?status=taken"));
				return;
			}
			MyJDBC.executeJDBCUpdate("INSERT INTO login VALUES (\"" + username + "\",\"" + password + "\");");
			request.getSession().setAttribute("username", username);
			response.sendRedirect(response.encodeRedirectURL("/search"));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			response.sendRedirect(response.encodeRedirectURL("/newuser?status=bad"));
		}
	}

}
