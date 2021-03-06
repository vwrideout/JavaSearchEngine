import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jetty Servlet implementing the logout process for user accounts.
 * @author Vincent Rideout
 *
 */
public class LogoutServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		request.getSession().setAttribute("username", null);
		response.sendRedirect(response.encodeRedirectURL("/search"));
	}
}
