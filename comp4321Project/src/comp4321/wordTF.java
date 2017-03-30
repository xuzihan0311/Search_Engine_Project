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

    wordTF(String id) throws IOException
    {
       	pageID = id;
       	TF = 1;
    }
    
    public int getTF() throws IOException
    {
    	return TF;
    }
    
    public String getPageId() throws IOException
    {
    	return pageID;
    }
    
    public void addTF()
    {
    	TF++;
    }
}

