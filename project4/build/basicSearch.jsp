<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="edu.ucla.cs.cs144.SearchQuery" %>
<%@ page import="java.util.ArrayList" %>

<html>
    <head>
        <title>
            Basic Search
        </title>
    </head>
    <body>
        <h1> Basic Search </h1>
        <h2> New Search </h2>
        <form action="search" method="GET">
            Keyword<br>
            <input type="text" name="q">
            <br>
            Number of Results to Skip<br>
            <input type="text" name="numResultsToSkip" value="0">
            <br>
            Number of Results to Return<br>
            <input type="text" name="numResultsToReturn" value="100">
            <br><br>
            <input type="submit" value="Submit">
        </form>

        <h2> Results </h2>
        <table border=2>
            <tr>
                <td>Number</td><td>ItemID</td><td>Name</td>
            </tr>
            <%  
                int i = 1;
                SearchResult[] results = (SearchResult[]) request.getAttribute("results");
                for ( SearchResult result : results ) {
                %>
                    <tr>
                        <td><%= i %></td>
                        <td><a href="item?id=<%= result.getItemId() %>" ><%= result.getItemId() %></a></td>
                        <td><%= result.getName() %></td>
                    </tr>
                <%
                    i ++;
                }
            %>
        </table>    

        <%
            ArrayList<SearchQuery> search = (ArrayList<SearchQuery>) request.getAttribute("search");
            int index = (Integer) request.getAttribute("index");
                    %>
                        <h1><%= index %></h1>
                    <%
            if ( !search.isEmpty() ) {
               if ( index >= 0 && index < search.size() - 1 ) {
                    String nextQuery = "search?";
                    nextQuery += "q=" + search.get(index+1).getQuery();
                    nextQuery += "&numResultsToSkip=" + Integer.toString( search.get(index+1).getNumResultsToSkip() );
                    nextQuery += "&numResultsToReturn=" + Integer.toString( search.get(index+1).getNumResultsToReturn() );
                    %>
                        <button type="button" onClick="location.href='<%= nextQuery %>'">Next</button>
                    <%
               } else {
                    %>
                        <button type="button" onclick="alert('Already the newest search!')">Next</button>
                    <%
               }

               if( index > 0 ) {
                    String preQuery = "search?";
                    preQuery += "q=" + search.get(index-1).getQuery();
                    preQuery += "&numResultsToSkip=" + Integer.toString( search.get(index-1).getNumResultsToSkip() );
                    preQuery += "&numResultsToReturn=" + Integer.toString( search.get(index-1).getNumResultsToReturn() );
                    %>
                        <button type="button" onClick="location.href='<%= preQuery %>'">Previous</button>
                    <%

               } else {
                    %>
                        <button type="button" onclick="alert('Already the oldest search!')">Previous</button>
                    <%
               }
            }
        %>

        <table border=2>
            <tr>
                <td>Query</td><td>n1</td><td>n2</td>
            </tr>
            <%  
                for ( SearchQuery s : search ) {
                %>
                    <tr>
                        <td><%= s.getQuery() %></td>
                        <td><%= s.getNumResultsToSkip() %></td>
                        <td><%= s.getNumResultsToReturn() %></td>
                    </tr>
                <%
                }
            %>
        </table> 
    </body>
</html>
