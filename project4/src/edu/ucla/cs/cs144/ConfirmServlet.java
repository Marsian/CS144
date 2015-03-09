package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class ConfirmServlet extends HttpServlet implements Servlet {
       
    public ConfirmServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        HttpSession session = request.getSession( true );
        ItemInfo it = (ItemInfo)session.getAttribute( "itemInfo" );
        String card = (String) request.getParameter("card");

        request.setAttribute( "itemInfo", it );
        request.setAttribute( "card", card );
        request.getRequestDispatcher("/confirm.jsp").forward( request, response );
        /*
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println( "Almost There" );
        out.println( it.getName() );
        */
    }
}
