import java.util.ArrayList;
import java.time.LocalDateTime;
import java.io.File;

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
    private InetAddress serverAddress;
    private InetAddress myAddress;

    private final int udpPort = 4599;
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

    public Client(String userName, String serverIP) throws UnknownHostException, SocketException
    {
        this.userName = userName;
        this.serverIP = serverIP;

        this.serverAddress = InetAddress.getByName(serverIP);
        myAddress = InetAddress.getLocalHost();
        
        outSocket = new DatagramSocket();
        inSocket = new DatagramSocket(udpPort);
        
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
            online = true;
            sendMyVideoList();
            serverListener = new Thread(new ServerListener(outSocket, inSocket, inPacket, buf, onlineVideos, online));
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

    public void sendMyVideoList() throws IOException
    {
        DatagramPacket myVideosDP = new DatagramPacket(getMyVideosString().getBytes(), getMyVideosString().getBytes().length, serverAddress, udpPort);
        outSocket.send(myVideosDP);
    }

    public void requestVideo()
    {
        Thread requestVideo = new Thread(new VideoDownloader("10.0.0.1","papa.mp4"));        
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
    public ArrayList getMyVideoList()
    {
        return myVideos;
    }

    /**
     * An accessor method to return the list of online videos
     *
     * @return onlineVideos the list of online videos
     */
    public ArrayList getOnlineVideoList()
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
}
