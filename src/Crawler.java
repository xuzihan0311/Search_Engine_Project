/* --
COMP4321 Project
Crawler.java
*/
package comp4321;
import java.util.Vector;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.htmlparser.beans.StringBean;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.StringTokenizer;

import org.htmlparser.beans.LinkBean;

import java.net.URL;


public class Crawler
{
	private String url;
	Crawler(String _url)
	{
		url = _url;
	}
	public Vector<String> extractWords() throws ParserException

	{
		// extract words in url and return them
		// use StringTokenizer to tokenize the result from StringBean
		// ADD YOUR CODES HERE
		Vector<String> result = new Vector<String>();
		StringBean bean = new StringBean();
		bean.setURL(url);
		bean.setLinks(false);
		String contents = bean.getStrings();
		StringTokenizer st = new StringTokenizer(contents);
		while (st.hasMoreTokens()) {
		    result.add(st.nextToken());
		}
		return result;
	}
	public Vector<String> extractLinks() throws ParserException

	{
		// extract links in url and return them
		// ADD YOUR CODES HERE
		Vector<String> result = new Vector<String>();
		LinkBean bean = new LinkBean();
		bean.setURL(url);
		URL[] urls = bean.getLinks();
		for (URL s : urls) {
		    result.add(s.toString());
		}
		return result;
	}
	
	public static void main (String[] args)
	{
		try
		{
			// Create a RecordManager called "RM"
			RecordManager recman = RecordManagerFactory.createRecordManager("RM");
			mappingURL index = new mappingURL(recman,"mappingURL");
			
			// Add the parent link to the mappingURL Table
			index.addEntry("1","http://www.cse.ust.hk");
			
			// Do crawler work to find the words and links included in the page
			Crawler crawler = new Crawler("http://www.cse.ust.hk");

			
			/*Vector<String> words = crawler.extractWords();		
			
			System.out.println("Words in "+crawler.url+":");
			for(int i = 0; i < words.size(); i++)
				System.out.print(words.get(i)+" ");
			System.out.println("\n\n");*/
			

			// Extract the links in the page
			Vector<String> links = crawler.extractLinks();
			// Set 
			int id = 2;
			// Repeatedly add the childLink into the mappingURL table
			for(int i = 0; i < links.size(); i++)	
			{
				// The url is added successfully, increase the pageId by 1
				if(index.addEntry(Integer.toString(id),links.get(i)));
					id++;
			}
			index.printALL();
			
			
			// Add first 30 page into PageStruc
			for(int i=1;i<31;i++)
			{
				PageStruc struc = new PageStruc(Integer.toString(i),recman,"PageStruc");
				struc.setTitle();
				struc.setLmDate();
				struc.setSize();
				//struc.setNode(links);
				struc.printAll();
				System.out.println("------------------------------------------------------");
			}
			index.finalize();
			System.out.println();
			
		}
		catch (Exception e)
            	{
			 		System.err.println(e.toString());
            	}

	}
}
	
