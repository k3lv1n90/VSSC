import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.SocketException;
/**
 * This class is responsible to start the application.
 * 
 * @author  Deniz Sumner
 * @version v1.0.1738
 */
public class Start
{
    private static Scanner reader = new Scanner(System.in);
    private static Client client;  
    public static boolean online = false;
    /**
     * The application starts execution from this method.
     * The console UserInterface is called from this method.
     * Company class object is initialized from this method.
     * 
     */
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Enter a username : ");
            String userName = reader.nextLine();
            System.out.println("Enter server address (IP) : ");
            String serverIP = reader.nextLine();
            System.out.println("Enter connection port : (i.e. 4501)");
            int udpPort = Integer.parseInt(reader.nextLine());

            //String userName = "deniz";  //hc
            //String serverIP = "192.168.1.8";    //hc
            //System.out.println("Username : " + userName);
            //System.out.println("Server   : " + serverIP);

            client = new Client(userName, serverIP, udpPort);
            client.updateMyVideoList();
            printMenu();
        }
        catch(UnknownHostException e)
        {
            System.out.println(
                "============================================\n" +
                "UnknownHostException in main : " +
                "\n" + e + 
                "\n============================================");
        }
        catch(SocketException e)
        {
            System.out.println(
                "============================================\n" +
                "SocketException in main : " +
                "\n" + e + 
                "\n============================================");
        }
    }

    public static void printMenu()
    {
        while(true)
        {
            switch (menuSelection()) 
            {
                case 1: goOnline(); break;
                case 2: printOnlineVideos(); break;
                case 3: requestVideo(); break;
                case 4: client.updateMyVideoList(); break;
                case 5: printLocalVideos(); break;
                case 6: addLocalVideo(); break;
                case 7: removeLocalVideo(); break;
                case 8: goOffline(); break;
                case 0: return;
            }
        }
    }

    public static int menuSelection()
    {
        String status;
        if(online){status = "Online";} else {status = "Offline";}
        System.out.println("" +
            "\n============================================" +
            "\nStatus: " + status + 
            "\n============================================" +
            "\n[1] Connect to server" +
            "\n[2] Display online video list" +
            "\n[3] Download a video" +
            "\n[4] Refresh local video list" +
            "\n[5] Display local video list" +
            "\n[6] Add file to local video list" +
            "\n[7] Remove file from local video list" +
            "\n[8] Disconnect from server" +
            "\n[0] Exit" +
            "\nSelect option : " +
            "\n============================================");
        return Integer.parseInt(reader.nextLine());
    }

    public static void goOnline()
    {
        try
        {
            if(client.goOnline())
            {
                System.out.println("Connected to server.");
            } else {
                System.out.println("Connection to server failed");
            }
        }
        catch(IOException e)
        {
            System.out.println(
                "============================================\n" +
                "IOException in Client.goOnline : " +
                "\n" + e + 
                "\n============================================");
        }
    }

    public static void goOffline()
    {
        try
        {
            client.goOffline();
            System.out.println("Disconnected from server");
        }
        catch(IOException e)
        {
            System.out.println("Disconnection from server failed");
            System.out.println(
                "============================================\n" +
                "IOException in Client.goOffline : " +
                "\n" + e + 
                "\n============================================");
        }
    }

    public static void refreshLocalVideos()
    {
        client.updateMyVideoList();
        client.sendMyVideoList();
    }

    public static void printOnlineVideos()
    {
        System.out.println(
            "============================================\n" +
            "ONLINE VIDEO LIST" + 
            "\n============================================");
        for(Video v : client.getOnlineVideoList())
        {
            System.out.println(v.getOwnerINET() + " : " + v.getFileName());
        }
        System.out.print("============================================");
    }

    public static void printLocalVideos()
    {
        System.out.println(
            "============================================\n" +
            "LOCAL VIDEO LIST" + 
            "\n============================================");
        for(Video v : client.getMyVideoList())
        {
            System.out.println(v.getFileName());
        }
        System.out.print("============================================");
    }

    public static void addLocalVideo()
    {
        System.out.println(
            "============================================\n" +
            "ADD LOCAL VIDEO TO LIST" + 
            "\n============================================\n" + 
            "Enter filename : ");
        String fileName = reader.nextLine();
        client.addVideo(fileName);
        client.sendMyVideoList();
    }

    public static void removeLocalVideo()
    {
        System.out.println(
            "============================================\n" +
            "REMOVE LOCAL VIDEO FROM LIST" + 
            "\n============================================");
        for(Video v : client.getMyVideoList())
        {
            System.out.println(v.getFileName());
        }
        System.out.println("Enter filename : ");
        String fileName = reader.nextLine();
        for(int i = 0; i < client.getMyVideoList().size(); i++)
        {
            if(fileName.equals(client.getMyVideoList().get(i).getFileName()))
            {
                client.getMyVideoList().remove(i);
            }
        }
        client.sendMyVideoList();
    }

    public static void requestVideo()
    {
        printOnlineVideos();

        boolean found = false;   
        System.out.println("");
        System.out.println("\nPlease choose the video you would like to download/Stream: ");
        do{            
            String fileName = reader.nextLine();
            Video v = client.findOnlineVideo(fileName);

            if (v == null)
            {
                System.out.println("Video does not exist. Please try again.");
                found = false;
            }
            else
            {
                client.requestVideo(v);
                found = true;
            }    

        } while (found == false);
    }
}
