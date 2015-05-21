import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class VisitedServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(request.getParameter("link") != null){
			HttpSession session = request.getSession();
			if(session.getAttribute("username") != null){
				MyJDBC.executeJDBCUpdate("INSERT INTO visited VALUES (\"" + session.getAttribute("username") + "\",\"" + request.getParameter("link") + "\");");
			}
			response.sendRedirect(response.encodeRedirectURL(request.getParameter("link")));
		}
		else{
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			
			String username = (String)request.getSession().getAttribute("username");
			if(request.getParameter("clear") != null && request.getParameter("clear").equals("true")){
				MyJDBC.executeJDBCUpdate("DELETE FROM visited WHERE username=\"" + username + "\";");
			}
			ResultSet results = MyJDBC.executeJDBCQuery("SELECT url FROM visited WHERE username=\"" + username + "\";");
			
			PrintWriter out = response.getWriter();
			out.println("<html><title>Visited</title><body>");
			out.println("Visited Links for " + username + ":<br>");
			try {
				if(!results.last()){
					out.println("No links visited.<br>");
				}
				else{
					out.println("<a href=\"" + results.getString("url") + "\">" + results.getString("url") + "</a><br>");
					while(results.previous()){
						out.println("<a href=\"" + results.getString("url") + "\">" + results.getString("url") + "</a><br>");
					}
				}
				out.println("-----------------<br>");
				out.println("<a href=\"visited?clear=true\">Clear visited links</a>");
				out.println("<a href=\"search\">Home</a>");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

	}
}
