package util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ToastUtil
{
    public static void showToast(HttpServletResponse response, PrintWriter out,
                                 int statusCode, String type,
                                 String message, String redirectUrl) throws IOException
    {
        response.setStatus(statusCode);

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");

        out.println("<title>Message</title>");

        out.println("<link rel='stylesheet' href='css/toast.css'>");

        out.println("</head>");

        out.println("<body>");

        out.println("<div class='toast " + type + "'>");
        out.println(message);
        out.println("</div>");

        out.println("<script>");
        out.println("setTimeout(function(){");
        out.println("window.location.href='" + redirectUrl + "';");
        out.println("},3000);");
        out.println("</script>");

        out.println("<script src='js/toast.js'></script>");

        out.println("</body>");
        out.println("</html>");
    }
}