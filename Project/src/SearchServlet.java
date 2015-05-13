import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>Search</title><body>");
		out.println("<form action=\"results\" method=\"post\">");
		out.println("Enter a query: <br/>");
		out.println("<input type=\"text\" name=\"query\"><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"></form>");
		out.println("</body></html>");
	}
}
