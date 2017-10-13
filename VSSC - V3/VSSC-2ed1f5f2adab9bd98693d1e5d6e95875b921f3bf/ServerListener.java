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
    private String myIP;
    private byte[] buf;
    private ArrayList<Video> onlineVideos;
    private String inMsg;
    public ServerListener(DatagramSocket outSocket, DatagramSocket inSocket, DatagramPacket inPacket, byte[] buf, ArrayList<Video> onlineVideos, String myIP)
    {
        this.inSocket = inSocket;
        this.inPacket = inPacket;
        this.buf = buf;
        this.onlineVideos = onlineVideos;
        this.myIP = myIP;
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
                    Start.online = false;
                    System.out.println("received packet: You are offline!");
                    outSocket.close();
                    inSocket.close();
                    break;
                }
                else
                {
                    Thread listUpdater = new Thread(new ListUpdater(inMsg, onlineVideos, myIP));
                    listUpdater.start();
                }
            }
        }
        catch(IOException e)
        {
            System.out.println("serverListener : Connection failed to server!");
        }
    }
}