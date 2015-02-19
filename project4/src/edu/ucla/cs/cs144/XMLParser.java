/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;


class XMLParser {
    
    private DocumentBuilder builder;
    private ItemInfo itemInfo;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    public  XMLParser() {
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        itemInfo = new ItemInfo();
    }

    
    private class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    private Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    private Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    private String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    private String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    private String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one xml String.
     */
    public ItemInfo processString(String xml) {
        Document doc = null;
        try {
            InputSource is = new InputSource( new StringReader( xml ) );
            doc = builder.parse( is );
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on String " + xml);
            System.out.println("  (not supposed to happen with supplied XML String)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed.");
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        /**************************************************************/

        //Element item = getElementByTagNameNR(doc.getDocumentElement(), "Item");
        Element item = doc.getDocumentElement();
        
        parseItem( item );
        
        return itemInfo; 
    }

    /**
     * Method to parse item information from xml data.
     * For each item, extract the itemID, sellerID, name,
     * currently price, buy price, first bid price, started date,
     * end data and description.
     * @param pass in the item Element.
     * @throws IOException if there is an error in reading in the value.
     */
    private void parseItem(Element item) {
        String itemID = item.getAttribute("ItemID");
        //String itemID = "1";
        String name = getElementTextByTagNameNR(item, "Name");
        
        Element seller = getElementByTagNameNR(item, "Seller");
        String sellerID = seller.getAttribute("UserID");
        

        String currently = strip(getElementTextByTagNameNR(item, "Currently"));
        String buyPrice = strip(getElementTextByTagNameNR(item, "Buy_Price"));
        String firstBid = strip(getElementTextByTagNameNR(item, "First_Bid"));
        
        String started = timestamp(getElementTextByTagNameNR(item, "Started"));
        String ends = timestamp(getElementTextByTagNameNR(item, "Ends"));

        Element loc = getElementByTagNameNR(item, "Location");
        String location = "" + getElementTextByTagNameNR(item, "Location");
        String country = "" + getElementTextByTagNameNR(item, "Country");
        String latitude = "" + loc.getAttribute("Latitude");
        String longitude = "" + loc.getAttribute("Longitude");

        String description = getElementTextByTagNameNR(item, "Description");
        if (description.length()>4000) {
            description = description.substring(0, 4000);
        }
        
        itemInfo.setItemId( itemID );
        itemInfo.setName( name );
        itemInfo.setSellerId( sellerID );
        itemInfo.setBuyPrice( buyPrice );
        itemInfo.setEndTime( ends );
        itemInfo.setLocation( location );
        itemInfo.setLatitude( latitude );
        itemInfo.setLongitude( longitude );
        itemInfo.setDescription( description );
    }

    /**
     * Method to parse user information from xml data.
     * First parse the user infromation from seller information. 
     * Then iterate through bidding information and extract the 
     * bidders' information.
     * @param pass in the item Element.
     * @throws IOException if there is an error in reading in the value.
     */
    private void parseUser(Element item) throws IOException {
        Element user = getElementByTagNameNR(item, "Seller");
        String userID = user.getAttribute("UserID");
        String rating = user.getAttribute("Rating");

        String location = getElementTextByTagNameNR(item, "Location");
        String country = getElementTextByTagNameNR(item, "Country");

        if (location == null)
            location = "";
        if (country == null)
            country = "";


        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), 
                                                "Bid");
        for (int i = 0; i < bids.length; i++) {
            Element bidder = getElementByTagNameNR(bids[i], "Bidder");
            userID = bidder.getAttribute("UserID");
            rating = bidder.getAttribute("Rating");
            location = getElementTextByTagNameNR(bidder, "Location");
            country = getElementTextByTagNameNR(bidder, "Country");

            if (location == null)
                location = "";
            if (country == null)
                country = "";

        }
    }

    /**
     * Method to parse category information for each item from xml data.
     * First get the itemID. Then iterate through all the categories.
     * Each time write the itemID:category data to the file.
     * @param pass in the item Element.
     * @throws IOException if there is an error in reading in the value.
     */
    private void parseCategory(Element item) throws IOException {
        String itemID = item.getAttribute("ItemID");

        Element[] categories = getElementsByTagNameNR(item, "Category");
        for( int i=0; i<categories.length; i++ ) {
            String category = getElementText(categories[i]);

        }
    }
    
    /**
     * Method to parse bid information for each bid from xml data.
     * First get all the bids for the item.
     * Iterate through the bids and extract their information.
     * @param pass in the item Element.
     * @throws IOException if there is an error in reading in the value.
     */
    private void parseBid(Element item) throws IOException {
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"),
                                                "Bid");
        String itemID = item.getAttribute("ItemID");

        for( int i=0; i<bids.length; i++ ){
            Element bidder = getElementByTagNameNR(bids[i], "Bidder");
            String userID = bidder.getAttribute("UserID");
            String bid_time = getElementTextByTagNameNR(bids[i], "Time");
            String time = "" + timestamp(bid_time);
            
            String amount = strip(getElementTextByTagNameNR(bids[i], "Amount"));

        }
    }

    /**
     * Method to convert xml format date to MySql TIMESTAMP formate data.
     * @param pass in xml formate data as String.
     * @throws IOException if there is an error in reading in the value.
     */
    private static String timestamp(String date) {
        SimpleDateFormat format_in = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat format_out = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer buffer = new StringBuffer();
                                                                                  
        try
        {
            Date parsedDate = format_in.parse(date);
            return "" + format_out.format(parsedDate);
        }
        catch(ParseException pe) {
            System.err.println("Parse error");
            return "Parse error";
        }
    }

}
