import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
/**
 * Write a description of class VideoDownloader here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VideoDownloader implements Runnable
{
    private String ipAddress;
    private String videoName;

    public VideoDownloader (String ipAddress, String videoName)
    {
        this.ipAddress = ipAddress;
        this.videoName = videoName;
        run();
    }

    public void run()
    {
        try {
            requestFile(ipAddress,videoName);        
        } catch (Exception e)
        {
            System.out.println("Could not load file, please try again");
        }
    }

    public void requestFile(String IP, String videoName) throws Exception{
        //Initialize socket
        Socket socket = new Socket(InetAddress.getByName(IP), 6012);
        DataOutputStream output = new DataOutputStream( socket.getOutputStream());
        output.writeUTF(videoName);

        byte[] contents = new byte[1000000];
        //Initialize the FileOutputStream to the output file's full path.

        FileOutputStream fos = new FileOutputStream("./videos/"+videoName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        InputStream is = socket.getInputStream();

        System.out.println("Receiving File");
        ProcessBuilder pb = new ProcessBuilder("C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe", "D:\\Temp\\"+videoName);
        Process start = pb.start(); 
        //No of bytes read in one read() call
        int bytesRead = 0; 
        while((bytesRead=is.read(contents))!=-1){
            System.out.println("Bytes Received: " + bytesRead);

            //contents = (CryptoFile.doDecrypt(contents,keyByte));                        
            bos.write(contents, 0, bytesRead); 

        }

        bos.flush(); 
        socket.close(); 

        System.out.println("File saved successfully!");
    }

}
