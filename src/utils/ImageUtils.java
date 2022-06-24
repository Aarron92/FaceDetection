package utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Utility class for image processing
 */
public class ImageUtils
{
	/**
	 * Java implementation of the method used to display an image on screen - equivalent of the C++ OpenCV library version
	 * @param img - the image to display
	 * @param width - the width of the image to display
	 * @param height - the height of the image to display
	 */
	public static void display(final Mat img, final double width, final double height) 
	{
		// resize the image - parameters can be adjusted to the size as needed
	    Imgproc.resize(img, img, new Size(width, height));
	    
	    // encode the image to a byte matrix and then use that to create a byte array
	    MatOfByte matOfByte = new MatOfByte();
	    Imgcodecs.imencode(".jpg", img, matOfByte);
	    byte[] byteArray = matOfByte.toArray();
	   
	    BufferedImage bufImage = null;
	    try 
	    {
	    	// now attempt to read the byte array into an input stream, which can then be read into an image
	        InputStream in = new ByteArrayInputStream(byteArray);
	        bufImage = ImageIO.read(in);
	        
	        // now use Java Swing to create a frame element and display the image in it
	        JFrame frame = new JFrame();
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
	        frame.pack();
	        frame.setVisible(true);
	    } 
	    catch (final Exception e) 
	    {
	    	System.out.println("Error when trying to display the image");
	        e.printStackTrace();
	    }
	}
}