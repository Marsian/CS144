package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        AuctionSearchClient asc = new AuctionSearchClient();
        
        String id = (String) request.getParameter("id");
        String xml = asc.getXMLDataForItemId( id );
        
        ItemInfo it = new ItemInfo();
        it.setItemId("123");
        it.setName("H");
        it.setDescription(xml);
        request.setAttribute( "itemInfo", it );
        request.getRequestDispatcher("/item.jsp").forward( request, response );
    }
}
