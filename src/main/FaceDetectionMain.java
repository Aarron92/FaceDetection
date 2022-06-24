package main;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import utils.ImageUtils;

/**
 * Main class for image processing and face detection
 */
public class FaceDetectionMain
{
	/**
	 * Static block to load the native library
	 * 
	 * When working with native code and JNI resources, the libraries that are being used all need
	 * to be loaded in order to use their various methods
	 */
	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args)
	{
		// Load the image into a matrix object
		Mat originalImg = Imgcodecs.imread("C:/Users/evansao/Pictures/faces.jpg");
		
//		// process the image by converting to greyscale and detecting the edges
//		Mat processedImg = convertToGreyScaleAndDetectEdges(originalImg);
		
		// get a greyscale version of the original image and equalize the histogram to adjust the contrast and improve detection
        Mat processedImg = new Mat();
		Imgproc.cvtColor(originalImg, processedImg, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(processedImg, processedImg);
		
		/*
		 * Get the face recognition classifier algorithm - imported as an XML
		 *  
		 * A cascading classifier is a machine learning algorithm that takes the image matrix and uses
		 * a weighted neural network to detect faces
		 * 
		 * This XML file is included with the OpenCV library
		 */
		CascadeClassifier classifier = new CascadeClassifier("C:/Program Files/opencv/build/etc/haarcascades/haarcascade_frontalface_alt.xml");
		
		// create a matrix of rectangles, with rectangles being used to highlight faces found in an image
		MatOfRect faceRects = new MatOfRect();
		
		// detect the faces in the image and record them with a rectangle in the matrix
		classifier.detectMultiScale(processedImg, faceRects);
		
		// image to display
		Mat displayImg = new Mat();
		Imgproc.cvtColor(processedImg, displayImg, Imgproc.COLOR_GRAY2BGR); // TODO: colour isn't being added back in for some reason
		
		// loop over the rectangles, colour them and display them on the image with the faces
		for(Rect rectangle : faceRects.toArray())
		{
			Imgproc.rectangle(displayImg, rectangle.tl(), rectangle.br(), new Scalar(0, 0, 255), 2);
		}
		
//		// find any contours in the image
//		List<MatOfPoint> contours = findContours(processedImg);
//		
//		// display the contours on the image
//		Mat displayImgWithContours = displayContoursOnImage(originalImg, contours);
		
		// Save the image to an output file
		 Imgcodecs.imwrite("C:/Users/evansao/Pictures/Image Processing Output/output.png", displayImg);
		
		// display the image with all the processing on screen
		ImageUtils.display(displayImg, 612*2, 408*2);
		
		// println just used so I know that it's gone all the way through and saved the new image
		System.out.println("Image processed...");
	}

	/**
	 * Display contours on an image
	 * 
	 * @param image the image to display the contours on
	 * @param contours contours as a list of point matrices
	 * @return return the image with the contours displayed on it
	 */
	private static Mat displayContoursOnImage(Mat image, List<MatOfPoint> contours)
	{
		// Display the contours that were found by taking the original image and drawing the contours
		// over it in a colour (red in this case)
		Mat displayImg = image.clone();
		Imgproc.drawContours(displayImg, contours, -1, new Scalar(0, 0, 255), 2);
		
		return displayImg;
	}

	/**
	 * Given an image, convert it to greyscale and perform Canny edge detection on it
	 * 
	 * @param originalImg the image to process
	 * @return return the processed greyscale image with edges detected
	 */
	private static Mat convertToGreyScaleAndDetectEdges(final Mat originalImg)
	{
		Mat processedImg = new Mat();
		
		// First thing to do is to convert pictures to greyscale
		// Much better for the algorithms because of the increased contrast
		Imgproc.cvtColor(originalImg, processedImg, Imgproc.COLOR_BGR2GRAY);
		
		// Next, use a Canny edge detection algorithm to detect edges in the greyscale image
		Imgproc.Canny(processedImg, processedImg, 50, 200);
		
		return processedImg;
	}
	
	/*
	 * Find the contours in the pictures, this is where the edges in the image are as a set of points
	 * 
	 * Clones the image as the source for finding contours so that you don't lose the greyscale image because
	 * the contour detection overwrites it
	 * 
	 * @param image the image to find the contours in
	 * @return return a List of point matrices that represent the contours
	 */
	private static List<MatOfPoint> findContours(final Mat image)
	{
		/*
		 * Find the contours in the pictures, this is basically where the edges in the image are as a set of points
		 * 
		 * Remember to clone the image as the source so that you don't lose the greyscale image because
		 * the contour detection overwrites it
		 */
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(image.clone(), contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		
		return contours;
	}
}