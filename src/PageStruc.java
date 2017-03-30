/* --
COMP4321 Phase1
PageStruc
*/
package comp4321;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

import java.io.IOException;

import org.htmlparser.util.ParserException;

import java.util.Date;
import java.util.Scanner;
import java.util.Vector;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.net.URLConnection;
import java.net.URL;

public class PageStruc
{
    private String pageID;
    private String url;
    private String title;
    private long lmDate;
    private int size;
    private int numOfChild;
    private RecordManager recman;
    private HTree hashtable;
    private Vector<String> node;
    

    // Default Constructor
    PageStruc(String id,RecordManager rec, String objectName) throws IOException
    {
       	pageID = id;
		recman = rec;
		long recid = rec.getNamedObject(objectName);
		node = new Vector<String>();
		numOfChild = 0;
	
		// load the hash table named with objectName from the RecordManager
		if(recid != 0)
			hashtable = HTree.load(recman, recid);
		// If it does not exist, create a new hash table "PageStruct" in the RecordManager
		else
		{
			hashtable = HTree.createInstance(recman);
			recman.setNamedObject("PageStruc",hashtable.getRecid());
		}
		// Get the mappingURL hash table
		long mapid = rec.getNamedObject("mappingURL");
		HTree mappingTable = HTree.load(recman, mapid);
		// Initialize all data members
		url = (String)mappingTable.get(pageID);        
		title = null;
		lmDate = 0;
		size = 0;
    }

    public void setTitle()
    {
		// scan the <title> in the page and get the title to initialize the data member of title.
		InputStream response = null;
		try{
		   response = new URL(url).openStream(); //read the content of the URL
		   
		   Scanner scanner = new Scanner(response);
		   String responseBody = scanner.useDelimiter("\\A").next();
	   	   title = responseBody.substring(responseBody.indexOf("<title>")+7, responseBody.indexOf("</title>"));
		}catch(IOException ex){ex.printStackTrace();}
		finally{
				try{
					response.close();
					}catch(IOException ex){ex.printStackTrace();}
		}
    }			                   
 

    public void setLmDate() throws IOException
    {
        URL u = new URL(url);
        URLConnection connection = u.openConnection();
        // Get the Last Modified Date by reading the last modified date field in the page
        lmDate = connection.getLastModified();
        // If it does not exist, then read the date field 
        if (lmDate == 0)
        	lmDate = connection.getDate();
    }

    public void setSize() throws ParserException,IOException
    {
		URL u = new URL(url);
		URLConnection connection = u.openConnection();
		// Get the size of page by reading content-length header field in the page
		size = connection.getContentLength();
		// If it does not exist, then directly count the number of characters
		if(size == -1)
		{	
			StringExtractor se = new StringExtractor(url);
			String length = se.extractStrings(false);
			size = length.length();
		}    
    } 
    
    public void setNode(Vector<String> LinkE) throws ParserException,IOException
    {
    	//Crawler craw = new Crawler(url);
    	//Vector<String> LinkE = craw.extractLinks();
    	numOfChild = LinkE.size();
    	//System.out.println("Num of Child :" +  numOfChild);
    	for(int i=0; i<numOfChild; i++){
    		node.addElement(LinkE.get(i));
    	}
    } 

	public Vector<String> getNode() throws IOException
    {
    	return node;
    }
    
    public String getTitle() throws IOException
    {
    	return title;
    }

    public long getLmDate() throws IOException
    {
    	return lmDate;
    }

    public int getSize() throws IOException
    {
    	return size;
    }

    public void printAll() throws IOException
    {
    	// Print out all the information of the page
       System.out.println("Page title: " + title);
       System.out.println("URL: " + url);
       SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
       Date lm = new Date(lmDate);
       System.out.println("Last modification date: " + sdf.format(lm));
       System.out.println("Size: " + size);
       /*System.out.print("Child links: ");
       for(int i = 0; i<numOfChild; i++){
    	   System.out.println(node.get(i));
       }*/
    }   
 /*
	public static void main(String[] args) throws ParserException,IOException
	{
	    RecordManager recman;
        HTree hashtable;
        recman = RecordManagerFactory.createRecordManager("testRM");
        long recid = recman.getNamedObject("mappingURL");
        if (recid != 0)
        {
           hashtable = HTree.load(recman, recid);
        }
        else
        {
           hashtable = HTree.createInstance(recman);
           recman.setNamedObject( "mappingURL", hashtable.getRecid() );
        }

        hashtable.put("1", "http://www.cse.ust.hk");
	    PageStruc st = new PageStruc("1",recman,"mappingURL");
	    st.setTitle();
	    st.setLmDate();
	    st.setSize();
	    st.printAll();
	    recman.commit();
	    recman.close();
	}*/

}
