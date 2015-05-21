import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ChangePasswordServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		String status = request.getParameter("status");
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Change Password</title><body>");
		if(status != null){
			if(status.equals("badoldpassword")){
				out.println("Incorrect old password.<br>");
			}
			if(status.equals("badnewpassword")){
				out.println("Please complete both fields.<br>");
			}
		}
		out.println("Change your password:<br>");
		out.println("<form action=\"changepassword\" method=\"post\">");
		out.println("Old Password: <br/>");
		out.println("<input type=\"text\" name=\"oldpassword\"><br/>");
		out.println("New Password: <br/>");
		out.println("<input type=\"text\" name=\"newpassword\"><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"></form>");
		out.println("<a href=\"search\">nevermind, back to search.</a>");
		out.println("</body></html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String username = (String)request.getSession().getAttribute("username");
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		if(oldpassword == null || oldpassword.equals("") || newpassword == null || newpassword.equals("")){
			response.sendRedirect(response.encodeRedirectURL("/changepassword?status=badnewpassword"));
			return;
		}
		ResultSet results = MyJDBC.executeJDBCQuery("SELECT * FROM login WHERE username=\"" + username + "\";");
		try {
			results.first();
			if(!results.getString("password").equals(request.getParameter("oldpassword"))){
				response.sendRedirect(response.encodeRedirectURL("/changepassword?status=badoldpassword"));
				return;
			}
			MyJDBC.executeJDBCUpdate("DELETE FROM login WHERE username=\"" + username + "\";");
			MyJDBC.executeJDBCUpdate("INSERT INTO login VALUES (\"" + username + "\",\"" + request.getParameter("newpassword") + "\");");
			response.sendRedirect(response.encodeRedirectURL("/search?newpassword=true"));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			response.sendRedirect(response.encodeRedirectURL("/newuser?status=badnewpassword"));
		}
	}
}
