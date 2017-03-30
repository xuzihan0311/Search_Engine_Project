package comp4321;


import java.io.*;
import java.util.Vector;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class StopStem
{
	private Porter porter;
	private java.util.HashSet stopWords;
	public boolean isStopWord(String input)
	{
		return stopWords.contains(input);	
	}
	public StopStem(String str) throws Exception
	{
		super();
		porter = new Porter();
		stopWords = new java.util.HashSet();
				
		FileReader fr = new FileReader(str);	
		BufferedReader in = new BufferedReader(fr);
		String line;
		while((line = in.readLine()) != null)
		{
			stopWords.add(line);
		}
		
	}
	public String stem(String str)
	{
		return porter.stripAffixes(str);
	}
	
	/*
	public static void main(String[] arg) throws Exception
	{
		StopStem stopStem = new StopStem("stopwords.txt");

		try{
				//System.out.print("Please enter a single English word: ");
				//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				//input = in.readLine();
				Vector<String> input = new Vector<String>();
				RecordManager recman = RecordManagerFactory.createRecordManager("testRM");
				mappingKeyword index = new mappingKeyword(recman,"mappingKeyword");
				
				
				input.addElement("stopword");
				input.addElement("again");
				input.addElement("!!!");
				input.addElement("321");
				input.addElement("Success");
				
				for(int i=0;i<input.size();i++)
				{
					if (stopStem.isStopWord(input.get(i)))
					{
						System.out.println(input.get(i) + " is Stop Word");
						continue;
					}
					else
					{
			   			index.addEntry(input.get(i));
			   			System.out.println(input.get(i) + " is added");
					}
				}
				index.printALL();
				
	
		}
		catch(IOException ioe)
		{
			System.err.println(ioe.toString());
		}
	}*/
}
