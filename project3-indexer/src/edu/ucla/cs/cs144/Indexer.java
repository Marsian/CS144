package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    public void rebuildIndexes() {

        Connection conn = null;
        Connection c = null;

        // create a connection to the database to retrieve Items from MySQL
	    try {
	        conn = DbManager.getConnection(true);
	    } catch (SQLException ex) {
	        System.out.println(ex);
	    }

        try {
            c = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        /*
         * Add your code here to retrieve Items using the connection
         * and add corresponding entries to your Lucene inverted indexes.
             *
             * You will have to use JDBC API to retrieve MySQL data from Java.
             * Read our tutorial on JDBC if you do not know how to use JDBC.
             *
             * You will also have to use Lucene IndexWriter and Document
             * classes to create an index and populate it with Items data.
             * Read our tutorial on Lucene as well if you don't know how.
             *
             * As part of this development, you may want to add 
             * new methods and create additional Java classes. 
             * If you create new classes, make sure that
             * the classes become part of "edu.ucla.cs.cs144" package
             * and place your class source files at src/edu/ucla/cs/cs144/.
         * 
         */
        try {
            // create new indexwriter
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter indexWriter= new IndexWriter(indexDir, config);

            Statement state = conn.createStatement();
            ResultSet items = state.executeQuery("SELECT * FROM Item");
            PreparedStatement prepareState = conn.prepareStatement
            (
                 "SELECT * FROM ItemCategory WHERE ItemID=?"
            );

            int i = 0;
            while( items.next() ) {
                String itemID = "" + items.getInt("ItemID");
                if( i++ % 500 == 0 ) 
                    System.out.println( itemID + " " + i );
                String name = items.getString("Name");
                String category = null; 
                String description = items.getString("Description");

                prepareState.setString( 1, itemID );
                ResultSet categories = prepareState.executeQuery();
                while( categories.next() ) {
                    category += (categories.getString("Category") + " ");
                }
            
                String fullSearchableText = name + " " + category + " " + description;
                Document doc = new Document();
                doc.add(new StringField("itemID", itemID, Field.Store.YES));
                doc.add(new TextField("name", name, Field.Store.YES));
                doc.add(new TextField("category", category, Field.Store.YES));
                doc.add(new TextField("description", description, Field.Store.YES));
                doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
                indexWriter.addDocument(doc);
            }

            state.close();
            prepareState.close();
            indexWriter.close();
        }

        catch( Exception e ) {
            System.err.println(e.getMessage());
        }

        // close the database connection
	    try {
	        conn.close();
            c.close();
	    } catch (SQLException ex) {
	        System.out.println(ex);
	    }
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
