import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet{
	private ConcurrentInvertedIndex index;
	
	public SearchServlet(ConcurrentInvertedIndex index){
		super();
		this.index = index;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Search</title><body>");
		out.println("<a href=\"login\">Log in!</a>     <a href=\"newuser\">Create account</a><br>");
		out.println("<form action=\"search\" method=\"post\">");
		out.println("Enter a query: <br/>");
		out.println("<input type=\"text\" name=\"query\"><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"></form>");
		out.println("</body></html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		DocumentResultList results = index.search(request.getParameter("query"));
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Results</title><body>");
		out.println("<a href=\"login\">Log in!</a>     <a href=\"newuser\">Create account</a><br>");
		out.println("<form action=\"search\" method=\"post\">");
		out.println("Enter a query: <br/>");
		out.println("<input type=\"text\" name=\"query\"><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"></form>");
		out.println("You searched for " + request.getParameter("query"));
		for(DocumentResult result: results){
			out.println("<br><a href=\"" + result.getName() + "\">" + result.getName() + "</a>");
		}
		out.println("</body></html>");
	}
}
