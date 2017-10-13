import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Write a description of class ServerListener here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ServerListener implements Runnable
{
    private DatagramSocket outSocket;
    private DatagramSocket inSocket;
    private DatagramPacket inPacket;
    private byte[] buf;
    private ArrayList<Video> onlineVideos;
    private boolean online;
    private String inMsg;
    public ServerListener(DatagramSocket outSocket, DatagramSocket inSocket, DatagramPacket inPacket, byte[] buf, ArrayList<Video> onlineVideos, boolean online)
    {
        this.inSocket = inSocket;
        this.inPacket = inPacket;
        this.buf = buf;
        this.onlineVideos = onlineVideos;
        this.online = online;
    }

    public void run()
    {
        try
        {
            while(true)
            {
                inSocket.receive(inPacket);
                inMsg = new String(inPacket.getData(), 0, inPacket.getLength());
                if(inMsg.equals("You are offline!"))
                {
                    online = false;
                    outSocket.close();
                    inSocket.close();
                    break;
                }
                else
                {
                    Thread updateList = new Thread(new ListUpdater(inMsg, onlineVideos));
                }
            }
        }
        catch(IOException e)
        {
            System.out.println("serverListener : Connection failed to server!");
        }
    }
}