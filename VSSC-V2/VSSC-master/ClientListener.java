import java.net.Socket;
import java.net.ServerSocket;
/**
 * Write a description of class ClientListener here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ClientListener implements Runnable
{

    // /**
    // * Constructor for objects of class ClientListener
    // */
    // public ClientListener()
    // {
    // run();
    // }

    public void run()
    {
        int i = 0;
        ServerSocket ssock = null;
        try {
            ssock = new ServerSocket(6012);
        } catch (Exception e)
        {
            System.out.println("Some Errors Happened");
            System.out.println(e);
        }

        while (true){
            VideoStreamer VC;
            try {
                VC = new VideoStreamer(ssock.accept());            
                Thread t = new Thread(VC);
                t.start();
            } catch (Exception e)
            {
                System.out.println(e);   
            }
        }        
    }

    // /**GET CODE FROM VIN BROTHERS!*/
    // try
    // {
    // while(true)
    // {
    // //listen tcp
    // Thread streamVideo = new Thread(new VideoStreamer());
    // }
    // }
    // catch(Exception e)
    // {
    // System.out.println("Exception (ServerListener run()) occured!");
    // }

}
