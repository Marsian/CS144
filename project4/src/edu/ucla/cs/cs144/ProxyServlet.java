package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String query = (String) request.getParameter( "query" );
        query = URLEncoder.encode( query, "UTF-8" );
        String url = "http://google.com/complete/search?output=toolbar&q=" + query;
        URL obj = new URL( url );
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()) );
        String inputLine;
        StringBuffer buffer = new StringBuffer();
 
        while ( (inputLine = in.readLine()) != null ) {
            buffer.append(inputLine);
        }
        in.close();
 
        String result = buffer.toString();
        PrintWriter out = response.getWriter();
        out.println( result );
        out.close();
    }
}
