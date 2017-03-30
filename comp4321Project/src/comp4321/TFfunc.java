/* --
COMP4321 Group Project
TFfunc.java
*/
package comp4321;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.io.IOException;
import java.io.Serializable;


public class TFfunc
{
	private RecordManager recman;
	private HTree hashtable;
	private String keyword;
	private int wordID;

	TFfunc(RecordManager rec,String objectName) throws IOException
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
			recman.setNamedObject( "mappingKeyword", hashtable.getRecid() );
		}
	}


	public void finalize() throws IOException
	{
		// commit the changes
		recman.commit();
		// close the RecordManager
		recman.close();				
	} 

	public void addEntry(String key) throws IOException
	{
		// Call isExist()
		// If return false, add a "wordID" entry for the key "keyword" into hashtable
		// Else, do nothing 
		if(isExist(keyword)== false){	
		   keyword = key;
		   //Do the hash function to generalize a wordID
		   wordID = hashFunc();
		   hashtable.put(keyword,wordID);
		}
	}
	public void delEntry(String keyword) throws IOException
	{
		// call isExist()
		// Return true, delete an entry with specified pageId
		// Else, do nothing
		if(isExist(keyword) == true)
		   hashtable.remove(keyword);
	}
 
	public int hashFunc() throws IOException
	{
		// Do the hashFunc to convert the keyword(data member) into word ID
		int id = 0;
		int i = 0;
		int prime = 101;
		char[] charArray = keyword.toCharArray();
		for(char temp: charArray){
		   id += temp * Math.pow(prime,i);
		   i++;
		}
		return id;
	}

	public boolean isExist(String keyword) throws IOException
	{
		// Check the specified keyword is exist or not
		// If yes, return true. If no, return fasle
		FastIterator iter = hashtable.keys();
		String key;
		while ( (key = (String)iter.next()) != null){
			String word = keyword;
			if(word.equals(key))
				return true;
		}	
		return false;
	}	

	public int getWordID(String keyword) throws IOException
	{
		// return the entry with specified wordID
		//String id = hashtable.get(keyword);
		//if(id != 0)
		//	return id;
		return 0;
	}

	public FastIterator getKeyowrd(int id) throws IOException
	{	
		// Return a keyword of the hashtable
		FastIterator iter = hashtable.keys();
		String key;
		while((key = (String)iter.next())!= null){
			return iter;
		}
		return null;
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
}	
