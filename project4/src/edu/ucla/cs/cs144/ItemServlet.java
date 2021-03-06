package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        HttpSession session = request.getSession(true);
        AuctionSearchClient asc = new AuctionSearchClient();
        
        String id = (String) request.getParameter("id");
        String xml = asc.getXMLDataForItemId( id );
        
        XMLParser xp = new XMLParser();
        ItemInfo it = xp.processString( xml );
        //it.setItemId("123");
        //it.setName("H");
        //it.setDescription(xml);
        request.setAttribute( "itemInfo", it );
        session.setAttribute( "itemInfo", it );
        request.getRequestDispatcher("/item.jsp").forward( request, response );
    }
}
