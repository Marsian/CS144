package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.io.PrintWriter;
import java.util.ArrayList;


public class SearchServlet extends HttpServlet implements Servlet {
       
    private ArrayList<SearchQuery> search; 

    public SearchServlet() {
        search = new ArrayList<SearchQuery>();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        AuctionSearchClient asc = new AuctionSearchClient();
        String query;
        int numResultsToSkip;
        int numResultsToReturn;

        query = (String) request.getParameter("q");
        numResultsToSkip = Integer.parseInt( (String) request.getParameter("numResultsToSkip") );
        numResultsToReturn = Integer.parseInt( (String) request.getParameter("numResultsToReturn") );
        SearchResult[] basicResults = asc.basicSearch( query, numResultsToSkip,
                                                       numResultsToReturn );
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setQuery(query);
        searchQuery.setNumResultsToSkip(numResultsToSkip);
        searchQuery.setNumResultsToReturn(numResultsToReturn);
        
        int index = -1;
        for( int i = 0; i < search.size(); i++ ) {
            if ( query.equals( search.get(i).getQuery() ) &&
                 numResultsToSkip == search.get(i).getNumResultsToSkip() &&
                 numResultsToReturn == search.get(i).getNumResultsToReturn() ) {
                index = i;
                break;
            }
        }

        if( index < 0 ) {
            search.add( searchQuery );
            index = search.size() - 1;
        }

        request.setAttribute( "index", index );
        request.setAttribute( "search", search );
        request.setAttribute( "results", basicResults );
        request.getRequestDispatcher("/basicSearch.jsp").forward( request, response );
    }
}
