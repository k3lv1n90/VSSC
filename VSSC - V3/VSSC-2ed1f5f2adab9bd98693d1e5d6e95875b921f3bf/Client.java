import java.util.ArrayList;
import java.time.LocalDateTime;
import java.io.File;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.io.IOException;
import java.net.UnknownHostException;
import java.net.SocketException;

/**
 * Write a description of class Client here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Client
{
    private String userName;

    private String serverIP;
    private String myIP;

    private InetAddress serverAddress;
    private InetAddress myAddress;

    private int udpPort;
    private final int tcpPort = 64599;

    private DatagramSocket outSocket;
    private DatagramSocket inSocket;

    private Thread serverListener;
    private Thread clientListener;

    DatagramPacket inPacket;
    private byte[] buf;

    private ArrayList<Video> myVideos;
    private ArrayList<Video> onlineVideos;
    private File folder;
    private boolean online;

    private String outMsg;
    private String inMsg;

    public Client(String userName, String serverIP, int udpPort) throws UnknownHostException, SocketException
    {
        this.userName = userName;
        this.serverIP = serverIP;
        this.serverAddress = InetAddress.getByName(serverIP);
        this.udpPort = udpPort;
        try
        {
            Socket s = new Socket("10.0.0.1", 80);
            myAddress = s.getLocalAddress();
            myIP = myAddress.getHostAddress();
            s.close();
        }
        catch(IOException e)
        {
            System.out.println("Failed to get Local host address");
            myAddress = InetAddress.getByName("10.0.0.1");
        }
        outSocket = new DatagramSocket();
        inSocket = new DatagramSocket(this.udpPort);
        buf = new byte[1024];
        inPacket = new DatagramPacket(buf, buf.length);
        myVideos = new ArrayList<Video>();
        onlineVideos = new ArrayList<Video>();
        folder = new File("./videos");
        this.online = false;
    }

    public boolean goOnline() throws IOException
    {
        String handShake = "I'm online!," + userName;
        DatagramPacket hSPacket1 = new DatagramPacket(handShake.getBytes(), handShake.getBytes().length, serverAddress, udpPort);
        outSocket.send(hSPacket1);
        inSocket.receive(inPacket);
        String inMsg = new String(inPacket.getData(), 0, inPacket.getLength());
        if(inMsg.equals("You are online!"))
        {
            Start.online = true;
            sendMyVideoList();
            serverListener = new Thread(new ServerListener(outSocket, inSocket, inPacket, buf, onlineVideos, myIP));
            serverListener.start();
            clientListener = new Thread(new ClientListener());
            clientListener.start();
        }
        return online;
    }

    /**
     * A method to update local videos list from videos folder
     *
     */
    public void updateMyVideoList()
    {
        myVideos.clear();
        File[] listOfFiles = folder.listFiles();
        for(int i = 0; i < listOfFiles.length; i++)
        {
            myVideos.add(new Video(listOfFiles[i].getName(), myAddress));
        }
    }

    public void sendMyVideoList()
    {
        try
        {
            DatagramPacket myVideosDP = new DatagramPacket(getMyVideosString().getBytes(), getMyVideosString().getBytes().length, serverAddress, udpPort);
            outSocket.send(myVideosDP);
        }
        catch(IOException e)
        {
            System.out.println(
                "============================================\n" +
                "IOException in Client.sendMyVideoList : " +
                "\n" + e + 
                "\n============================================");
        }
    }

    public void requestVideo(Video v)
    {
        String ip = v.getOwnerSTR();
        String fileName = v.getFileName();
        Thread requestVideo = new Thread(new VideoDownloader(ip,fileName));
    }
    

    public boolean goOffline() throws IOException
    {
        String handShake = "bye!," + userName;
        DatagramPacket byePacket = new DatagramPacket(handShake.getBytes(), handShake.getBytes().length, serverAddress, udpPort);
        outSocket.send(byePacket);
        serverListener.interrupt();
        clientListener.interrupt();
        return true;
    }

    /**
     * An accessor method to return the list of local videos
     *
     * @return myVideos the list of local videos
     */
    public ArrayList<Video> getMyVideoList()
    {
        return myVideos;
    }

    /**
     * An accessor method to return the list of online videos
     *
     * @return onlineVideos the list of online videos
     */
    public ArrayList<Video> getOnlineVideoList()
    {
        return onlineVideos;
    }

    public String getMyVideosString()
    {
        String myVideosString = "myVideosList";
        for(Video v: myVideos)
        {
            myVideosString += "," + v.toString();
        } 
        return myVideosString;
    }

    public void addVideo(String fileName)
    {
        myVideos.add(new Video(fileName, myAddress));
    }

    public Video findOnlineVideo(String fileName)
    {
        for(Video v : onlineVideos)
        {
            if(v.getFileName().equals(fileName))
            {
                return v;   
            }
        }
        return null;
    }
}
