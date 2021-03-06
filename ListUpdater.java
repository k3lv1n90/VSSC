import java.util.ArrayList;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Write a description of class ListUpdater here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ListUpdater implements Runnable
{
    private String inMsg;
    private ArrayList<Video> onlineVideos;
    /**
     * Constructor for objects of class ListUpdater
     */
    public ListUpdater(String inMsg, ArrayList<Video> onlineVideos)
    {
        this.inMsg = inMsg;
        this.onlineVideos = onlineVideos;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void run()
    {
        try
        {
            String[] v = inMsg.split(",");
            for(int i = 0; i < v.length; i = i + 2)
            {
                if(InetAddress.getByName(v[i]).equals(InetAddress.getLocalHost()))
                {
                    //nah, this is my video!
                }
                else
                {
                    onlineVideos.add(new Video(v[i+1], InetAddress.getByName(v[i])));
                }
            }
            System.out.println("Online videos list updated!");
        }
        catch(Exception e)
        {
            System.out.println("Exception in ListUpdater : " + e);
        }
    }
}
