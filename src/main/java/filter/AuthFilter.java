package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;

import java.io.IOException;

public class AuthFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;

        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        boolean loggedIn = false;

        if(session != null)
        {
            Employee employee = (Employee) session.getAttribute("employee");

            loggedIn = (employee != null);
        }

        if(loggedIn)
        {
            chain.doFilter(request, response);
        }
        else
        {
            res.sendRedirect("pages/login.html");
        }
    }
}