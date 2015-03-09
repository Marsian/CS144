<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.ItemInfo" %>
<html>
    <head>
        <title>
            Confirmation
        </title>
    </head>
    <body>
        <h1> Your Purchase </h1>
        <%
            ItemInfo it = (ItemInfo) request.getAttribute("itemInfo");
            String itemID = it.getItemId();
            String name = it.getName();
            String buyPrice = it.getBuyPrice();
            
            String card = (String) request.getAttribute("card");
        %>
        <p>ItemID: <%= itemID %></p><br>
        <p>Item Name: <%= name %></p><br>
        <p>Buy Price: <%= buyPrice %></p><br>
        <p>Credit Card: <%= card %></p><br>
    </body>
</html>
