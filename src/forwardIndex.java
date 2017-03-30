/* --
COMP4321 Group Project
fowardIndex.java
*/

package comp4321;

import java.io.*;
import java.net.URL;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;
import java.util.Vector;
import java.io.IOException;
import java.io.Serializable;


public class forwardIndex {
	private RecordManager recman;
	private HTree hashtable;

	public forwardIndex(RecordManager rec,String objectName) throws IOException
	{
		recman = rec;
		long recid = rec.getNamedObject(objectName);
		if (recid != 0)
			// load the hash table from the RecordManager
			hashtable = HTree.load(recman, recid);
		else
		{
			// Create a hash table in the RecordManager
			hashtable = HTree.createInstance(recman);
			// Set the name of the hash table to "mappingURL"
			recman.setNamedObject( "forwardIndex", hashtable.getRecid() );
		}
	}


	public void finalize() throws IOException
	{
		// commit the changes
		recman.commit();
		// close the RecordManager
		recman.close();				
	} 
	/*
	public void addEntry(String pageId, Vector<String> child) throws IOException
	{
		// Add a "child_link" entry for the key "pageId" into hashtable
		hashtable.put(pageId,child);
	}
	public void delEntry(String pageId) throws IOException
	{
		// Delete an entry with specified pageId
		hashtable.remove(pageId);
	} 
	public boolean isChildExist(String pageId,String url) throws IOException
	{
		// Check the specified URL is exist or not
		// If yes, return true. If no, return fasle
		Vector<String> childLink = hashtable.get(pageId);
		for(int i=0;i<childLink.size();i++)
		{
			if(childLink.get(i).equals(url))
				return true;
		
		}	
		return false;
	}	

	public Vector<String> getChild(String pageID) throws IOException
	{
		// return the entry with specified pagrID
		Vector<String> childLink = hashtable.get(pageID);
		if(childLink.size() == 0)
			return "Child Link does not found";
		else
			return childLink;
	}

	public FastIterator getKeys() throws IOException
	{	
		// Return an enumeration of the hashtable
		FastIterator iter = hashtable.keys();
		return iter;
	}

	public void printALL() throws IOException
	{
		// Print out all the key value pairs in the hashtable
		FastIterator iter = hashtable.keys();
                String key;
                while( (key = (String)iter.next())!=null)
                {
		   Vector<String> childLink = hashtable.get(key);
		   Ststem.out.println("Pageid: " + key);
	           for(int i=0;i<childLink.size();i++)
		   {
                   	System.out.println(childLink.get(i));
  		   } 
               }
	}	
	

	public static void main(String[] args)
	{
		try
		{
			RecordManager recman = RecordManagerFactory.createRecordManager("RM");
			mappingURL index = new mappingURL(recman,"mappingURL");
			index.addEntry("1","http://www.cse.ust.hk");
			System.out.println("First print");
			index.printALL();

			index.addEntry("2", "http://course.cse.ust.hk/comp4321");
			System.out.println("Second print");
			index.printALL();

			System.out.println("Third print");
			index.addEntry("3", "http://course.cse.ust.hk/comp4651");
			index.delEntry("2");
			index.printALL();

			index.finalize();
		}
		catch(IOException ex)
		{
			System.err.println(ex.toString());
		}

	}*/

}
