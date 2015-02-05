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


class MyParser {
    
    static DocumentBuilder builder;

    private static BufferedWriter itemFileWriter;
    private static BufferedWriter userFileWriter;
    private static BufferedWriter categoryFileWriter;
    private static BufferedWriter bidFileWriter;
    private static int bidID = 0;
    
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
    
    static class MyErrorHandler implements ErrorHandler {
        
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
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
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
    static Element getElementByTagNameNR(Element e, String tagName) {
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
    static String getElementText(Element e) {
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
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
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
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        /**************************************************************/

        Element[] items = getElementsByTagNameNR(doc.getDocumentElement(), "Item");
        
        try {
            for(int i = 0; i < items.length; i++) {
                parseItem(items[i]);
                parseUser(items[i]);
                parseCategory(items[i]);
                parseBid(items[i]);
            }
        }

        catch(IOException e) {
            e.printStackTrace();
        }   
        
        
    }

    /**
     * Method to parse item information from xml data.
     * For each item, extract the itemID, sellerID, name,
     * currently price, buy price, first bid price, started date,
     * end data and description.
     * @param pass in the item Element.
     * @throws IOException if there is an error in reading in the value.
     */
    static void parseItem(Element item) throws IOException {
        String itemID = item.getAttribute("ItemID");
        
        Element seller = getElementByTagNameNR(item, "Seller");
        String sellerID = seller.getAttribute("UserID");
        
        String name = getElementTextByTagNameNR(item, "Name");

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
        
        writeToFile(itemFileWriter, itemID, sellerID, name, currently, buyPrice, 
                    firstBid, started, ends, country, latitude, longitude, 
                    country, description);
    }

    /**
     * Method to parse user information from xml data.
     * First parse the user infromation from seller information. 
     * Then iterate through bidding information and extract the 
     * bidders' information.
     * @param pass in the item Element.
     * @throws IOException if there is an error in reading in the value.
     */
    static void parseUser(Element item) throws IOException {
        Element user = getElementByTagNameNR(item, "Seller");
        String userID = user.getAttribute("UserID");
        String rating = user.getAttribute("Rating");

        String location = getElementTextByTagNameNR(item, "Location");
        String country = getElementTextByTagNameNR(item, "Country");

        if (location == null)
            location = "";
        if (country == null)
            country = "";

        writeToFile(userFileWriter, userID, rating, location, country);

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

            writeToFile(userFileWriter, userID, rating, location, country);
        }
    }

    /**
     * Method to parse category information for each item from xml data.
     * First get the itemID. Then iterate through all the categories.
     * Each time write the itemID:category data to the file.
     * @param pass in the item Element.
     * @throws IOException if there is an error in reading in the value.
     */
    static void parseCategory(Element item) throws IOException {
        String itemID = item.getAttribute("ItemID");

        Element[] categories = getElementsByTagNameNR(item, "Category");
        for( int i=0; i<categories.length; i++ ) {
            String category = getElementText(categories[i]);

            writeToFile(categoryFileWriter, itemID, category);
        }
    }
    
    /**
     * Method to parse bid information for each bid from xml data.
     * First get all the bids for the item.
     * Iterate through the bids and extract their information.
     * @param pass in the item Element.
     * @throws IOException if there is an error in reading in the value.
     */
    static void parseBid(Element item) throws IOException {
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"),
                                                "Bid");
        String itemID = item.getAttribute("ItemID");

        for( int i=0; i<bids.length; i++ ){
            Element bidder = getElementByTagNameNR(bids[i], "Bidder");
            String userID = bidder.getAttribute("UserID");
            String bid_time = getElementTextByTagNameNR(bids[i], "Time");
            String time = "" + timestamp(bid_time);
            
            String amount = strip(getElementTextByTagNameNR(bids[i], "Amount"));

            writeToFile(bidFileWriter,"" + bidID++, userID, itemID, time, amount);
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

    /**
     * Method to convert the input strings to MySql readible data.
     * Combine all the strings to one line and, 
     * and add column seperators.
     * @param pass in all the information as strings.
     */
    private static String formatRow(String[] input) {
        String formattedOutput = "";
        
        int i = 0;
        for (; i < input.length-1; i++) {
            formattedOutput += input[i] + "|*|";
        }
        formattedOutput += input[i];

        return formattedOutput;
    }
    
    /*
     * Method to formate the strings and write them to the data file.
     */
    private static void writeToFile(BufferedWriter output, String... args) throws IOException {
        output.write(formatRow(args));
        output.newLine();
    }

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
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
        
        try {
            itemFileWriter = new BufferedWriter(new FileWriter("item.dat",true));
            userFileWriter = new BufferedWriter(new FileWriter("user.dat",true));
            categoryFileWriter = new BufferedWriter(new FileWriter("category.dat",true));
            bidFileWriter = new BufferedWriter(new FileWriter("bid.dat",true));

            /* Process all files listed on command line. */
            for (int i = 0; i < args.length; i++) {
                File currentFile = new File(args[i]);
                processFile(currentFile);
            }
            
            itemFileWriter.close();
            userFileWriter.close();
            categoryFileWriter.close();
            bidFileWriter.close();
        }

        catch(IOException e) {
            e.printStackTrace();    
        }
    }
}
