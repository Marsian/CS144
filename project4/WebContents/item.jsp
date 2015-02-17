<%@ page import="edu.ucla.cs.cs144.ItemInfo" %>
<html>
    <head>
        <title>
            Item Information 
        </title>
    </head>
    <body>
        <h1> Item Information </h1>
        <h2> New Search </h2>
        <form action="item" method="GET">
            itemID<br>
            <input type="text" name="id">
            <br><br>
            <input type="submit" value="Submit">
        </form>

        <h2> Results </h2>
        <table border=2>
            <tr>
                <td>Entry</td><td>Value</td>
            </tr>
            <%
                ItemInfo itemInfo = (ItemInfo) request.getAttribute("itemInfo");
                %>
                    <tr>
                        <td>ItemID</td>
                        <td><%= itemInfo.getItemId() %></td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td><%= itemInfo.getName() %></td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td><%= itemInfo.getDescription() %></td>
                    </tr>
                <%
            %>
        </table>    
    </body>
</html>
