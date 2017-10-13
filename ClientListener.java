
/**
 * Write a description of class ClientListener here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ClientListener implements Runnable
{

    /**
     * Constructor for objects of class ClientListener
     */
    public ClientListener()
    {
        /**GET CODE FROM VIN BROTHERS!*/
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void run()
    {
        /**GET CODE FROM VIN BROTHERS!*/
        try
        {
            while(true)
            {
                //listen tcp
                Thread streamVideo = new Thread(new VideoStreamer());
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception (ServerListener run()) occured!");
        }
    }
}
