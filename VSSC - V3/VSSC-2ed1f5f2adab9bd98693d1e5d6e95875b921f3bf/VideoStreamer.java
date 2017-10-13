import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Write a description of class VideoStreamer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VideoStreamer implements Runnable
{

    private Socket socketPort;
    public VideoStreamer(Socket socketPort)
    {
        this.socketPort = socketPort;
    }

    public void run()
    {
        try {
            DataInputStream input = new DataInputStream(socketPort.getInputStream());
            String videoName = input.readUTF();

            //The InetAddress specification
            // automatically get local ip
            InetAddress IA = InetAddress.getLocalHost();

            //Specify the file
            File file = new File("./videos/"+videoName);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis); 

            //Get socket's output stream
            OutputStream os = socketPort.getOutputStream();

            //Read File Contents into contents array 
            byte[] contents;
            long fileLength = file.length(); 
            long current = 0;
            long start = System.nanoTime();

            while(current!=fileLength){ 
                int size = 1000000;
                if(fileLength - current >= size)
                    current += size;    
                else{ 
                    size = (int)(fileLength - current); 
                    current = fileLength;
                } 
                contents = new byte[size]; 
                bis.read(contents, 0, size); 
                os.write(contents);                 
                //os.write(CryptoTest1.doEncrypt(contents,keyByte));                   
                System.out.print("Sending file to "+ socketPort.getInetAddress().toString() +" " +(current*100)/fileLength+"% complete!\n");                
            }   

            os.flush(); 
            //File transfer done. Close the socket connection!
            socketPort.close();
            // ssock.close();
            System.out.println("File sent succesfully!");

            System.out.println(
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
            
        } catch (Exception e)
        {
            System.out.println("ggwp");   
            System.out.println(e);
        }
        //Start.menuSelection();
    }
}
