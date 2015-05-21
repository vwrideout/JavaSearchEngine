import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SearchServlet extends HttpServlet{
	private ConcurrentInvertedIndex index;
	
	public SearchServlet(ConcurrentInvertedIndex index){
		super();
		this.index = index;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(request.getParameter("query") != null){
			doPost(request,response);
			return;
		}
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		HttpSession session = request.getSession();
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Search</title><body>");
		if(request.getParameter("newpassword") != null){
			out.println("Password successfully changed.<br>");
		}
		if(session.getAttribute("username") != null){
			out.println("logged in as " + session.getAttribute("username") + ".     <a href=\"logout\">Logout</a>");
			out.println("        <a href=\"changepassword\">Change Password</a><br>");
			out.println("<a href=\"history\">View Search History</a><br>");
			out.println("<a href=\"visited\">View Visited Links</a><br>");
			out.println("<a href=\"favorite\">View Favorite Links</a><br>");
		}
		else{
			out.println("<a href=\"login\">Log in!</a>     <a href=\"newuser\">Create account</a><br>");
		}
		out.println("<form action=\"search\" method=\"post\">");
		out.println("Enter a query: <br/>");
		out.println("<input type=\"text\" name=\"query\"><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"></form>");
		suggestQueries((String)session.getAttribute("username"), out);
		out.println("</body></html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String query = request.getParameter("query");
		if(query == null || query.equals("")){
			response.sendRedirect(response.encodeRedirectURL("/search"));
		}
		
		ResultSet querycount = MyJDBC.executeJDBCQuery("SELECT * FROM querycounts WHERE query=\"" + query + "\";");
		try {
			if(querycount.first()){
				MyJDBC.executeJDBCUpdate("UPDATE querycounts SET count = count + 1 WHERE query=\"" + query + "\";");
			}
			else{
				MyJDBC.executeJDBCUpdate("INSERT INTO querycounts VALUES (\"" + query + "\",1);");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		DocumentResultList results = index.search(request.getParameter("query"));
		
		PrintWriter out = response.getWriter();
		if(session.getAttribute("username") != null){
			out.println("logged in as " + username + ".     <a href=\"logout\">Logout</a>");
			out.println("        <a href=\"changepassword\">Change Password</a><br>");
			out.println("<a href=\"history\">View Search History</a><br>");
			out.println("<a href=\"visited\">View Visited Links</a><br>");
			out.println("<a href=\"favorite\">View Favorite Links</a><br>");
			MyJDBC.executeJDBCUpdate("INSERT INTO history VALUES (\"" + username + "\",\"" + query + "\");");
		}
		else{
			out.println("<a href=\"login\">Log in!</a>     <a href=\"newuser\">Create account</a><br>");
		}
		out.println("<form action=\"search\" method=\"post\">");
		out.println("Enter a query: <br/>");
		out.println("<input type=\"text\" name=\"query\"><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"></form>");
		out.println("</body></html>");		out.println("You searched for " + query);
		for(DocumentResult result: results){
			String link = result.getName();
			out.println("<br><a href=\"visited?link=" + link + "\">" + link + "</a>");
			out.println("<a href=\"favorite?link=" + link + "\">  [Favorite]</a>");
		}
		out.println("<br>");
		suggestQueries(username, out);
		out.println("</body></html>");
	}
	
	private void suggestQueries(String username, PrintWriter out){
		ResultSet queries = MyJDBC.executeJDBCQuery("SELECT query FROM querycounts ORDER BY count DESC");
		int queriesprinted = 0;
		out.println("Suggested searches:<br>");
		try {
			while(queries.next() && queriesprinted < 5){
				String query = queries.getString("query");
				if(username != null){
					ResultSet userqueries = MyJDBC.executeJDBCQuery("SELECT * FROM history WHERE (username=\"" 
							+ username + "\" AND query=\"" + query + "\");");
					if(!userqueries.first()){
						out.println("<a href=\"search?query=" + query + "\" method=\"post\">" + query + "</a> ");
						queriesprinted++;
					}
				}
				else{
					out.println("<a href=\"search?query=" + query + "\" method=\"post\">" + query + "</a> ");
					queriesprinted++;
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
