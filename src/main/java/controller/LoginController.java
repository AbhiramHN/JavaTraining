package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import service.AuthenticationService;
import util.ToastUtil;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginController extends HttpServlet
{
    private AuthenticationService authenticationService;

    @Override
    public void init()
    {
        authenticationService = new AuthenticationService();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException
    {
        String employeeId = request.getParameter("employeeId");

        String password = request.getParameter("password");

        Employee employee = authenticationService.login(employeeId, password);
        PrintWriter out = response.getWriter();
        if(employee != null)
        {
            HttpSession session = request.getSession();

            session.setAttribute("employee", employee);

            ToastUtil.showToast(response, out, HttpServletResponse.SC_OK,
                    "success", "Login Successful", "dashboard");
        }
        else
        {
            ToastUtil.showToast(response, out, HttpServletResponse.SC_UNAUTHORIZED,
                    "error", "Invalid Employee ID or Password", "pages/login.html");
        }
    }
}