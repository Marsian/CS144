<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.ItemInfo" %>
<html>
    <head>
        <title>
           Pay 
        </title>
    </head>
    <body>
        <h1> Pay your item </h1>
        <%
            ItemInfo it = (ItemInfo) request.getAttribute("itemInfo");
            String itemID = it.getItemId();
            String name = it.getName();
            String buyPrice = it.getBuyPrice();
        %>
        <p>ItemID: <%= itemID %></p>
        <p>Item Name: <%= name %></p>
        <p>Buy Price: <%= buyPrice %></p>
        <form action="confirm" method="GET">
            Credit Card:
            <input type="text" name="card">
            <br>
            <input type="submit" value="Submit">
        </form>

    </body>
</html>
