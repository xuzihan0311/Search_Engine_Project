/* --
COMP4321 Group Project
mappingURL.java
*/

package comp4321;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;
import java.io.IOException;


public class mappingURL {
	private RecordManager recman;
	private HTree hashtable;

	public mappingURL(RecordManager rec,String objectName) throws IOException
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
			recman.setNamedObject( "mappingURL", hashtable.getRecid() );
		}
	}

	public void finalize() throws IOException
	{
		// commit the changes
		recman.commit();
		// close the RecordManager
		recman.close();				
	} 

	public boolean addEntry(String pageId, String url) throws IOException
	{
		// Add a "url" entry for the key "pageId" into hashtable
		// If the url is existed already,do not add a duplicate entry
		if(isExist(url) == true)
				return false;
		// If the url is not existed yet, add a new entry
		hashtable.put(pageId,url);
		return true;
	}
	public void delEntry(String pageId) throws IOException
	{
		// Delete an entry with specified pageId
		hashtable.remove(pageId);
	} 
	public boolean isExist(String url) throws IOException
	{
		// Check the specified URL is exist or not
		// If yes, return true. If no, return fasle
		FastIterator iter = hashtable.keys();
		String key;
		while ( (key = (String)iter.next()) != null){
			String link = (String) hashtable.get(key);
			if(link.equals(url))
				return true;
		}	
		return false;
	}	

	public String getURL(String pageID) throws IOException
	{
		// return the entry with specified pagrID
		String url = (String)hashtable.get(pageID);
		if(url == null)
			return "Entry does not found";
		else
			return url;
	}
	public String getID(String url) throws IOException
	{
		FastIterator iter = hashtable.keys();
		String key;
		while( (key = (String)iter.next())!=null)
        {
           if(hashtable.get(key).equals(url))
        	   	return key;
        }
		return null;
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
                   System.out.println(key + " : " + hashtable.get(key));
                }
	}	
	

	/*public static void main(String[] args)
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
