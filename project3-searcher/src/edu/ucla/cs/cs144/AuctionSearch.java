package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import javax.xml.transform.*;
import java.io.StringWriter;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
        try {
            SearchResult[] searchResults;

            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index"))));
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query lQuery = parser.parse(query);
            int totalHits = numResultsToReturn + numResultsToSkip;
            if( numResultsToReturn == 0 )
                totalHits = 20000;
            TopDocs topDocs = searcher.search(lQuery, totalHits);
            
            ScoreDoc[] hits = topDocs.scoreDocs;
            if( hits.length < totalHits )
                totalHits = hits.length;
            totalHits -= numResultsToSkip;
            searchResults = new SearchResult[totalHits];
                
            for( int i=numResultsToSkip; i<hits.length; i++ ) {
                Document doc = searcher.doc(hits[i].doc);
                String itemID = doc.get("itemID");
                String name = doc.get("name");

                SearchResult s = new SearchResult();
                s.setItemId(itemID);
                s.setName(name);
                searchResults[i-numResultsToSkip] = s;
            }

            return searchResults;
        }

        catch( Exception e ) {
            System.out.println( e.getMessage() );
            return new SearchResult[0];
        }
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
        SearchResult[] basicResults = basicSearch(query, 0, 0 );
        SearchResult[] spatialResults = null;
        Connection conn = null;

        try {
            conn = DbManager.getConnection(true);
        } catch( SQLException ex ) {
            System.out.println(ex);
        }
       
        try {
            String r = "" + region.getLx() + " " + region.getLy() + ", " + region.getRx() + " " + region.getLy() + ", " + region.getRx() + " " + region.getRy() + ", " + region.getLx() + " " + region.getRy() + ", " + region.getLx() + " " + region.getLy();
            String state = "SELECT COUNT(*) From ItemGeo WHERE MBRContains(PointFromText('Polygon((" + r + "))'), g) AND ItemID=?";
            PreparedStatement prepareState = conn.prepareStatement (state);

            List<SearchResult> results = new ArrayList<SearchResult>();
            for( int i = 0; i < basicResults.length; i++ ) {
                String itemID = basicResults[i].getItemId();
                prepareState.setString( 1, itemID );
                ResultSet itemNum = prepareState.executeQuery();
                
                itemNum.next();
                if( itemNum.getInt("COUNT(*)") != 0 ) {
                    results.add(basicResults[i]);
                }
            }
            
            int length = numResultsToReturn;
            if( results.size() < numResultsToReturn + numResultsToSkip )
                length = results.size() - numResultsToSkip;

            spatialResults = new SearchResult[length];
            int i = 0;
            for( SearchResult ret : results ) {
                if( numResultsToSkip-- > 0 )
                    continue;
                spatialResults[i++] = ret;
            }

        }

        catch( SQLException ex ) {
            System.out.println(ex);
        }

        try {
            conn.close(); 
        } catch( SQLException ex ) {
            System.out.println(ex);
        }

        return spatialResults;
		//return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
        String xml = "";
        Connection conn = null;

        try {
            conn = DbManager.getConnection(true);
            Statement state = conn.createStatement();

            ResultSet result = state.executeQuery("SELECT * FROM Item WHERE ItemID=" + itemId );
            result.first();
            if( result.getRow() != 0 ) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                org.w3c.dom.Document doc   = builder.newDocument();

                // root element and all information from the Item
                Element root = doc.createElement("Item");
                root.setAttribute("ItemID", itemId);
                doc.appendChild(root);
                String userid = result.getString("UserID");
                String name = result.getString("Name");
                String currently = result.getString("Currently");
                String buyprice = result.getString("BuyPrice");
                String firstbid = result.getString("FirstBid");
                String started = result.getString("Started");
                started = timestamp(started);
                String ends = result.getString("Ends");
                ends = timestamp(ends);
                String location = result.getString("Location");
                String latitude = result.getString("Latitude");
                String longitude = result.getString("longitude");
                String country = result.getString("Country");
                String description = result.getString("Description");

                result = state.executeQuery("SELECT * FROM User WHERE UserID='" + userid + "'");
                result.first();
                String rating = result.getString("Rating");

                result = state.executeQuery("SELECT * FROM ItemCategory WHERE ItemID=" + itemId);
                List<String> categories = new ArrayList<String>();
                while( result.next() ) {
                    categories.add(result.getString("Category"));
                }
                
                result = state.executeQuery("SELECT COUNT(*) FROM Bid WHERE ItemID=" + itemId);
                result.first();
                String numberofbids = result.getString("COUNT(*)");


                // append elements
                Element element_name = doc.createElement("Name");
                element_name.appendChild(doc.createTextNode(name));
                root.appendChild(element_name);

                Element element_category = null;
                for( String category : categories ) {
                    element_category = doc.createElement("Category");
                    element_category.appendChild(doc.createTextNode(category));
                    root.appendChild(element_category);
                }

                Element element_currently = doc.createElement("Currently");
                element_currently.appendChild(doc.createTextNode(currently));
                root.appendChild(element_currently);

                Element element_buyprice = doc.createElement("Buy_Price");
                element_buyprice.appendChild(doc.createTextNode(buyprice));
                root.appendChild(element_buyprice);

                Element element_firstbid = doc.createElement("First_Bid");
                element_firstbid.appendChild(doc.createTextNode(buyprice));
                root.appendChild(element_firstbid);

                Element element_numberofbids = doc.createElement("Number_of_Bids");
                element_numberofbids.appendChild(doc.createTextNode(numberofbids));
                root.appendChild(element_numberofbids);
                
                Element element_bids = doc.createElement("Bids");
                result = state.executeQuery("SELECT * FROM Bid WHERE ItemID=" + itemId);
                while( result.next() ) {
                    String bidUser = result.getString("UserID");
                    String bidTime = result.getString("Time");
                    bidTime = timestamp(bidTime);
                    String bidAmount = result.getString("Amount");

                    Element element_bid = doc.createElement("Bid");
                    Statement bidState = conn.createStatement();
                    ResultSet bidUserResult = bidState.executeQuery("SELECT * FROM User WHERE UserID='" +
                                                                 bidUser + "'");
                    bidUserResult.first();
                    String bidUserRating = bidUserResult.getString("Rating");
                    String bidUserLocation = bidUserResult.getString("Location");
                    String bidUserCountry = bidUserResult.getString("Country");

                    Element element_biduser = doc.createElement("Bidder");
                    element_biduser.setAttribute("Rating", bidUserRating);
                    element_biduser.setAttribute("UserID", bidUser);

                    Element element_bidlocation = doc.createElement("Location");
                    element_bidlocation.appendChild(doc.createTextNode(bidUserLocation));
                    element_biduser.appendChild(element_bidlocation);

                    Element element_bidcountry = doc.createElement("Country");
                    element_bidcountry.appendChild(doc.createTextNode(bidUserCountry));
                    element_biduser.appendChild(element_bidcountry);
                    element_bid.appendChild(element_biduser);

                    Element element_time = doc.createElement("Time");
                    element_time.appendChild(doc.createTextNode(bidTime));
                    element_bid.appendChild(element_time);

                    Element element_amount = doc.createElement("Amount");
                    element_amount.appendChild(doc.createTextNode(bidAmount));
                    element_bid.appendChild(element_amount);

                    element_bids.appendChild(element_bid);
                }
                root.appendChild(element_bids);

                Element element_location = doc.createElement("Location");
                if( !latitude.equals("0") )
                    element_location.setAttribute("Latitude", latitude);
                if( !longitude.equals("0") )
                    element_location.setAttribute("Longitude", longitude);
                element_location.appendChild(doc.createTextNode(location));
                root.appendChild(element_location);
                
                Element element_country = doc.createElement("Country");
                element_country.appendChild(doc.createTextNode(country));
                root.appendChild(element_country);

                Element element_started = doc.createElement("Started");
                element_started.appendChild(doc.createTextNode(started));
                root.appendChild(element_started);

                Element element_ends = doc.createElement("Ends");
                element_ends.appendChild(doc.createTextNode(ends));
                root.appendChild(element_ends);

                Element element_seller = doc.createElement("Seller");
                element_seller.setAttribute("Rating", rating);
                element_seller.setAttribute("UserID", userid);
                root.appendChild(element_seller);

                Element element_description = doc.createElement("Description");
                element_description.appendChild(doc.createTextNode(description));
                root.appendChild(element_description);


                // transfer the Document to String
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(doc), new StreamResult(writer));
                xml = writer.getBuffer().toString();

            }

            return xml;
        }

        catch( Exception ex ) {
            System.out.println(ex);
        }

		return "";
	}
	
	public String echo(String message) {
		return message;
	}

    private static String timestamp(String date) {
        SimpleDateFormat format_in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format_out = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        StringBuffer buffer = new StringBuffer();
        
        try {
            Date parsedDate = format_in.parse(date);
            return "" + format_out.format(parsedDate);
        }
        catch(Exception pe) {
            System.err.println("Parse error");
            return "Parse error";
        }
    }

}
