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
            client = new Client(userName, serverIP);
        }
        catch(UnknownHostException e)
        {
            System.out.println("Server address is not valid!");
        }
        catch(SocketException e)
        {
            System.out.println("Failed to create socket.");
        }
        
        /**GO ONLINE*/
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
            System.out.println("Unable to connect server!");
        }
        
        /**GO OFFLINE*/
        try
        {
            if(client.goOffline())
            {
                System.out.println("Disconnected from server");
            } else {
                System.out.println("Disconnection from server failed");
            }
        }
        catch(IOException e)
        {
            System.out.println("Unable to disconnect server!");
        }
    }

}
