/* --
COMP4321 Project
Crawler.java
*/
package comp4321;
import java.text.SimpleDateFormat;
import java.util.Vector;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;


public class Crawler
{
	private String url;
	private String pageId;
	Crawler(String _url)
	{
		url = _url;
		pageId = null;
	}
	public void setPageId(String id) 
	{
		pageId = id;
	}
	public String getPageId()
	{
		return pageId;
	}
	public String getURL()
	{
		return url;
	}
	public Vector<String> extractWords() throws ParserException

	{
		// extract words in url and return them
		// use StringTokenizer to tokenize the result from StringBean
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
			// Create a InvertedIndex hashmap for storing the PageId with corresponding keyword and TF
			HashMap<String, List<wordTF>> invertedIndex = new HashMap<String, List<wordTF>>();
			
			mappingURL index = new mappingURL(recman,"mappingURL");
			
			// Add the parent link to the mappingURL Table
			index.addEntry("1","http://www.cse.ust.hk");
			
			// Do crawler work to find the words and links included in the page
			Crawler crawler = new Crawler("http://www.cse.ust.hk");
			crawler.setPageId(index.getID(crawler.getURL()));
			// Extract the words in the page
			Vector<String> words = crawler.extractWords();		
			// Create a mappingKeyword 
			mappingKeyword keyW = new mappingKeyword(recman,"mappingKeyword");
			// Find the keywords
			//Vector<String> keyWord = new Vector<String>();
			// Read the stop words listed in "stopwords.txt"
			StopStem stopStem = new StopStem("stopwords.txt");
			for(int i = 0; i < words.size(); i++)
			{
				// If the word is a stop word, ignore it and go to next iteration of loop
				if (stopStem.isStopWord(words.get(i)))
				{
					continue;
				}
				// If the word is not a stop word, stem it and add it to the mappingKeyword Table
				else
				{
		   			String key = stopStem.stem(words.get(i));
					keyW.addEntry(key);
					// If the word is already exist in the inverted index table
					if(invertedIndex.containsKey(key))
					{
						// Find out the wordTF structure and add its TF by 1
						for(int j=0;j<invertedIndex.get(key).size();j++)
						{
							if(invertedIndex.get(key).get(j).getPageId().equals(crawler.getPageId()))
								invertedIndex.get(key).get(j).addTF();
						}
					}
					 //If the word is not exist, put a new entry to the inverted index table
					else
					{
						List<wordTF> wordlist = new ArrayList<wordTF>();
						wordTF word = new wordTF(crawler.getPageId());
						wordlist.add(word);
						invertedIndex.put(key, wordlist);
					}

				}
			}
			keyW.printALL();
			System.out.println("\n\n");
		
			// Extract the links in the page
			Vector<String> links = crawler.extractLinks();
			// Set the pageId of the childLink
			int id = 2;
			// Repeatedly add the childLink into the mappingURL table
			for(int i = 0; i < links.size(); i++)	
			{
				// The url is added successfully, increase the pageId by 1
				if(index.addEntry(Integer.toString(id),links.get(i)));
					id++;	
			}
			index.printALL();
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream("spider_result.txt"), "utf-8"));
			// Add first 30 page into PageStruc
			for(int i=1;i<31;i++)
			{
				PageStruc struc = new PageStruc(Integer.toString(i),recman,"PageStruc");
				LinkExtractor link = new LinkExtractor(index.getURL(Integer.toString(i)));
				struc.setTitle();
				struc.setLmDate();
				struc.setSize();
				struc.setNode(link.extractLinks());

				 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			     Date lm = new Date(struc.getLmDate());
			    
			     
				writer.write("Page title: " + struc.getTitle() + "\n");
	            writer.write("URL: " + struc.getUrl() + "\n");
	              writer.write("Last modification date: " + sdf.format(lm) + "\n");
	              writer.write("Size: " + struc.getSize() + "\n");
	              for(int k=0;k<invertedIndex.size();k++)
	  			{
	  				Set<String> keyList = invertedIndex.keySet();
	  				Iterator<String> iter = keyList.iterator();
	  				String key;
	  				while((key = (String)iter.next()) != null)
	  				{
	  					writer.write("keyword1: " + key);
	  					for(int l=1;l<invertedIndex.get(key).size();l++)
	  						writer.write(invertedIndex.get(key).get(l).getPageId() + "," + invertedIndex.get(key).get(l).getTF());
	  				}
	              Vector<String> node = struc.getNode();
				     for(int j=0; j<node.size(); j++){
				           writer.write("Children link: " + node.get(j) + "\n");
				       }
				    
	          
	              writer.write("\n");
	              writer.write("------------------------------------------------------\n");
	              writer.write("\n");
			}
			index.finalize();
			writer.close();
			System.out.println();
			}
		}
		catch (Exception e)
        {
			System.err.println(e.toString());
        }

	}
}
	
