/* --
COMP4321 Phase1
wordTF
*/
package comp4321;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;
import java.util.Vector;
import java.io.IOException;
import java.io.Serializable;

public class wordTF
{
    private String pageID;
    private int TF;

    wordTF(String id, int tf) throws IOException
    {
       	pageID = id;
       	TF = tf;
    }
    
    public int getTF() throws IOException
    {
    	return TF;
    }
    
    /*public static void main(String[] args)
    {
    	try{
    		wordTF w1 = new wordTF("1",20);
    		int result = w1.getTF();
    		System.out.println(result);
    	}catch(Exception ex){}
    }*/
}

