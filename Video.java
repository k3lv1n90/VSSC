import java.net.InetAddress;
/**
 * Write a description of class Video here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Video
{
    private String fileName;
    private InetAddress owner;

    /**
     * Constructor for objects of class Video
     * 
     * @param fileName the file name
     * @param size the file size
     */
    public Video(String fileName, InetAddress owner)
    {
        this.fileName = fileName;
        this.owner = owner;
    }

    /**
     * An accessor method to return the file name
     *
     * @return fileName the file name
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * An accessor method to return the file host
     *
     * @return fileName the file host
     */
    public InetAddress getOwner()
    {
        return owner;
    }

    /**
     * A method to return details of video file
     *
     * @return fileName the file name
     */
    public String toString()
    {
        return fileName + "," + owner.getHostAddress();
    }
}
