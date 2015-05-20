import java.io.IOException;
import java.io.PrintWriter;

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
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		HttpSession session = request.getSession();
		
		if(request.getParameter("query") != null){
			this.doPost(request, response);
			return;
		}
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Search</title><body>");
		if(session.getAttribute("username") != null){
			out.println("logged in as " + session.getAttribute("username") + ".     <a href=\"logout\">Logout</a><br>");
			out.println("<a href=\"history\">View Search History</a><br>");
		}
		else{
			out.println("<a href=\"login\">Log in!</a>     <a href=\"newuser\">Create account</a><br>");
		}
		out.println("<form action=\"search\" method=\"post\">");
		out.println("Enter a query: <br/>");
		out.println("<input type=\"text\" name=\"query\"><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"></form>");
		out.println("</body></html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String query = request.getParameter("query");
		if(query == null || query.equals("")){
			response.sendRedirect(response.encodeRedirectURL("/search"));
		}
		
		HttpSession session = request.getSession();
		DocumentResultList results = index.search(request.getParameter("query"));
		
		PrintWriter out = response.getWriter();
		if(session.getAttribute("username") != null){
			out.println("logged in as " + session.getAttribute("username") + ".     <a href=\"logout\">Logout</a><br>");
			out.println("<a href=\"history\">View Search History</a><br>");
			MyJDBC.executeJDBCUpdate("INSERT INTO history VALUES (\"" + session.getAttribute("username") + "\",\"" + query + "\");");
		}
		else{
			out.println("<a href=\"login\">Log in!</a>     <a href=\"newuser\">Create account</a><br>");
		}
		out.println("<form action=\"search\" method=\"post\">");
		out.println("Enter a query: <br/>");
		out.println("<input type=\"text\" name=\"query\"><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"></form>");
		out.println("</body></html>");		out.println("You searched for " + request.getParameter("query"));
		for(DocumentResult result: results){
			out.println("<br><a href=\"" + result.getName() + "\">" + result.getName() + "</a>");
		}
		out.println("</body></html>");
	}
}
